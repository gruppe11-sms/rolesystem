package dk.group11.rolesystem.services

import org.springframework.stereotype.Service
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

interface ISecretService {
    fun get(name: String): ByteArray
}

@Service
class SecretService : ISecretService {
    /**
     * Used to cache any secret calculations, as they otherwise would be extremely
     * slow when they have to hit the disk
     */
    private val cache = mutableMapOf<String, ByteArray>()

    override fun get(name: String) = cache.computeIfAbsent(name) {
        try {
            Files.readAllBytes(Paths.get("/run/secrets/" + name))
        } catch (e: IOException) {
            println("Could not load secret $name from docker secrets, attempting local directory. ")
            Files.readAllBytes(Paths.get(name))
        }!!
    }
}