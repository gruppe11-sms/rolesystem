package dk.group11.rolesystem.controllers

import dk.group11.rolesystem.models.Role
import dk.group11.rolesystem.services.RoleService
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/roles")
class RoleController(val roleService: RoleService) {

    @GetMapping
    fun getRoles(): List<Role> {
        return roleService.getRoles()
    }

    @GetMapping("/{id}")
    fun getRole(@PathVariable id: Long): Role {
        return roleService.getRole(id)
    }

    @PostMapping
    fun createRoles(@RequestBody role: Role) {
        roleService.createRole(role)
    }

    @PutMapping
    fun updateRole(@RequestBody role: Role) {
        roleService.updateRole(role)
    }

    @DeleteMapping("/{id}")
    fun deleteRole(@PathVariable id: Long) {
        roleService.deleteRole(id)
    }
}