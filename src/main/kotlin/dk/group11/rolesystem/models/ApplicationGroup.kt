package dk.group11.rolesystem.models

import javax.persistence.*

@Entity
data class ApplicationGroup(@Id @GeneratedValue(strategy = GenerationType.AUTO)
                            var id: Long = 0,
                            var title: String = "",
                            var description: String = "",
                            @ManyToMany(cascade = arrayOf(CascadeType.ALL))
                            @JoinColumn
                            var members: MutableList<ApplicationUser> = mutableListOf(),
                            @ManyToMany(cascade = arrayOf(CascadeType.ALL))
                            @JoinColumn
                            var roles: MutableList<Role> = mutableListOf())