package dk.group11.rolesystem.model

import java.util.*
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

data class Role(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: UUID = UUID.randomUUID(),
        var title: String = "",
        var description: String = "",
        var users: List<ApplicationUser> = emptyList())
