package dk.group11.rolesystem.model

import java.util.*
import javax.persistence.*

@Entity
data class ApplicationUser(@Id
                           @GeneratedValue(strategy = GenerationType.AUTO)
                           var id: UUID = UUID.randomUUID(), // TODO find a better sulution
                           var name: String = "",
                           var password: String = "",
                           var username: String = "",
                           @OneToMany
                           var roles: List<Role> = emptyList())

