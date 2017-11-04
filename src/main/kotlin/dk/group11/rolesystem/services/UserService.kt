package dk.group11.rolesystem.services

import dk.group11.rolesystem.auditClient.AuditClient
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

data class createUserAuditEntry(val username: String, val name: String, val userId: Long)

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

        auditClient.createEntry("[RoleSystem] User created", createUserAuditEntry(user.username, user.name, user.id), security.getToken())

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
    }

    fun deleteUser(id: Long) {
        userRepository.delete(id)
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
    }

    fun removeUserRole(userId: Long, roleId: Long) {
        val user = userRepository.findOne(userId)
        user.roles.removeIf { it.id == roleId }
        userRepository.save(user)
    }
}