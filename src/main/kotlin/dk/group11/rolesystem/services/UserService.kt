package dk.group11.rolesystem.services

import dk.group11.rolesystem.models.ApplicationUser
import dk.group11.rolesystem.models.LoginUser
import dk.group11.rolesystem.repositories.UserRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*


@Service
class UserService(private val userRepository: UserRepository,
                  private val bCryptPasswordEncoder: BCryptPasswordEncoder) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails? {
        val user: ApplicationUser? = userRepository.findByUsername(username)
        if (user != null) {
            return LoginUser(
                    username = user.username,
                    password = user.password,
                    authority = emptyList<GrantedAuthority>().toMutableList()
            )
        } else {
            throw UsernameNotFoundException(username)
        }
    }


    fun getUser(id: UUID): ApplicationUser {
        return userRepository.findOne(id)
    }

    fun createUser(user: ApplicationUser): ApplicationUser {
        user.password = bCryptPasswordEncoder.encode(user.password)
        userRepository.save(user)
        return user
    }

    fun getUsers(ids: List<UUID>): List<ApplicationUser> {
        return userRepository.findAll(ids.toMutableList()).toList()
    }

    fun getAllUsers(): List<ApplicationUser> {
        return userRepository.findAll().toList()
    }

    fun updateUser(user: ApplicationUser) {
        userRepository.save(user)
    }

    fun deleteUser(id: UUID) {
        userRepository.delete(id)
    }

}