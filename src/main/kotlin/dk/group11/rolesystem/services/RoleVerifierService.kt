package dk.group11.rolesystem.services

import dk.group11.rolesystem.repositories.UserRepository
import org.springframework.stereotype.Service

@Service
class RoleVerifierService(private val userRepository: UserRepository) {

    /**
     * Checks if a user has a role
     */
    fun hasRoles(userId: Long, vararg roleKeys: String): Boolean {
        val currentUser = userRepository.findOne(userId)
        return currentUser.roles.map { it.id }.containsAll(roleKeys.toList())
    }
}