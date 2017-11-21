package dk.group11.rolesystem.security


interface ISecurityService {

    fun getId(): Long

    fun getToken(): String
    fun requireRoles(vararg roleKeys: String)
}