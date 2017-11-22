package dk.group11.rolesystem.controllers

import dk.group11.rolesystem.models.Role
import dk.group11.rolesystem.security.RoleCreatorRole
import dk.group11.rolesystem.security.SecurityService
import dk.group11.rolesystem.services.RoleService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/roles")
class RoleController(val roleService: RoleService, private val securityService: SecurityService) {

    @GetMapping
    fun getRoles(): List<RoleDTO> = roleService.getRoles().map { it.toDTO(false) }

    @GetMapping("/{id}")
    fun getRole(@PathVariable key: String): RoleDTO {
        return roleService.getRole(key).toDTO()
    }

    @PutMapping("/{id}")
    fun updateRole(@RequestBody role: Role) {
        roleService.updateRole(role)
    }

    @PostMapping
    fun createRole(@RequestBody role: Role): RoleDTO {
        securityService.requireRoles(RoleCreatorRole)
        return roleService.addRole(key = role.id, description = role.description, title = role.title).toDTO(false)
    }
}