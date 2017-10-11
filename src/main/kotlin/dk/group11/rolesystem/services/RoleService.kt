package dk.group11.rolesystem.services

import dk.group11.rolesystem.model.Role
import dk.group11.rolesystem.repositories.RoleRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class RoleService(private val roleRepository: RoleRepository) {

    init {
        val role: Role = Role(title = "PartyManager", description = "I love parties")
        roleRepository.save(role)
    }

    fun getRoles(): List<Role> {
        return roleRepository.findAll().toList()
    }

    fun getRole(id: UUID): Role {
        return roleRepository.findOne(id)
    }

    fun createRoles(role: Role) {
        roleRepository.save(role)
    }

    fun updateRole(role: Role) {
        roleRepository.save(role)
    }

    fun deleteRole(id: UUID) {
        roleRepository.delete(id)
    }


}