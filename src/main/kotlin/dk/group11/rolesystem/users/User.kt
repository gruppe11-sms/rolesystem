package dk.group11.rolesystem.users

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "\"User\"")
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: UUID = UUID.randomUUID(), // TODO find a better sulution
        var name: String = "",
        var password: String = "",
        var username: String = ""
)