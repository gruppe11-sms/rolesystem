package dk.group11.rolesystem.controllers

import dk.group11.rolesystem.models.Role
import dk.group11.rolesystem.services.RoleService
import dk.group11.rolesystem.services.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users/{id}/roles")
class UserRoleController(private val userService: UserService, private val roleService: RoleService) {

    @PutMapping
    fun updateUserRole(@PathVariable id: Long, @RequestParam(name = "roles") roleIds: String) {
        val ids = roleIds.split(delimiters = ",").mapNotNull { s -> s.toLongOrNull() }
        val user = userService.getUser(id)
        val roles = roleService.getRoles(ids)
        user.roles = roles.toMutableList()
        userService.updateUser(user)
    }

    @DeleteMapping("/{roleId}")
    fun deleteUserRole(@PathVariable id: Long, @PathVariable roleId: Long) {
        val user = userService.getUser(id)
        user.roles.removeIf({ role -> role.id == roleId })
        userService.updateUser(user)
    }

    @PostMapping
    fun createUserRole(@PathVariable id: Long, @RequestBody role: Role) {
        val user = userService.getUser(id)
        user.roles.add(role)
        userService.updateUser(user)
    }

    @GetMapping
    fun getUserRoles(@PathVariable id: Long): List<RoleDTO> {
        return userService.getUser(id).roles.map { it.toDTO() }
    }
}