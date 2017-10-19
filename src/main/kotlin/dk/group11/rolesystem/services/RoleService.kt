package dk.group11.rolesystem.services

import dk.group11.rolesystem.models.Role
import dk.group11.rolesystem.repositories.RoleRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class RoleService(private val roleRepository: RoleRepository) {

    fun getRoles(): List<Role> {
        return roleRepository.findAll().toList()
    }

    fun getRole(id: UUID): Role {
        return roleRepository.findOne(id)
    }

    fun createRole(role: Role): Role {
        return roleRepository.save(role)
    }

    fun updateRole(role: Role) {
        roleRepository.save(role)
    }

    fun deleteRole(id: UUID) {
        roleRepository.delete(id)
    }


}