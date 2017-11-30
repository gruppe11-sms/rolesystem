package dk.group11.rolesystem.services

import dk.group11.rolesystem.clients.*
import dk.group11.rolesystem.controllers.UserDTO
import dk.group11.rolesystem.controllers.toDTO
import dk.group11.rolesystem.exceptions.BadRequestException
import dk.group11.rolesystem.models.ApplicationUser
import dk.group11.rolesystem.models.LoginUser
import dk.group11.rolesystem.repositories.GroupRepository
import dk.group11.rolesystem.repositories.RoleRepository
import dk.group11.rolesystem.repositories.UserRepository
import dk.group11.rolesystem.security.ISecurityService
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional

data class updateUserAuditEntry(val before: UserDTO, val after: UserDTO)

@Service
class UserService(private val userRepository: UserRepository,
                  private val bCryptPasswordEncoder: BCryptPasswordEncoder,
                  private val auditClient: AuditClient,
                  private val security: ISecurityService,
                  private val roleRepository: RoleRepository,
                  private val groupRepository: GroupRepository,
                  private val roleVerifierService: RoleVerifierService) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails? {
        val user: ApplicationUser? = userRepository.findByUsername(username)
        if (user != null) {
            return LoginUser(
                    username = user.username,
                    password = user.password,
                    authority = emptyList<GrantedAuthority>().toMutableList()
            )
        } else {
            throw UsernameNotFoundException(username)
        }
    }

    fun getUser(id: Long): ApplicationUser {
        val user = userRepository.findOne(id)
        if (user != null) {
            return user
        } else {
            throw BadRequestException("Cant find user")
        }
    }

    fun getMe(): ApplicationUser {
        val me = getUser(security.getId())
        val roles = roleVerifierService.getRolesForUser(me)

        return me.copy(roles = roles.toMutableSet())
    }

    fun createUser(user: ApplicationUser): ApplicationUser {
        if (userRepository.existsByUsername(user.username)) {
            throw BadRequestException("Username already in use")
        }

        user.password = bCryptPasswordEncoder.encode(user.password)
        userRepository.save(user)

        auditClient.createEntry("[RoleSystem] User created", user.toAuditEntry(), security.getToken())

        return user
    }

    fun getUsers(ids: List<Long>): List<ApplicationUser> {
        return userRepository.findAll(ids.toMutableList()).toList()
    }

    fun getAllUsers(): List<ApplicationUser> {
        return userRepository.findAll().toList()
    }

    @Transactional
    fun updateUser(user: ApplicationUser): ApplicationUser {

        val currentUser = userRepository.findOne(user.id)

        auditClient.createEntry("[RoleSystem] Updating user", updateUserAuditEntry(currentUser.toDTO(true), user.toDTO(true)))

        currentUser.name = user.name
        currentUser.username = user.username

        currentUser.roles = user.roles.map { roleRepository.findOne(it.id) }.toMutableSet()

        currentUser.groups = user.groups.map { groupRepository.findOne(it.id) }.toMutableSet()

        return currentUser
    }

    fun deleteUser(id: Long) {
        val user = userRepository.findOne(id)
        userRepository.delete(user)
        auditClient.createEntry("[RoleSystem] User deleted", user.toAuditEntry(), security.getToken())
    }

    fun getUserByUsername(username: String): ApplicationUser {
        val user = userRepository.findByUsername(username)
        if (user != null) {
            return user
        } else
            throw UsernameNotFoundException(username)
    }

    fun addUserRole(userId: Long, roleId: String) {
        val user = userRepository.findOne(userId)
        val role = roleRepository.findOne(roleId)
        user.roles.add(role)
        userRepository.save(user)
        auditClient.createEntry("[RoleSystem] User role added",
                UserWithRole(user.toAuditEntry(), role.toAuditEntry()), security.getToken()
        )
    }

    fun removeUserRole(userId: Long, roleId: String) {
        val user = userRepository.findOne(userId)
        val role = roleRepository.findOne(roleId)
        user.roles.remove(role)
        userRepository.save(user)
        auditClient.createEntry("[RoleSystem] Roles removed", UserWithRole(user.toAuditEntry(), role.toAuditEntry()), security.getToken()
        )
    }

    fun updateUserRoles(userId: Long, roleIds: List<String>) {
        val user = userRepository.findOne(userId)
        val roles = roleRepository.findAll(roleIds)
        user.roles = roles.toMutableSet()
        userRepository.save(user)
        auditClient.createEntry("[RoleSystem] Roles updated",
                UserWithRoles(user.toAuditEntry(), roles.map { it.toAuditEntry() }), security.getToken()
        )
    }

    fun updateUserGroup(userId: Long, groupIds: List<Long>) {
        val user = userRepository.findOne(userId)
        val groups = groupRepository.findAll(groupIds)
        user.groups = groups.toMutableSet()
        userRepository.save(user)
        auditClient.createEntry("[RoleSystem] Groups updated",
                UserWithGroups(user = user.toAuditEntry(), group = groups.map { it.toAuditEntry() }),
                security.getToken()
        )
    }
}