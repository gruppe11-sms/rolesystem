package dk.group11.rolesystem.models

import javax.persistence.*

@Entity
class ApplicationUser(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0,
        var name: String = "",
        var password: String = "",
        var username: String = "",
        @ManyToMany(cascade = arrayOf(CascadeType.ALL))
        @JoinColumn
        var roles: MutableSet<Role> = mutableSetOf(),
        @ManyToMany(mappedBy = "members", cascade = arrayOf(CascadeType.ALL))
        var groups: MutableSet<ApplicationGroup> = mutableSetOf(),
        /**
         * Used for some system calculations, that requires a
         * persistent way of referencing a user
         */
        var systemRef: Long = 0
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ApplicationUser

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}