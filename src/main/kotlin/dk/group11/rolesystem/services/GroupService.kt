package dk.group11.rolesystem.services

import dk.group11.rolesystem.auditClient.AuditClient
import dk.group11.rolesystem.auditClient.UserWithGroup
import dk.group11.rolesystem.auditClient.toAuditEntry
import dk.group11.rolesystem.models.ApplicationGroup
import dk.group11.rolesystem.repositories.GroupRepository
import dk.group11.rolesystem.repositories.UserRepository
import dk.group11.rolesystem.security.ISecurityService
import org.springframework.stereotype.Service


@Service
class GroupService(private val groupRepository: GroupRepository,
                   private val userRepository: UserRepository,
                   private val auditClient: AuditClient,
                   private val security: ISecurityService) {

    fun getGroups(): List<ApplicationGroup> = groupRepository.findAll().toList()

    fun getGroup(id: Long): ApplicationGroup = groupRepository.findOne(id)

    fun getGroupsByMemberId(id: Long): List<ApplicationGroup> = groupRepository.findByMembersId(id)

    fun createGroup(group: ApplicationGroup): ApplicationGroup = groupRepository.save(group)

    fun updateGroup(group: ApplicationGroup): ApplicationGroup = groupRepository.save(group)

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