package dk.group11.rolesystem.controllers

import dk.group11.rolesystem.helpers.toIDList
import dk.group11.rolesystem.services.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users/{userId}/roles")
class UserRoleController(val userService: UserService) {

    @GetMapping
    fun getUserRoles(@PathVariable userId: Long): List<RoleDTO> =
            userService.getUser(userId).roles.map { it.toDTO() }

    @PutMapping
    fun updateUserRole(@PathVariable userId: Long, @RequestParam(value = "roles") roleIdParam: String) =
            userService.updateUserRoles(userId = userId, roleIds = roleIdParam.toIDList())

    @DeleteMapping("/{roleId}")
    fun deleteUserRole(@PathVariable userId: Long, @PathVariable roleId: Long) =
            userService.removeUserRole(userId = userId, roleId = roleId)

    @PostMapping("/{roleId}")
    fun createUserRole(@PathVariable userId: Long, @PathVariable roleId: Long) =
            userService.addUserRole(userId = userId, roleId = roleId)
}