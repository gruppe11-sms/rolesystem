package dk.group11.rolesystem.services

import dk.group11.rolesystem.models.AccessKey
import dk.group11.rolesystem.repositories.AccessKeyRepository
import dk.group11.rolesystem.repositories.UserRepository
import dk.group11.rolesystem.security.ForbiddenException
import dk.group11.rolesystem.security.SecurityService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class AccessKeyService(
        private val accessKeyRepository: AccessKeyRepository,
        private val securityService: SecurityService,
        private val bCryptPasswordEncoder: BCryptPasswordEncoder,
        private val userRepository: UserRepository
) {

    fun createAccessKey(userId: Long = securityService.getId()): AccessKey {
        val token = UUID.randomUUID().toString()
        val hashedToken = bCryptPasswordEncoder.encode(token)

        val accessKey = AccessKey(
                token = hashedToken,
                user = userRepository.findOne(userId)
        )

        accessKeyRepository.save(accessKey)

        return AccessKey(
                token = token,
                user = accessKey.user,
                id = accessKey.id
        )

    }

    fun validateAccessKey(tokenId: Long, token: String, userId: Long) {

        val accessKey = accessKeyRepository.findOne(tokenId) ?: throw ForbiddenException("Invalid token")
        if (accessKey.user.id != userId) {
            throw ForbiddenException("Invalid token")
        }

        if (!bCryptPasswordEncoder.matches(token, accessKey.token)) {
            throw ForbiddenException("Invalid token")
        }


    }

}