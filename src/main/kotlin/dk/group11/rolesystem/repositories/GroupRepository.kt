package dk.group11.rolesystem.repositories

import dk.group11.rolesystem.models.ApplicationGroup
import org.springframework.data.repository.CrudRepository
import java.util.*

interface GroupRepository : CrudRepository<ApplicationGroup, UUID> {
    fun existsByTitle(title: String): Boolean
}