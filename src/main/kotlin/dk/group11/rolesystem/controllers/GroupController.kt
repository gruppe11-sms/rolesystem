package dk.group11.rolesystem.controllers

import dk.group11.rolesystem.model.ApplicationGroup
import dk.group11.rolesystem.services.GroupService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class GroupController(val groupService: GroupService) {

    @GetMapping("/api/groups")
    fun getGroups(): List<ApplicationGroup> {
        return groupService.getGroups()
    }

    @GetMapping("/api/groups/{id}")
    fun getGroup(@PathVariable id: UUID): ApplicationGroup {
        return groupService.getGroup(id)
    }

    @PostMapping("/api/groups")
    fun createGroups(@RequestBody group: ApplicationGroup) {
        groupService.createGroups(group)
    }

    @PutMapping("/api/groups")
    fun updateGroup(@RequestBody group: ApplicationGroup) {
        groupService.updateGroup(group)
    }

    @DeleteMapping("/api/groups/{id}")
    fun deleteGroup(@PathVariable id: UUID) {
        groupService.deleteGroup(id)
    }

}


