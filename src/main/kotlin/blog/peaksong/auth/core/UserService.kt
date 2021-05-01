package blog.peaksong.auth.core

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service("userDetailsService")
class UserService: UserDetailsService {

    @Autowired
    private lateinit var authUserDAO: AuthUserDAO

    override fun loadUserByUsername(username: String): UserDetails {
        val ret = authUserDAO.findUserById(username)

        if (!ret.isPresent)
            throw  UsernameNotFoundException("用户不存在")

        val user = ret.get()

        val roleList = authUserDAO.findRoleByUser(username)

        return User(username, user.userToken, roleList.map { SimpleGrantedAuthority(it) })
    }
}