package dk.group11.rolesystem.services

import dk.group11.rolesystem.model.ApplicationGroup
import dk.group11.rolesystem.repositories.GroupRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class GroupService(val groupRepository: GroupRepository) {

    fun getGroups(): List<ApplicationGroup> {
        return groupRepository.findAll().toList()
    }

    fun getGroup(id: UUID): ApplicationGroup {
        return groupRepository.findOne(id)
    }

    fun createGroups(group: ApplicationGroup) {
        groupRepository.save(group)
    }

    fun deleteGroup(id: UUID) {
        groupRepository.delete(id)
    }

    fun updateGroup(group: ApplicationGroup) {
        groupRepository.save(group)
    }
}