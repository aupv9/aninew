package autogear.frontapi.service

import autogear.frontapi.entity.Permission
import autogear.frontapi.entity.Role
import autogear.frontapi.repository.UserRepository
import autogear.frontapi.service.authen.ILoginAttemptService
import org.springframework.security.authentication.LockedException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthenticateService(val authenticateRepository: UserRepository,
    val loginAttemptService: ILoginAttemptService): UserDetailsService {


    @Transactional(readOnly = true)
    override fun loadUserByUsername(username: String?): UserDetails {
        if(loginAttemptService.isBlocked()) throw LockedException ("User Blocked")
        val user = authenticateRepository.findByEmail(username!!).orElseThrow { UsernameNotFoundException("User not found") }
        val authorities = this.getAuthorities(user.roles as List<Role>)
        return CustomUserDetails(user.id!!, user.userID, user.email, user.password!!, authorities)
    }

    fun getAuthorities(roles: Collection<Role>): Collection<GrantedAuthority> {
        val privileges = mutableListOf<String>()
        val collection  = mutableListOf<Permission>()
        roles.forEach {
            role -> privileges.add(role.name!!)
            collection.addAll(role.permission)
        }
        collection.forEach {
            privilege -> privileges.add(privilege.name)
        }
        return getGrantedAuthorities(privileges)
    }

    private fun getGrantedAuthorities(privileges: List<String>): List<GrantedAuthority> =  privileges.map { GrantedAuthority { it } }

    class CustomUserDetails(
        private val id: String,
        private val userId: String,
        private val username: String,
        private val password: String,
        private val authorities: Collection<GrantedAuthority>
    ) : UserDetails {


        fun getId(): String {
            return id
        }

        fun getUserId(): String {
            return userId
        }

        fun getEmail(): String {
            return username
        }


        override fun toString(): String {
            return "CustomUserDetails(id='$id', userId='$userId', username='$username', password='XXXXXXXXX', authorities=$authorities)"
        }

        override fun getAuthorities(): Collection<GrantedAuthority> {
            return authorities
        }

        override fun getPassword(): String {
            return password
        }

        override fun getUsername(): String {
            return username
        }


        override fun isAccountNonExpired(): Boolean {
            return true // Modify as needed
        }

        override fun isAccountNonLocked(): Boolean {
            return true // Modify as needed
        }

        override fun isCredentialsNonExpired(): Boolean {
            return true // Modify as needed
        }

        override fun isEnabled(): Boolean {
            return true // Modify as needed
        }
    }

}