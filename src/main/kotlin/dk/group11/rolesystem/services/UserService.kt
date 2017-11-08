package dk.group11.rolesystem.services

import dk.group11.rolesystem.auditClient.AuditClient
import dk.group11.rolesystem.auditClient.UserWithRole
import dk.group11.rolesystem.auditClient.UserWithRoles
import dk.group11.rolesystem.auditClient.toAuditEntry
import dk.group11.rolesystem.models.ApplicationUser
import dk.group11.rolesystem.models.LoginUser
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
                  private val roleService: RoleService) : UserDetailsService {

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
        return userRepository.findOne(id)
    }

    fun createUser(user: ApplicationUser): ApplicationUser {
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

    fun updateUser(user: ApplicationUser) {
        userRepository.save(user)
        auditClient.createEntry("[RoleSystem] User updated", user.toAuditEntry(), security.getToken())
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

    fun addUserRole(userId: Long, roleId: Long) {
        val user = userRepository.findOne(userId)
        val role = roleService.getRole(roleId)
        user.roles.add(role)
        userRepository.save(user)
        auditClient.createEntry("[RoleSystem] User role added",
                UserWithRole(user.toAuditEntry(), role.toAuditEntry()), security.getToken())
    }

    fun removeUserRole(userId: Long, roleId: Long) {
        val user = userRepository.findOne(userId)
        val role = roleService.getRole(roleId)
        user.roles.remove(role)
        userRepository.save(user)
        auditClient.createEntry("[RoleSystem] Roles removed",
                UserWithRole(user.toAuditEntry(), role.toAuditEntry()), security.getToken())
    }

    fun updateUserRoles(userId: Long, roleIds: List<Long>) {
        val user = userRepository.findOne(userId)
        val roles = roleService.getRoles(roleIds)
        user.roles = roles.toMutableList()
        userRepository.save(user)
        auditClient.createEntry("[RoleSystem] Roles updated", UserWithRoles(user.toAuditEntry(), roles.map { it.toAuditEntry() }), security.getToken())
    }
}