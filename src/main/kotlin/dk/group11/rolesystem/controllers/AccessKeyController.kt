package dk.group11.rolesystem.controllers

import dk.group11.rolesystem.services.AccessKeyService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController("/api/tokens")
class AccessKeyController(private val accessKeyService: AccessKeyService) {

    @RequestMapping(method = arrayOf(RequestMethod.POST))
    fun createAccessKey(): AccessKeyDTO {

        return accessKeyService.createAccessKey().toDTO()

    }

    @RequestMapping(method = arrayOf(RequestMethod.GET))
    fun validateAccessKey(
            @RequestParam(name = "token") token: String,
            @RequestParam(name = "userId") userId: Long,
            @RequestParam(name = "tokenId") tokenId: Long
    ) {
        accessKeyService.validateAccessKey(tokenId, token, userId)
    }

}