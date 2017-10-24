package dk.group11.rolesystem.controllers

import dk.group11.rolesystem.models.ApplicationGroup
import dk.group11.rolesystem.services.GroupService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/groups")
class GroupController(val groupService: GroupService) {

    @GetMapping
    fun getGroups(): List<GroupDTO> {
        return groupService.getGroups().map { it.toDTO() }
    }

    @GetMapping("/{id}")
    fun getGroup(@PathVariable id: Long): GroupDTO {
        return groupService.getGroup(id).toDTO()
    }

    @PostMapping
    fun createGroups(@RequestBody group: ApplicationGroup) {
        groupService.createGroup(group)
    }

    @PutMapping
    fun updateGroup(@RequestBody group: ApplicationGroup) {
        groupService.updateGroup(group)
    }

    @DeleteMapping("/{id}")
    fun deleteGroup(@PathVariable id: Long) {
        groupService.deleteGroup(id)
    }
}