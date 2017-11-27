package dk.group11.rolesystem.models

import javax.persistence.*

@Entity
class Role(
        @Id
        var id: String = "",
        var title: String = "",
        var description: String = "",
        @ManyToMany(cascade = arrayOf(CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.DETACH))
        @JoinTable(name = "application_user_roles",
                joinColumns = arrayOf(JoinColumn(name = "role_id")),
                inverseJoinColumns = arrayOf(JoinColumn(name = "user_id"))
        )
        var users: MutableList<ApplicationUser> = mutableListOf(),

        @ManyToMany(cascade = arrayOf(CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.DETACH))
        @JoinTable(name = "application_group_members",
                joinColumns = arrayOf(JoinColumn(name = "role_id")),
                inverseJoinColumns = arrayOf(JoinColumn(name = "group_id"))
        )
        var groups: MutableList<ApplicationGroup> = mutableListOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Role

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}