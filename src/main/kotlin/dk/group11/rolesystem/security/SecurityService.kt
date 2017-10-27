package dk.group11.rolesystem.security

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest

@Service
class SecurityService(private val request: HttpServletRequest) : ISecurityService {


    override fun getId(): Long {
        return principal.id
    }

    override fun getToken(): String {
        return this.request.getHeader(HEADER_STRING)
    }

    val principal: AuthenticationFilter.UserData
        get() = SecurityContextHolder.getContext().authentication.principal as AuthenticationFilter.UserData
}