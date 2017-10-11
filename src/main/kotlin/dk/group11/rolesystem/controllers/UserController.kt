package dk.group11.rolesystem.controllers

import dk.group11.rolesystem.model.ApplicationUser
import dk.group11.rolesystem.services.UserService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class UserController(val userService: UserService) {

    @GetMapping("/api/users/{id}")
    fun getUser(@PathVariable id: UUID): ApplicationUser {
        return userService.getUser(id)
    }

    @GetMapping("/api/users")
    fun getUsers(): List<ApplicationUser> {
        return userService.getUsers()
    }

    @PostMapping("/api/users")
    fun createUser(@RequestBody user: ApplicationUser) {
        userService.createUser(user)
    }

    @PutMapping("/api/users")
    fun updateUser(@RequestBody user: ApplicationUser) {
        userService.updateUser(user)
    }

    @DeleteMapping("/api/users/{id}")
    fun deleteUser(@PathVariable id: UUID) {
        userService.deleteUser(id)
    }
}