package dk.group11.rolesystem.repositories

import dk.group11.rolesystem.model.Group
import org.springframework.data.repository.CrudRepository
import java.util.*

interface GroupRepository : CrudRepository<Group, UUID>