package dk.group11.rolesystem.helpers

fun String.toIDList(separator: String = ","): List<Long> =
        this.split(delimiters = separator).mapNotNull { it.toLongOrNull() }