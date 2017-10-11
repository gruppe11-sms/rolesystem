package dk.group11.rolesystem.controllers

import dk.group11.rolesystem.model.Group
import dk.group11.rolesystem.services.GroupService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class GroupController(val groupService: GroupService) {

    @GetMapping("/api/groups")
    fun getGroups(): List<Group> {
        return groupService.getGroups()
    }

    @GetMapping("/api/groups/{id}")
    fun getGroup(@PathVariable id: UUID): Group {
        return groupService.getGroup(id)
    }

    @PostMapping("/api/groups")
    fun createGroups(@RequestBody group: Group) {
        groupService.createGroups(group)
    }

    @PutMapping("/api/groups")
    fun updateGroup(@RequestBody group: Group) {
        groupService.updateGroup(group)
    }

    @DeleteMapping("/api/groups/{id}")
    fun deleteGroup(@PathVariable id: UUID) {
        groupService.deleteGroup(id)
    }

}


