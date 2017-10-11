package dk.group11.rolesystem.services

import dk.group11.rolesystem.model.ApplicationUser
import dk.group11.rolesystem.model.LoginUser
import dk.group11.rolesystem.repositories.UserRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*


@Service
class UserService(private val userRepository: UserRepository, val bCryptPasswordEncoder: BCryptPasswordEncoder) : UserDetailsService {


    init {
        if (!userRepository.existsByUsername("sofie12")) {
            val user: ApplicationUser = ApplicationUser(name = "Sofie", username = "sofie12", password = "1234")
            user.password = bCryptPasswordEncoder.encode(user.password)
            userRepository.save(user)
        }

    }

    override fun loadUserByUsername(username: String): UserDetails? {
        val user: ApplicationUser = userRepository.findByUsername(username)
        return LoginUser(username = user.username, password = user.password, authority = emptyList<GrantedAuthority>().toMutableList())
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