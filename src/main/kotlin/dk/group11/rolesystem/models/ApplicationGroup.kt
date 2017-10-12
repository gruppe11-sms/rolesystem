package dk.group11.rolesystem.models

import java.util.*
import javax.persistence.*

@Entity
data class ApplicationGroup(@Id
                            @GeneratedValue(strategy = GenerationType.AUTO)
                            var id: UUID = UUID.randomUUID(),
                            var title: String = "",
                            var description: String = "",
                            @OneToMany
                            var members: MutableList<ApplicationUser> = emptyList<ApplicationUser>().toMutableList(),
                            @OneToMany
                            var roles: MutableList<Role> = emptyList<Role>().toMutableList())
