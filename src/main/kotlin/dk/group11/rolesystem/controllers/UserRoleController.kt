package dk.group11.rolesystem.controllers

import dk.group11.rolesystem.models.Role
import dk.group11.rolesystem.services.UserService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/users/{id}/roles")
class UserRoleController(val userService: UserService) {

    @PutMapping
    fun updateUserRole(@PathVariable id: UUID, @RequestBody role: Role) {
        val user = userService.getUser(id)
        user.roles.add(role)
        userService.updateUser(user)
    }

    @DeleteMapping("/{roleId}")
    fun deleteUserRole(@PathVariable id: UUID, @PathVariable roleId: UUID) {
        val user = userService.getUser(id)
        user.roles.removeIf({ role -> role.id == roleId })
        userService.updateUser(user)
    }

    @PostMapping
    fun createUserRole(@PathVariable id: UUID, @RequestBody role: Role) {
        val user = userService.getUser(id)
        user.roles.add(role)
        userService.updateUser(user)
    }

    @GetMapping
    fun getUserRoles(@PathVariable id: UUID): List<Role> {
        return userService.getUser(id).roles
    }
}