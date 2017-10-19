package dk.group11.rolesystem.models

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Role(@Id @GeneratedValue(strategy = GenerationType.AUTO)
                var id: UUID = UUID.randomUUID(),
                var title: String = "",
                var description: String = "")
