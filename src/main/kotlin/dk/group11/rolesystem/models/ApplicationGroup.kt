package dk.group11.rolesystem.models

import javax.persistence.*

@Entity
class ApplicationGroup(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0,
        var title: String = "",
        var description: String = "",
        @ManyToMany(cascade = arrayOf(CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.DETACH))
        @JoinTable(name = "application_group_members",
                joinColumns = arrayOf(JoinColumn(name = "group_id")),
                inverseJoinColumns = arrayOf(JoinColumn(name = "member_id"))
        )
        var members: MutableSet<ApplicationUser> = mutableSetOf(),

        @ManyToMany(cascade = arrayOf(CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.DETACH))
        @JoinTable(name = "application_group_roles",
                joinColumns = arrayOf(JoinColumn(name = "group_id")),
                inverseJoinColumns = arrayOf(JoinColumn(name = "role_id"))
        )
        var roles: MutableSet<Role> = mutableSetOf(),

        @ManyToMany(cascade = arrayOf(CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.DETACH))
        @JoinTable(name = "application_group_in_groups",
                joinColumns = arrayOf(JoinColumn(name = "group_in_id")),
                inverseJoinColumns = arrayOf(JoinColumn(name = "in_group_id"))
        )
        /**
         * The groups this group is in
         */
        var groupsIn: MutableSet<ApplicationGroup> = mutableSetOf(),


        @ManyToMany(cascade = arrayOf(CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.DETACH))
        @JoinTable(name = "application_group_in_groups",
                joinColumns = arrayOf(JoinColumn(name = "in_group_id")),
                inverseJoinColumns = arrayOf(JoinColumn(name = "group_in_id"))
        )
        /**
         * The groups that are part of this group
         */
        var inGroups: MutableSet<ApplicationGroup> = mutableSetOf()
) {


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