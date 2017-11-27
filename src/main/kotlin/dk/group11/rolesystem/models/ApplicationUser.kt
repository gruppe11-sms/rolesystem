package dk.group11.rolesystem.models

import javax.persistence.*

@Entity
data class ApplicationUser(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0,
        var name: String = "",
        var password: String = "",
        @Column(unique = true)
        var username: String = "",
        @ManyToMany(cascade = arrayOf(CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.DETACH))
        @JoinTable(name = "application_user_roles",
                joinColumns = arrayOf(JoinColumn(name = "user_id")),
                inverseJoinColumns = arrayOf(JoinColumn(name = "role_id"))
        )
        var roles: MutableSet<Role> = mutableSetOf(),


        @ManyToMany(cascade = arrayOf(CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.DETACH))
        @JoinTable(name = "application_group_members",
                joinColumns = arrayOf(JoinColumn(name = "member_id")),
                inverseJoinColumns = arrayOf(JoinColumn(name = "group_id"))
        )
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