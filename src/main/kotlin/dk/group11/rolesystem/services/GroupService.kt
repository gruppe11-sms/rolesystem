package dk.group11.rolesystem.services

import dk.group11.rolesystem.models.ApplicationGroup
import dk.group11.rolesystem.repositories.GroupRepository
import org.springframework.stereotype.Service

@Service
class GroupService(private val groupRepository: GroupRepository) {

    fun getGroups(): List<ApplicationGroup> {
        return groupRepository.findAll().toList()
    }

    fun getGroup(id: Long): ApplicationGroup {
        return groupRepository.findOne(id)
    }

    fun getGroupsByMemberId(id: Long): List<ApplicationGroup> {
        return groupRepository.findByMembersId(id)
    }

    fun createGroup(group: ApplicationGroup): ApplicationGroup {
        return groupRepository.save(group)
    }

    fun deleteGroup(id: Long) {
        groupRepository.delete(id)
    }

    fun updateGroup(group: ApplicationGroup) {
        groupRepository.save(group)
    }
}