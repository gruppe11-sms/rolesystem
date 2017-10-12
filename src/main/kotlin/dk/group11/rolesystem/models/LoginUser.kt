package dk.group11.rolesystem.models

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class LoginUser(private var username: String = "",
                     private var password: String = "",
                     private var authority: MutableCollection<out GrantedAuthority> = emptyList<GrantedAuthority>().toMutableList(),
                     private var enabled: Boolean = true) : UserDetails {

    override fun getUsername(): String {
        return username
    }

    override fun getPassword(): String {
        return password
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return authority
    }

    override fun isEnabled(): Boolean {
        return enabled
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }
}