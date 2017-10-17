package dk.group11.rolesystem.services

import dk.group11.rolesystem.repositories.UserRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
@DataJpaTest
internal class UserServiceTest(val userRepository: UserRepository) {

    lateinit var userService: UserService

    @Autowired
    lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        userService = UserService(userRepository)
    }

    @Test
    fun loadUserByUsername() {
    }

    @Test ,
    fun getUser() {
    }

    @Test
    fun createUser() {
    }

    @Test
    fun getUsers() {
    }

    @Test
    fun getAllUsers() {
    }

    @Test
    fun updateUser() {
    }

    @Test
    fun deleteUser() {
    }

    @Test
    fun getBCryptPasswordEncoder() {
    }

}