package dk.group11.rolesystem.repositories

import dk.group11.rolesystem.models.AccessKey
import org.springframework.data.repository.CrudRepository

interface AccessKeyRepository : CrudRepository<AccessKey, Long>