package dk.group11.rolesystem.models

import javax.persistence.*

@Entity
class ApplicationGroup(@Id @GeneratedValue(strategy = GenerationType.AUTO)
                            var id: Long = 0,
                       var title: String = "",
                       var description: String = "",
                       @ManyToMany(cascade = arrayOf(CascadeType.ALL))
                            @JoinColumn
                            var members: MutableList<ApplicationUser> = mutableListOf(),
                       @ManyToMany(cascade = arrayOf(CascadeType.ALL))
                            @JoinColumn
                       var roles: MutableList<Role> = mutableListOf()) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ApplicationGroup

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}