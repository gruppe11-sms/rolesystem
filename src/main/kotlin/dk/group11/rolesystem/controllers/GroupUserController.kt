package dk.group11.rolesystem.controllers

import dk.group11.rolesystem.models.ApplicationGroup
import dk.group11.rolesystem.models.ApplicationUser
import dk.group11.rolesystem.services.GroupService
import dk.group11.rolesystem.services.UserService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/groups/{groupId}/users/")
class GroupUserController(private val groupService: GroupService,
                          private val userService: UserService) {

    @GetMapping
    fun getGroupUsers(@PathVariable groupId: UUID): List<ApplicationUser> {
        return groupService.getGroup(groupId).members
    }

    @GetMapping("/{userId}")
    fun getGroupUser(@PathVariable userId: UUID): List<ApplicationGroup> {
        val user = userService.getUser(userId)
        return groupService.getGroups().filter { group -> group.members.contains(user) }
    }

    @PostMapping("/{userId}")
    fun addGroupUser(@PathVariable groupId: UUID, @PathVariable userId: UUID) {
        val user = userService.getUser(groupId)
        groupService.getGroup(groupId).members.add(user)
    }

    @PostMapping
    fun addGroupUsers(@PathVariable groupId: UUID, @RequestBody ids: List<UUID>) {
        groupService.getGroup(groupId).members.addAll(userService.getUsers(ids))
    }

}