package dk.group11.rolesystem.clients

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.kittinunf.fuel.Fuel
import dk.group11.rolesystem.security.HEADER_STRING
import dk.group11.rolesystem.security.SecurityService
import org.springframework.beans.BeanUtils
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service

@Service
class AuditClient(val auditConfigProperties: AuditConfigProperties, private val securityService: SecurityService) {
    fun createEntry(action: String, data: Any, authToken: String = securityService.getToken()) {
        Fuel.post(auditConfigProperties.url + "/api/auditentry")
                .header(Pair(HEADER_STRING, authToken))
                .header(Pair("Content-Type", "application/json"))
                .body(getJson(action, data))
                .responseString()
    }
}

private fun getJson(action: String, data: Any): String {
    val requestData = if (BeanUtils.isSimpleValueType(data::class.java)) data.toString()
    else ObjectMapper().writeValueAsString(data)
    val request = AuditRequest(action, requestData)
    return ObjectMapper().writeValueAsString(request)
}


data class AuditRequest(val action: String, val data: Any)

@Configuration
@ConfigurationProperties(prefix = "audit")
data class AuditConfigProperties(var url: String = "")