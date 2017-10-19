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
        println("Code is ran")

        val user = ApplicationUser(name = "Sofie", username = "sofie12", password = "1234")
        user.password = bCryptPasswordEncoder.encode(user.password)
        val role = Role(title = "PartyManager", description = "Manages parties")
        roleRepository.save(role)
        user.roles.add(role)
        userRepository.save(user)

        val group = ApplicationGroup(title = "3.B", description = "Member of class 3.B")
        group.members.add(user)
        groupRepository.save(group)
    }
}