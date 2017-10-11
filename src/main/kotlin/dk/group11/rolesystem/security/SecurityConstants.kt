package dk.group11.rolesystem.security

const val SECRET = "SecretKeyToGenJWTs"
const val EXPIRATION_TIME: Long = 864000000 // 10 days
const val TOKEN_PREFIX = "Bearer "
const val HEADER_STRING = "Authorization"
const val SIGN_UP_URL = "/api/repositories"

