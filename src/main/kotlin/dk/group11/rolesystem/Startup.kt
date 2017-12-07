package dk.group11.rolesystem

import dk.group11.rolesystem.models.ApplicationUser
import dk.group11.rolesystem.repositories.UserRepository
import dk.group11.rolesystem.security.COURSE_CREATOR_ROLE
import dk.group11.rolesystem.security.COURSE_MANAGEMENT_ROLE
import dk.group11.rolesystem.security.GROUP_MAINTAINER_ROLE
import dk.group11.rolesystem.security.ROLE_CREATOR_ROLE
import dk.group11.rolesystem.services.ISecretService
import dk.group11.rolesystem.services.RoleService
import org.springframework.beans.BeansException
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.jdbc.datasource.AbstractDataSource
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.NoSuchFileException
import java.nio.file.Paths
import java.sql.Connection
import java.sql.SQLException
import java.util.*
import javax.sql.DataSource
import javax.transaction.Transactional

private const val SYS_ADMIN_REF: Long = -10
private const val SYSTEM_REF: Long = -11

// This code is run on startup, and ensures a consistent first install

@Component
class Startup(private val roleService: RoleService,
              private val userRepository: UserRepository,
              private val bCryptPasswordEncoder: BCryptPasswordEncoder,
              private val secretService: ISecretService) : ApplicationRunner {

    @Transactional
    override fun run(args: ApplicationArguments?) {

        // Create necessary roles
        val roleCreatorRole = roleService.addRole(
                key = ROLE_CREATOR_ROLE,
                title = "Role creator",
                description = "Can create new roles"
        )
        val courseCreator = roleService.addRole(
                key = COURSE_CREATOR_ROLE,
                title = "Course Creator",
                description = "Can create courses"
        )
        val groupMaintainerRole = roleService.addRole(
                key = GROUP_MAINTAINER_ROLE,
                title = "Group Maintainer",
                description = "Can maintain groups by deleting, updating, and creating "
        )
        val courseManagementRole = roleService.addRole(
                key = COURSE_MANAGEMENT_ROLE,
                title = "Course Manager",
                description = "Can edit Courses"
        )

        val allRoles = listOf(courseCreator, roleCreatorRole, groupMaintainerRole, courseManagementRole)

        val systemPasswordPath = Paths.get("system_password")
        val systemPassword = try {
            String(secretService.get(systemPasswordPath.toString())).trim()
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

        sysadmin.roles.addAll(allRoles)
        userRepository.save(sysadmin)

        systemUser.roles.addAll(allRoles)
        userRepository.save(systemUser)
    }
}


@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
class RetryableDataSourceBeanPostProcessor : BeanPostProcessor {
    @Throws(BeansException::class)
    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any {
        var bean = bean
        if (bean is DataSource) {
            bean = RetryableDataSource(bean)
        }
        return bean
    }

    @Throws(BeansException::class)
    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any {
        return bean
    }
}

open internal class RetryableDataSource(private val delegate: DataSource) : AbstractDataSource() {

    @Retryable(maxAttempts = 10, backoff = Backoff(multiplier = 2.3, maxDelay = 30000))
    @Throws(SQLException::class)
    override fun getConnection(): Connection {
        return delegate.connection
    }

    @Retryable(maxAttempts = 10, backoff = Backoff(multiplier = 2.3, maxDelay = 30000))
    @Throws(SQLException::class)
    override fun getConnection(username: String, password: String): Connection {
        return delegate.getConnection(username, password)
    }

}
