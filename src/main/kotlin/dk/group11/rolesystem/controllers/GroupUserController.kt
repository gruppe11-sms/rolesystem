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
        return groupService.getGroupsByMemberId(userId)
    }

    @PostMapping("/{userId}")
    fun addGroupUser(@PathVariable groupId: UUID, @PathVariable userId: UUID) {
        val group = groupService.getGroup(groupId)
        val user = userService.getUser(groupId)
        group.members.add(user)
        groupService.updateGroup(group)
    }

    @PostMapping
    fun addGroupUsers(@PathVariable groupId: UUID, @RequestBody userIds: List<UUID>) {
        val group = groupService.getGroup(groupId)
        val users = userService.getUsers(userIds)
        group.members.addAll(users)
        groupService.updateGroup(group)

    }
}