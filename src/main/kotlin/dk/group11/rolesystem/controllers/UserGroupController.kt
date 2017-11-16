package dk.group11.rolesystem.controllers

import dk.group11.rolesystem.services.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users/{userId}/groups")
class UserGroupController(private val userService: UserService) {

    @PutMapping
    fun updateUserGroup(@PathVariable userId: Long, @RequestParam(value = "groups") groups: String) {
        userService.updateUserGroup(userId = userId, groupIds = groups.split(",").mapNotNull { it.toLongOrNull() })
    }
}