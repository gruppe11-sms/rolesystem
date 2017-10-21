package dk.group11.rolesystem.repositories

import dk.group11.rolesystem.models.ApplicationUser
import org.springframework.data.repository.CrudRepository
import java.util.*

interface UserRepository : CrudRepository<ApplicationUser, UUID> {

    fun findByUsername(username: String): ApplicationUser
    fun existsByUsername(s: String): Boolean
}