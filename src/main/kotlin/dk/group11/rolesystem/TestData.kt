package dk.group11.rolesystem

import dk.group11.rolesystem.models.ApplicationGroup
import dk.group11.rolesystem.models.ApplicationUser
import dk.group11.rolesystem.models.Role
import dk.group11.rolesystem.repositories.GroupRepository
import dk.group11.rolesystem.repositories.RoleRepository
import dk.group11.rolesystem.repositories.UserRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class TestData(private val userRepository: UserRepository,
               private val roleRepository: RoleRepository,
               private val groupRepository: GroupRepository,
               private val bCryptPasswordEncoder: BCryptPasswordEncoder) : ApplicationRunner {

    override fun run(args: ApplicationArguments) {
        if (!roleRepository.existsByTitle("PartyManager") &&
                !userRepository.existsByUsername("sofie12") &&
                !groupRepository.existsByTitle("3.B")) {

            val role = roleRepository.save(
                    Role(
                            title = "PartyManager",
                            description = "Manages parties"
                    )
            )

            val user = userRepository.save(
                    ApplicationUser(
                            name = "Sofie",
                            username = "sofie12",
                            password = bCryptPasswordEncoder.encode("1234"),
                            roles = mutableListOf(role)
                    )
            )
            groupRepository.save(
                    ApplicationGroup(
                            title = "3.B",
                            description = "Member of class 3.B",
                            members = mutableListOf(user)
                    )
            )
        }
    }
}