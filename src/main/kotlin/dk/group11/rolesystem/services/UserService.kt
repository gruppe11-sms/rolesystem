package dk.group11.rolesystem.services

import dk.group11.rolesystem.clients.*
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


@Service
class UserService(private val userRepository: UserRepository,
                  private val bCryptPasswordEncoder: BCryptPasswordEncoder,
                  private val auditClient: AuditClient,
                  private val security: ISecurityService,
                  private val roleRepository: RoleRepository,
                  private val groupRepository: GroupRepository) : UserDetailsService {

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

    fun updateUser(user: ApplicationUser): ApplicationUser {
        userRepository.save(user)
        auditClient.createEntry("[RoleSystem] User updated", user.toAuditEntry(), security.getToken())

        return user
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