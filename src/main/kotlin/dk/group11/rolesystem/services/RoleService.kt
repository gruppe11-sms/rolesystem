package dk.group11.rolesystem.services

import dk.group11.rolesystem.models.Role
import dk.group11.rolesystem.repositories.RoleRepository
import org.springframework.stereotype.Service

@Service
class RoleService(private val roleRepository: RoleRepository) {

    fun getRoles(): List<Role> {
        return roleRepository.findAll().toList()
    }

    fun getRoles(roleIds: List<Long>): List<Role> {
        return roleRepository.findById(roleIds).toList()
    }

    fun getRole(id: Long): Role {
        return roleRepository.findOne(id)
    }

    fun createRole(role: Role): Role {
        return roleRepository.save(role)
    }

    fun updateRole(role: Role) {
        roleRepository.save(role)
    }

    fun deleteRole(id: Long) {
        roleRepository.delete(id)
    }
}