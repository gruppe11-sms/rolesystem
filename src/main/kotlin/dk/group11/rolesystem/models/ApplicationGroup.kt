package dk.group11.rolesystem.models

import javax.persistence.*

@Entity
class ApplicationGroup(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0,
        var title: String = "",
        var description: String = "",
        @ManyToMany(cascade = arrayOf(CascadeType.MERGE))
        @JoinColumn
        var members: MutableSet<ApplicationUser> = mutableSetOf(),
        @ManyToMany(cascade = arrayOf(CascadeType.MERGE))
        @JoinColumn
        var roles: MutableSet<Role> = mutableSetOf(),

        @ManyToMany(mappedBy = "inGroups", cascade = arrayOf(CascadeType.MERGE, CascadeType.PERSIST))
        /**
         * The groups this group is in
         */
        var groupsIn: MutableSet<ApplicationGroup> = mutableSetOf(),


        @ManyToMany(cascade = arrayOf(CascadeType.MERGE, CascadeType.PERSIST))
        @JoinColumn
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