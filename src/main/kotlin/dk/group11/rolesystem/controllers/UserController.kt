package dk.group11.rolesystem.controllers

import dk.group11.rolesystem.models.ApplicationUser
import dk.group11.rolesystem.security.ISecurityService
import dk.group11.rolesystem.services.GroupService
import dk.group11.rolesystem.services.UserService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
@CrossOrigin
class UserController(private val userService: UserService,
                     val securityService: ISecurityService,
                     val bCryptPasswordEncoder: BCryptPasswordEncoder,
                     val groupService: GroupService) {

    data class Password(val oldPassword: String = "", val newPassword: String = "")

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
    fun createUser(@RequestBody user: ApplicationUser): UserDTO {
        return userService.createUser(user).toDTO()
    }

    @PutMapping
    fun updateUser(@RequestBody user: ApplicationUser): UserDTO {
        return userService.updateUser(user).toDTO()
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long) {
        userService.deleteUser(id)
    }

    @PostMapping("/changepassword")
    fun changePassword(@RequestBody body: Password) {
        userService.changePassword(body.oldPassword, body.newPassword)
    }

    @PutMapping("/{id}/groups")
    fun updateUserGroups(@PathVariable id: Long, @RequestParam(name = "groups") groupIds: String) {
        val ids = groupIds.split(delimiters = ",").mapNotNull { s -> s.toLongOrNull() }
        val user = userService.getUser(id)
        val groups = groupService.getGroups(ids)
        user.groups = groups.toMutableList()
        userService.updateUser(user)
    }

}
