package dk.group11.rolesystem.controllers

import dk.group11.rolesystem.exceptions.BadRequestException
import dk.group11.rolesystem.helpers.toIDList
import dk.group11.rolesystem.models.ApplicationUser
import dk.group11.rolesystem.security.ISecurityService
import dk.group11.rolesystem.services.UserService
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService, private val securityService: ISecurityService,
                     val bCryptPasswordEncoder: BCryptPasswordEncoder) {

    data class Password(val oldPassword: String = "", val newPassword: String = "")

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: Long): UserDTO = userService.getUser(id).toDTO()

    @GetMapping
    fun getUsers(): List<UserDTO> = userService.getAllUsers().map { it.toDTO() }

    @GetMapping("/me")
    fun getMe(): UserDTO = userService.getUser(securityService.getId()).toDTO()

    @GetMapping("/names")
    fun getUserNames(@RequestParam(name = "userIds") userIds: String): Map<Long, String> =
            userService.getUsers(userIds.toIDList()).map { it.id to it.name }.toMap()

    @PostMapping
    fun createUser(@RequestBody user: ApplicationUser) = userService.createUser(user)

    @PutMapping
    fun updateUser(@RequestBody user: ApplicationUser) = userService.updateUser(user)

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long) = userService.deleteUser(id)

    @PostMapping("/changepassword")
    fun changePassword(@RequestBody body: Password) {
        val userId = securityService.getId()
        val user = userService.getUser(userId)
        if (BCrypt.checkpw(body.oldPassword, user.password)) {
            user.password = bCryptPasswordEncoder.encode(body.newPassword)
            userService.updateUser(user)
        } else {
            throw BadRequestException("Wrong Password")

        }

    }
}