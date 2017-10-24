package dk.group11.rolesystem.models

import javax.persistence.*

@Entity
data class Role(@Id @GeneratedValue(strategy = GenerationType.AUTO)
                var id: Long = 0,
                var title: String = "",
                var description: String = "",
                @ManyToMany(cascade = arrayOf(CascadeType.ALL))
                var users: MutableList<ApplicationUser> = mutableListOf(),
                @ManyToMany(cascade = arrayOf(CascadeType.ALL))
                var groups: MutableList<ApplicationGroup> = mutableListOf())