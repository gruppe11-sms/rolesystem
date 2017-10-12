package dk.group11.rolesystem.controllers

import dk.group11.rolesystem.models.ApplicationUser
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController {

    @PostMapping("/api/auth")
    fun signUp(@RequestBody user: ApplicationUser) {

    }

    @PostMapping("/api/auth/login")
    fun login() {

    }
}