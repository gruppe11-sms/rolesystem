package dk.group11.rolesystem.users

import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class UserController(val userService: UserService) {

    @GetMapping("/api/users/{id}")
    fun getUser(@PathVariable id: UUID): User {
        return userService.getUser(id)
    }

    @GetMapping("/api/users")
    fun getUsers(): List<User> {
        return userService.getUsers()
    }

    @PostMapping("/api/users")
    fun createUser(@RequestBody user: User) {
        userService.createUser(user)
    }

    @PutMapping("/api/users")
    fun updateUser(@RequestBody user: User) {
        userService.updateUser(user)
    }

    @DeleteMapping("/api/users/{id}")
    fun deleteUser(@PathVariable id: UUID) {
        userService.deleteUser(id)
    }
}