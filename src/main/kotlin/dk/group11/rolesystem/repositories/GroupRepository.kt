package dk.group11.rolesystem.repositories

import dk.group11.rolesystem.models.ApplicationGroup
import org.springframework.data.repository.CrudRepository

interface GroupRepository : CrudRepository<ApplicationGroup, Long> {
    fun existsByTitle(title: String): Boolean
    fun findByMembersId(id: Long): List<ApplicationGroup>
}