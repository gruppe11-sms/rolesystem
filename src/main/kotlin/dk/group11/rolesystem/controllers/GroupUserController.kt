package dk.group11.rolesystem.controllers

import dk.group11.rolesystem.services.GroupService
import dk.group11.rolesystem.services.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/groups/{groupId}/users/")
class GroupUserController(private val groupService: GroupService,
                          private val userService: UserService) {

    @GetMapping
    fun getGroupUsers(@PathVariable groupId: Long): List<UserDTO> =
            groupService.getGroup(groupId).members.map { it.toDTO() }

    @GetMapping("/{userId}")
    fun getGroupUser(@PathVariable userId: Long): List<GroupDTO> =
            groupService.getGroupsByMemberId(userId).map { it.toDTO() }

    @PostMapping("/{userId}")
    fun addGroupUser(@PathVariable groupId: Long, @PathVariable userId: Long) =
            groupService.addGroupUser(groupId = groupId, userId = userId)

    @DeleteMapping("/{userId}")
    fun removeGroupUser(@PathVariable groupId: Long, @PathVariable userId: Long) =
            groupService.removeGroupUser(groupId = groupId, userId = userId)

    @PostMapping
    fun addGroupUsers(@PathVariable groupId: Long, @RequestBody userIds: List<Long>) {
        val group = groupService.getGroup(groupId)
        val users = userService.getUsers(userIds)
        group.members.addAll(users)
        groupService.updateGroup(group)
    }
}