package dk.group11.rolesystem.model

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class ApplicationUser(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: UUID = UUID.randomUUID(), // TODO find a better sulution
        var name: String = "",
        var password: String = "",
        var username: String = "",
        var roles: List<Role> = emptyList()
)

