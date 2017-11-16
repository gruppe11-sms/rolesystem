package dk.group11.rolesystem.repositories

import dk.group11.rolesystem.models.ApplicationUser
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<ApplicationUser, Long> {
    fun findByUsername(username: String): ApplicationUser?
    fun existsByUsername(s: String): Boolean
    fun findBySystemRef(systemRef: Long): ApplicationUser?
}