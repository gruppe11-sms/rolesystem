package dk.group11.rolesystem.controllers

import dk.group11.rolesystem.exceptions.BadRequestException
import dk.group11.rolesystem.models.ApplicationGroup
import dk.group11.rolesystem.services.GroupService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/groups")
class GroupController(val groupService: GroupService) {

    @GetMapping
    fun getGroups(): List<GroupDTO> = groupService.getGroups().map { it.toDTO() }

    @GetMapping("/{id}")
    fun getGroup(@PathVariable id: Long): GroupDTO {

        return groupService.getGroup(id).toDTO()
    }

    @PostMapping
    fun createGroups(@RequestBody group: ApplicationGroup) = groupService.createGroup(group).toDTO(true)

    @PutMapping("/{groupId}")
    fun updateGroup(@RequestBody group: ApplicationGroup, @PathVariable groupId: String): GroupDTO {
        val id = groupId.toLongOrNull() ?: throw BadRequestException("Group id was not a number")

        if (group.id != id) {
            throw BadRequestException("ids did not match")
        }

        return groupService.updateGroup(group).toDTO(true)
    }

    @DeleteMapping("/{id}")
    fun deleteGroup(@PathVariable id: Long) = groupService.deleteGroup(id)
}