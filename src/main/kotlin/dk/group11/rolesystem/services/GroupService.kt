package dk.group11.rolesystem.services

import dk.group11.rolesystem.clients.AuditClient
import dk.group11.rolesystem.clients.UserWithGroup
import dk.group11.rolesystem.clients.toAuditEntry
import dk.group11.rolesystem.controllers.GroupDTO
import dk.group11.rolesystem.controllers.toDTO
import dk.group11.rolesystem.exceptions.NotFoundException
import dk.group11.rolesystem.models.ApplicationGroup
import dk.group11.rolesystem.repositories.GroupRepository
import dk.group11.rolesystem.repositories.RoleRepository
import dk.group11.rolesystem.repositories.UserRepository
import dk.group11.rolesystem.security.GROUP_MAINTAINER_ROLE
import dk.group11.rolesystem.security.ISecurityService
import org.springframework.stereotype.Service
import javax.transaction.Transactional


private data class updatedGroupAuditEntry(val before: GroupDTO, val after: GroupDTO)

@Service
class GroupService(private val groupRepository: GroupRepository,
                   private val userRepository: UserRepository,
                   private val roleRepository: RoleRepository,
                   private val auditClient: AuditClient,
                   private val security: ISecurityService) {

    fun getGroups(): List<ApplicationGroup> = groupRepository.findAll().toList()

    fun getGroup(id: Long): ApplicationGroup {
        security.requireRoles(GROUP_MAINTAINER_ROLE)

        auditClient.createEntry("[RoleSystem] Update Group", id)
        val group = groupRepository.findOne(id) ?: throw NotFoundException("Group not find")

        return group
    }

    fun getGroupsByMemberId(id: Long): List<ApplicationGroup> = groupRepository.findByMembersId(id)

    fun createGroup(group: ApplicationGroup): ApplicationGroup {
        security.requireRoles(GROUP_MAINTAINER_ROLE)

        val createdGroup = groupRepository.save(group)
        auditClient.createEntry("[RoleSystem] Create Group", group.toDTO(false))

        return createdGroup
    }

    @Transactional
    fun updateGroup(group: ApplicationGroup): ApplicationGroup {
        security.requireRoles(GROUP_MAINTAINER_ROLE)

        val currentGroup = groupRepository.findOne(group.id) ?: throw NotFoundException("Group not found")

        auditClient.createEntry("[RoleSystem] Update group", updatedGroupAuditEntry(currentGroup.toDTO(true), group.toDTO(true)))

        currentGroup.title = group.title
        currentGroup.description = group.description

        currentGroup.roles.clear()
        currentGroup.roles.addAll(
                group.roles.map { roleRepository.findOne(it.id) }
        )

        currentGroup.members.clear()
        currentGroup.members.addAll(
                group.members.map { userRepository.findOne(it.id) }
        )

        currentGroup.groupsIn.clear()
        currentGroup.groupsIn.addAll(
                group.groupsIn.map { groupRepository.findOne(it.id) }
        )


        currentGroup.inGroups.clear()
        currentGroup.inGroups.addAll(
                group.inGroups.map { groupRepository.findOne(it.id) }
        )

        return currentGroup

    }

    fun deleteGroup(groupId: Long) {
        val group = groupRepository.findOne(groupId)
        val user = userRepository.findOne(security.getId())
        groupRepository.delete(groupId)
        auditClient.createEntry("[RoleSystem] Group deleted",
                UserWithGroup(user = user.toAuditEntry(), group = group.toAuditEntry()), security.getToken()
        )
    }

    fun addGroupUser(groupId: Long, userId: Long) {
        val group = groupRepository.findOne(groupId)
        val user = userRepository.findOne(security.getId())
        group.members.add(user)
        groupRepository.save(group)
        auditClient.createEntry("[RoleSystem] User added to group",
                UserWithGroup(user = user.toAuditEntry(), group = group.toAuditEntry()), security.getToken()
        )
    }

    fun removeGroupUser(groupId: Long, userId: Long) {
        val group = groupRepository.findOne(groupId)
        val user = userRepository.findOne(security.getId())
        group.members.remove(user)
        groupRepository.save(group)
        auditClient.createEntry("[RoleSystem] User removed from group",
                UserWithGroup(user = user.toAuditEntry(), group = group.toAuditEntry()), security.getToken()
        )
    }
}