package dk.group11.rolesystem.models

import javax.persistence.*

@Entity
data class ApplicationUser(@Id @GeneratedValue(strategy = GenerationType.AUTO)
                           var id: Long = 0,
                           var name: String = "",
                           var password: String = "",
                           var username: String = "",
                           @ManyToMany(cascade = arrayOf(CascadeType.ALL))
                           var roles: MutableList<Role> = mutableListOf(),
                           @ManyToMany(cascade = arrayOf(CascadeType.ALL))
                           var groups: MutableList<ApplicationGroup> = mutableListOf())