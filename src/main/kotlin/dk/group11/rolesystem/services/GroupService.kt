package dk.group11.rolesystem.services

import dk.group11.rolesystem.model.Group
import dk.group11.rolesystem.repositories.GroupRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class GroupService(val groupRepository: GroupRepository) {

    fun getGroups(): List<Group> {
        return groupRepository.findAll().toList()
    }

    fun getGroup(id: UUID): Group {
        return groupRepository.findOne(id)
    }

    fun createGroups(group: Group) {
        groupRepository.save(group)
    }

    fun deleteGroup(id: UUID) {
        groupRepository.delete(id)
    }

    fun updateGroup(group: Group) {
        groupRepository.save(group)
    }
}