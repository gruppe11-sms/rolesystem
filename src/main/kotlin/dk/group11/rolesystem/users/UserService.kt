package dk.group11.rolesystem.users

import dk.group11.rolesystem.model.ApplicationUser
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*


@Service
class UserService(val userRepository: UserRepository, val bCryptPasswordEncoder: BCryptPasswordEncoder) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        return userRepository.findByUsername(username)
    }


    fun getUser(id: UUID): ApplicationUser {
        return userRepository.findOne(id)
    }

    fun createUser(user: ApplicationUser) {
        user.password = bCryptPasswordEncoder.encode(user.password)
        userRepository.save(user)
    }

    fun getUsers(): List<ApplicationUser> {
        return userRepository.findAll().toList()
    }

    fun updateUser(user: ApplicationUser) {
        userRepository.save(user)
    }

    fun deleteUser(id: UUID) {
        userRepository.delete(id)
    }
}