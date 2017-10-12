package dk.group11.rolesystem.security

import com.fasterxml.jackson.databind.ObjectMapper
import dk.group11.rolesystem.models.ApplicationUser
import dk.group11.rolesystem.models.LoginUser
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
import kotlin.collections.ArrayList


class AuthenticationFilter(val authenticationManager1: AuthenticationManager) : UsernamePasswordAuthenticationFilter() {

    override fun attemptAuthentication(req: HttpServletRequest, res: HttpServletResponse): Authentication {
        val inpStr = req.inputStream
        val user: ApplicationUser = ObjectMapper().readValue(inpStr, ApplicationUser::class.java)
        val username = user.username
        val password = user.password
        return authenticationManager1.authenticate(UsernamePasswordAuthenticationToken(username, password, ArrayList()))
    }

    override fun successfulAuthentication(req: HttpServletRequest, res: HttpServletResponse, chain: FilterChain, auth: Authentication) {
        val token = Jwts.builder()
                .setSubject((auth.principal as LoginUser).username)
                .setExpiration(Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET.toByteArray())
                .compact()
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token)
    }
}