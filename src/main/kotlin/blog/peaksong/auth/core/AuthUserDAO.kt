package blog.peaksong.auth.core

import blog.peaksong.auth.model.RoleUrlMap
import blog.peaksong.auth.model.User
import blog.peaksong.util.LoggerDelegate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import java.util.*
import java.util.stream.Collectors

@Service
class AuthUserDAO {

    private val log by LoggerDelegate()

    @Autowired
    private lateinit var authUserRepo: AuthUserDAO

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    fun findUserById(id: String): Optional<User>{
        return authUserRepo.findUserById(id)
    }

    fun findRoleByUser(id: String): List<String> {
        return jdbcTemplate.queryForList(
            "select ROLE_NAME from demo.USER_ROLE_MAP where USER_ID = '$id'", String::class.java
        )
    }

    fun urlRoleMap(): Map<String, List<String>>{
        val roleUrlList = jdbcTemplate.query(
            "Select a.ROLE_NAME as role, b.URL_PATTERN as pattern" +
                    "from demo.ROLE_MODULE_MAP a, demo.AUTH_MODULE b" +
                    "where a.MODULE_NAME = b.MODULE_NAME" +
                    "group by a.ROLE_NAME, b.URL_PATTERN"
        ){ rs, _ -> RoleUrlMap(rs.getString("role"), rs.getString("pattern")) }

        return roleUrlList.stream().collect(Collectors.groupingBy { it.pattern }).mapValues {
            entry -> entry.value.map { item -> item.role }
        }
    }
}