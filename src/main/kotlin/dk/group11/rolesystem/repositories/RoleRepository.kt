package dk.group11.rolesystem.repositories

import dk.group11.rolesystem.models.Role
import org.springframework.data.repository.CrudRepository

interface RoleRepository : CrudRepository<Role, Long> {
    fun existsByTitle(title: String): Boolean
    fun findByTitle(s: String): Role?
    fun findById(roleIds: List<Long>): List<Role>
}