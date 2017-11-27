package dk.group11.rolesystem.services

import dk.group11.rolesystem.models.ApplicationGroup
import dk.group11.rolesystem.models.ApplicationUser
import dk.group11.rolesystem.models.Role
import dk.group11.rolesystem.repositories.UserRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class RoleVerifierService(private val userRepository: UserRepository) {

    /**
     * Checks if a user has a role
     */
    fun hasRoles(userId: Long, vararg roleKeys: String): Boolean {
        val currentUser = userRepository.findOne(userId)

        val roles = getRolesForUser(currentUser)

        return roles.map { it.id }.containsAll(roleKeys.toList())
    }

    fun getRolesForUser(user: ApplicationUser): Set<Role> {

        val roles = mutableSetOf(*user.roles.toTypedArray())

        val buffer = LinkedList<ApplicationGroup>()
        buffer.addAll(user.groups.toList())

        while(buffer.isNotEmpty()) {
            val group = buffer.removeFirst()
            buffer.addAll(group.groupsIn.toList())
            roles.addAll(group.roles.toList())
        }

        return roles
    }
}