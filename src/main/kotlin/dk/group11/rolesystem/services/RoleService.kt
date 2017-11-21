package dk.group11.rolesystem.services

import dk.group11.rolesystem.models.Role
import dk.group11.rolesystem.repositories.RoleRepository
import dk.group11.rolesystem.repositories.UserRepository
import dk.group11.rolesystem.security.SecurityService
import org.springframework.stereotype.Service

@Service
class RoleService(
        private val roleRepository: RoleRepository,
        private val securityService: SecurityService,
        private val userRepository: UserRepository
) {

    fun getRoles(): List<Role> {
        return roleRepository.findAll().toList()
    }

    fun getRole(key: String): Role {
        return roleRepository.findOne(key)
    }

    fun updateRole(role: Role) {
        roleRepository.save(role)
    }

    /**
     * Adds a new role to the system
     * This operation is idempotent, if the role exists, it's returned
     * otherwise it's created
     */
    fun addRole(key: String, title: String, description: String): Role {

        val existingRole = roleRepository.findOne(key)
        if (existingRole != null) {
            return existingRole
        }
        val role = Role(key, title, description)
        return roleRepository.save(role)
    }

}