package dk.group11.rolesystem.controllers

import dk.group11.rolesystem.models.AccessKey
import dk.group11.rolesystem.models.ApplicationGroup
import dk.group11.rolesystem.models.ApplicationUser
import dk.group11.rolesystem.models.Role

fun ApplicationUser.toDTO(recursive: Boolean = true): UserDTO {
    val roles = if (recursive) roles.map { it.toDTO(false) }
    else emptyList()

    val groups = if (recursive) groups.map { it.toDTO(false) }
    else emptyList()

    return UserDTO(
            id = id,
            name = name,
            password = password,
            username = username,
            roles = roles,
            groups = groups
    )
}

fun ApplicationGroup.toDTO(recursive: Boolean = true): GroupDTO {
    val members = if (recursive) members.map { it.toDTO(false) }
    else emptyList()

    val roles = if (recursive) roles.map { it.toDTO(false) }
    else emptyList()

    val groupsIn = if (recursive) groupsIn.map { it.toDTO(false) } else emptyList()
    val inGroups = if (recursive) inGroups.map { it.toDTO(false) } else emptyList()

    return GroupDTO(
            id = id,
            title = title,
            description = description,
            members = members,
            roles = roles,
            groupsIn = groupsIn,
            inGroups = inGroups
    )
}

fun Role.toDTO(recursive: Boolean = true): RoleDTO {
    val users = if (recursive) users.map { it.toDTO(false) }
    else emptyList()

    val groups = if (recursive) groups.map { it.toDTO(false) }
    else emptyList()

    return RoleDTO(
            id = id,
            description = description,
            title = title,
            users = users,
            groups = groups
    )
}

fun AccessKey.toDTO(): AccessKeyDTO {

    return AccessKeyDTO(
            id = id,
            userId = user.id,
            token = token
    )

}

data class UserDTO(val id: Long = 0,
                   val name: String = "",
                   val password: String = "",
                   val username: String = "",
                   val roles: List<RoleDTO> = emptyList(),
                   val groups: List<GroupDTO> = emptyList())

data class GroupDTO(val id: Long,
                    val title: String,
                    val description: String,
                    val members: List<UserDTO> = emptyList(),
                    val roles: List<RoleDTO> = emptyList(),
                    val groupsIn: List<GroupDTO> = emptyList(),
                    val inGroups: List<GroupDTO> = emptyList()
)

data class RoleDTO(val id: String,
                   val title: String,
                   val description: String,
                   val users: List<UserDTO> = emptyList(),
                   val groups: List<GroupDTO> = emptyList())

data class AccessKeyDTO(
        val id: Long,
        val userId: Long,
        val token: String
)