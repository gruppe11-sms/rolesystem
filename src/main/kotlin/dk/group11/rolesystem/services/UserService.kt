package dk.group11.rolesystem.services

import dk.group11.rolesystem.clients.AuditClient
import dk.group11.rolesystem.clients.toAuditEntry
import dk.group11.rolesystem.controllers.UserDTO
import dk.group11.rolesystem.controllers.toDTO
import dk.group11.rolesystem.exceptions.BadRequestException
import dk.group11.rolesystem.models.ApplicationUser
import dk.group11.rolesystem.models.LoginUser
import dk.group11.rolesystem.repositories.GroupRepository
import dk.group11.rolesystem.repositories.RoleRepository
import dk.group11.rolesystem.repositories.UserRepository
import dk.group11.rolesystem.security.ISecurityService
import dk.group11.rolesystem.security.USER_MANAGER_ROLE
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
        security.requireRoles(USER_MANAGER_ROLE)

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
        security.requireRoles(USER_MANAGER_ROLE)

        val currentUser = userRepository.findOne(user.id)

        auditClient.createEntry("[RoleSystem] Updating user", updateUserAuditEntry(currentUser.toDTO(true), user.toDTO(true)))

        currentUser.name = user.name
        currentUser.username = user.username

        currentUser.roles = user.roles.map { roleRepository.findOne(it.id) }.toMutableSet()

        currentUser.groups = user.groups.map { groupRepository.findOne(it.id) }.toMutableSet()

        return currentUser
    }

    fun deleteUser(id: Long) {
        security.requireRoles(USER_MANAGER_ROLE)

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
}