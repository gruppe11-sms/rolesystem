package dk.group11.rolesystem.models

import javax.persistence.*

@Entity
data class ApplicationGroup(@Id @GeneratedValue(strategy = GenerationType.AUTO)
                            var id: Long = 0,
                            var title: String = "",
                            var description: String = "",
                            @OneToMany(cascade = arrayOf(CascadeType.ALL))
                            var members: MutableList<ApplicationUser> = mutableListOf(),
                            @OneToMany(cascade = arrayOf(CascadeType.ALL))
                            var roles: MutableList<Role> = mutableListOf())
