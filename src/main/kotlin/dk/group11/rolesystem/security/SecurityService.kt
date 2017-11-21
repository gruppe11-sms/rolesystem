package dk.group11.rolesystem.security

import dk.group11.rolesystem.services.RoleVerifierService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest

class ForbiddenException(message: String) : RuntimeException(message)

@Service
class SecurityService(private val request: HttpServletRequest, private val roleVerifierService: RoleVerifierService) : ISecurityService {


    override fun getId(): Long {
        return principal.id
    }

    override fun getToken(): String {
        return this.request.getHeader(HEADER_STRING)
    }

    val principal: AuthenticationFilter.UserData
        get() = SecurityContextHolder.getContext().authentication.principal as AuthenticationFilter.UserData

    override fun requireRoles(vararg roleKeys: String) {
        if (!roleVerifierService.hasRoles(getId(), *roleKeys)) {
            throw ForbiddenException("Required roles: ${roleKeys.joinToString(", ")}")
        }
    }
}