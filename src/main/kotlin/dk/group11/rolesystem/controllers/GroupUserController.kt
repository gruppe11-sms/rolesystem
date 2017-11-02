package dk.group11.rolesystem.controllers

import dk.group11.rolesystem.services.GroupService
import dk.group11.rolesystem.services.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/groups/{groupId}/users/")
class GroupUserController(private val groupService: GroupService,
                          private val userService: UserService) {

    @GetMapping
    fun getGroupUsers(@PathVariable groupId: Long): List<UserDTO> {
        return groupService.getGroup(groupId).members.map { it.toDTO() }
    }

    @GetMapping("/{userId}")
    fun getGroupUser(@PathVariable userId: Long): List<GroupDTO> {
        return groupService.getGroupsByMemberId(userId).map { it.toDTO() }
    }

    @PostMapping("/{userId}")
    fun addGroupUser(@PathVariable groupId: Long, @PathVariable userId: Long) {
        val group = groupService.getGroup(groupId)
        val user = userService.getUser(groupId)
        group.members.add(user)
        groupService.updateGroup(group)
    }

    @PostMapping
    fun addGroupUsers(@PathVariable groupId: Long, @RequestBody userIds: List<Long>) {
        val group = groupService.getGroup(groupId)
        val users = userService.getUsers(userIds)
        group.members.addAll(users)
        groupService.updateGroup(group)
    }

    /*@PutMapping
    fun updateGroupUsers(@PathVariable id: Long, @RequestParam(name = "groupids") groupIds: String) {
        val ids = groupIds.split(delimiters = ",").mapNotNull { s -> s.toLongOrNull() }
        val user = userService.getUser(id)
        val roles = roleService.getRoles(ids)
        user.roles.addAll(roles)
        userService.updateUser(user)
    }*/
}