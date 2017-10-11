package dk.group11.rolesystem.controllers

import dk.group11.rolesystem.model.Role
import dk.group11.rolesystem.services.RoleService
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
class RoleController(val roleService: RoleService) {

    @GetMapping("/api/roles")
    fun getRoles(): List<Role> {
        return roleService.getRoles()
    }

    @GetMapping("/api/roles/{id}")
    fun getRole(@PathVariable id: UUID): Role {
        return roleService.getRole(id)
    }

    @PostMapping("/api/roles")
    fun createRoles(@RequestBody role: Role) {
        roleService.createRoles(role)
    }

    @PutMapping("/api/roles")
    fun updateRole(@RequestBody role: Role) {
        roleService.updateRole(role)
    }

    @DeleteMapping("/api/roles/{id}")
    fun deleteRole(@PathVariable id: UUID) {
        roleService.deleteRole(id)
    }

}