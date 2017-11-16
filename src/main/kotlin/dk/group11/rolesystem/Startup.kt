package dk.group11.rolesystem

import dk.group11.rolesystem.models.ApplicationUser
import dk.group11.rolesystem.repositories.UserRepository
import dk.group11.rolesystem.security.RoleCreatorRole
import dk.group11.rolesystem.services.RoleService
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.NoSuchFileException
import java.nio.file.Paths
import java.util.*
import javax.transaction.Transactional

private val SYS_ADMIN_REF: Long = -10
private val SYSTEM_REF: Long = -11

// This code is run on startup, and ensures a consistent first install

@Component
class Startup(
        private val roleService: RoleService,
        private val userRepository: UserRepository,
        private val bCryptPasswordEncoder: BCryptPasswordEncoder
) : ApplicationRunner {
    @Transactional
    override fun run(args: ApplicationArguments?) {

        // Create necessary roles
        val roleCreatorRole = roleService.addRole(
                RoleCreatorRole,
                "Role creator",
                "Can create new roles"
        )

        val systemPasswordPath = Paths.get("./system_password")
        val systemPassword = try {
            Files.readAllLines(systemPasswordPath).first()
        } catch (e: NoSuchFileException) {
            val password = UUID.randomUUID().toString()
            Files.write(systemPasswordPath, listOf(password))
            password
        }


        // If there are no users, then create the sysadmin,
        // otherwise load it from the database
        val sysadmin = userRepository.findBySystemRef(SYS_ADMIN_REF) ?: userRepository.save(ApplicationUser(
                systemRef = SYS_ADMIN_REF,
                name = "SysAdmin",
                username = "sysadmin",
                password = bCryptPasswordEncoder.encode("1234")
        ))
        val systemUser = userRepository.findBySystemRef(SYSTEM_REF) ?: userRepository.save(ApplicationUser(
                systemRef = SYSTEM_REF,
                name = "SYSTEM",
                username = "system",
                password = bCryptPasswordEncoder.encode(systemPassword)
        ))

        sysadmin.roles.add(roleCreatorRole)
        userRepository.save(sysadmin)

        systemUser.roles.add(roleCreatorRole)
        userRepository.save(systemUser)
    }
}