package dk.group11.rolesystem.users

import dk.group11.rolesystem.model.ApplicationUser
import org.springframework.data.repository.CrudRepository
import java.util.*

interface UserRepository : CrudRepository<ApplicationUser, UUID> {

    fun findByUsername(username: String): ApplicationUser
}