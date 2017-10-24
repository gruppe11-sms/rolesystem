package dk.group11.rolesystem.security

import com.fasterxml.jackson.databind.ObjectMapper
import dk.group11.rolesystem.models.ApplicationUser
import dk.group11.rolesystem.models.LoginUser
import dk.group11.rolesystem.services.UserService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class AuthenticationFilter(private val authManager: AuthenticationManager,
                           private val userService: UserService) : UsernamePasswordAuthenticationFilter() {

    data class UserData(val id: Long, val username: String)

    override fun attemptAuthentication(req: HttpServletRequest,
                                       res: HttpServletResponse): Authentication {
        val user: ApplicationUser = ObjectMapper().readValue(req.inputStream, ApplicationUser::class.java)
        return authManager.authenticate(UsernamePasswordAuthenticationToken(user.username, user.password, emptyList()))
    }

    override fun successfulAuthentication(req: HttpServletRequest,
                                          res: HttpServletResponse,
                                          chain: FilterChain,
                                          auth: Authentication) {
        val user = userService.getUserByUsername((auth.principal as LoginUser).username)
        val userData = UserData(username = user.username, id = user.id)
        val token = Jwts.builder()
                .setSubject(ObjectMapper().writeValueAsString(userData))
                .setExpiration(Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET.toByteArray())
                .compact()
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token)
    }
}