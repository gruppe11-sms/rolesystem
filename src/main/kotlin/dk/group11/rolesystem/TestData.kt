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
import javax.transaction.Transactional

@Component
class TestData(private val userRepository: UserRepository,
               private val roleRepository: RoleRepository,
               private val groupRepository: GroupRepository,
               private val bCryptPasswordEncoder: BCryptPasswordEncoder) : ApplicationRunner {
    @Transactional
    override fun run(args: ApplicationArguments) {
        if (!roleRepository.existsByTitle("PartyManager") &&
                !userRepository.existsByUsername("sofie12") &&
                !userRepository.existsByUsername("emikr15") &&
                !groupRepository.existsByTitle("3.B")) {


            roleRepository.save(listOf(
                    Role(
                            title = "PartyManager",
                            description = "Manages parties"
                    ),
                    Role(
                            title = "Teacher",
                            description = "Teaches a class"
                    )
            ))
            userRepository.save(listOf(
                    ApplicationUser(
                            name = "Sofie",
                            username = "sofie12",
                            password = bCryptPasswordEncoder.encode("1234")
                    ),
                    ApplicationUser(
                            name = "Emil",
                            username = "emikr15",
                            password = bCryptPasswordEncoder.encode("1234")
                    )
            ))

            groupRepository.save(
                    ApplicationGroup(
                            title = "3.B",
                            description = "Member of class 3.B"
                    )

            )
            val group = groupRepository.findByTitle("3.B")
            val user = userRepository.findByUsername("sofie12")
            val role = roleRepository.findByTitle("PartyManager")

            if (role != null && user != null) {
                println("Adding user")
                user.roles.add(role)
                userRepository.save(user)
                roleRepository.save(role)
            }

            if (group != null && user != null) {
                println("adding user to group")
                group.members.add(user)
                groupRepository.save(group)
                userRepository.save(user)
            }
        }
    }
}