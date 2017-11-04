package dk.group11.rolesystem.services

import dk.group11.rolesystem.models.ApplicationGroup
import dk.group11.rolesystem.repositories.GroupRepository
import org.springframework.stereotype.Service

@Service
class GroupService(private val groupRepository: GroupRepository,
                   private val userService: UserService) {

    fun getGroups(): List<ApplicationGroup> = groupRepository.findAll().toList()

    fun getGroup(id: Long): ApplicationGroup = groupRepository.findOne(id)

    fun getGroupsByMemberId(id: Long): List<ApplicationGroup> = groupRepository.findByMembersId(id)

    fun createGroup(group: ApplicationGroup): ApplicationGroup = groupRepository.save(group)

    fun updateGroup(group: ApplicationGroup): ApplicationGroup = groupRepository.save(group)

    fun deleteGroup(id: Long) = groupRepository.delete(id)

    fun addGroupUser(groupId: Long, userId: Long) {
        val group = groupRepository.findOne(groupId)
        val user = userService.getUser(userId)
        group.members.add(user)
        groupRepository.save(group)
    }
}