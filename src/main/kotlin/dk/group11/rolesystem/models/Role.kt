package dk.group11.rolesystem.models

import javax.persistence.*

@Entity
data class Role(@Id @GeneratedValue(strategy = GenerationType.AUTO)
                var id: Long = 0,
                var title: String = "",
                var description: String = "",
                @OneToMany(cascade = arrayOf(CascadeType.ALL))
                var users: MutableList<ApplicationUser> = mutableListOf())
