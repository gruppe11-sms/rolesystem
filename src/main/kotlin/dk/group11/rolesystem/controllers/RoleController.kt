package dk.group11.rolesystem.controllers

import dk.group11.rolesystem.models.Role
import dk.group11.rolesystem.services.RoleService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/roles")
class RoleController(val roleService: RoleService) {

    @GetMapping
    fun getRoles(): List<RoleDTO> = roleService.getRoles().map { it.toDTO(false) }

    @GetMapping("/{key}")
    fun getRole(@PathVariable key: String): RoleDTO {
        return roleService.getRole(key).toDTO()
    }

    @PutMapping("/{key}")
    fun updateRole(@RequestBody role: Role) {
        roleService.updateRole(role)
    }

    @PostMapping
    fun createRole(@RequestBody role: Role): RoleDTO {
        return roleService.addRole(key = role.key, description = role.description, title = role.title).toDTO(false)
    }
}