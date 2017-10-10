package dk.group11.rolesystem.users

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(val userRepository: UserRepository, val bCryptPasswordEncoder: BCryptPasswordEncoder) {

    fun getUser(id: UUID): User {
        return userRepository.findOne(id)
    }

    fun createUser(user: User) {
        user.password = bCryptPasswordEncoder.encode(user.password)
        userRepository.save(user)
    }

    fun getUsers(): List<User> {
        return userRepository.findAll().toList()
    }

    fun updateUser(user: User) {
        userRepository.save(user)
    }

    fun deleteUser(id: UUID) {
        userRepository.delete(id)
    }
}