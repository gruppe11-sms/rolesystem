package dk.group11.rolesystem.models

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToMany

@Entity
class Role(
        @Id
        var key: String = "",
        var title: String = "",
        var description: String = "",
        @ManyToMany(mappedBy = "roles", cascade = arrayOf(CascadeType.ALL))
        var users: MutableList<ApplicationUser> = mutableListOf(),
        @ManyToMany(mappedBy = "roles", cascade = arrayOf(CascadeType.ALL))
        var groups: MutableList<ApplicationGroup> = mutableListOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Role

        if (key != other.key) return false

        return true
    }

    override fun hashCode(): Int {
        return key.hashCode()
    }
}