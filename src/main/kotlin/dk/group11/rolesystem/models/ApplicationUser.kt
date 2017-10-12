package dk.group11.rolesystem.models

import java.util.*
import javax.persistence.*

@Entity
data class ApplicationUser(@Id
                           @GeneratedValue(strategy = GenerationType.AUTO)
                           var id: UUID = UUID.randomUUID(),
                           var name: String = "",
                           var password: String = "",
                           var username: String = "",
                           @OneToMany
                           var roles: List<Role> = emptyList())

