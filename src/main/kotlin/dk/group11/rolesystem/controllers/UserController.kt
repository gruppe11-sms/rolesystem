package dk.group11.rolesystem.controllers

import dk.group11.rolesystem.models.ApplicationUser
import dk.group11.rolesystem.security.ISecurityService
import dk.group11.rolesystem.services.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService,
                     val securityService: ISecurityService) {

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: Long): UserDTO {
        return userService.getUser(id).toDTO()
    }

    @GetMapping
    fun getUsers(): List<UserDTO> {
        return userService.getAllUsers().map { it.toDTO() }
    }

    @GetMapping("/me")
    fun getMe(): UserDTO {
        return userService.getUser(securityService.getId()).toDTO()
    }

    @GetMapping("/names")
    fun getUserNames(@RequestParam(name = "userIds") userIds: String): Map<Long, String> {
        val ids = userIds.split(delimiters = ",").mapNotNull { s -> s.toLongOrNull() }
        return userService.getUsers(ids).map { it.id to it.name }.toMap()
    }

    @PostMapping
    fun createUser(@RequestBody user: ApplicationUser): ApplicationUser {
        return userService.createUser(user)
    }

    @PutMapping
    fun updateUser(@RequestBody user: ApplicationUser) {
        userService.updateUser(user)
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long) {
        userService.deleteUser(id)
    }
}