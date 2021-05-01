package blog.peaksong.auth.util

import blog.peaksong.util.LoggerDelegate
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTCreationException
import com.auth0.jwt.exceptions.JWTVerificationException
import java.util.*

object JwtUtil {

    lateinit var tokenHeader: String
    lateinit var tokenPrefix: String
    lateinit var tokenSecret: String
    var expireTime: Long = 3600_000

    private const val rolesItem = "roles"

    private val logger by LoggerDelegate()

    fun createToken(username: String, role: List<*>?): String? {
        return try {
            val algorithm = Algorithm.HMAC256(tokenSecret)

            JWT.create()
                .withExpiresAt(Date(System.currentTimeMillis() + expireTime))
                .withClaim("username", username)
                .withClaim(rolesItem, role)
                .sign(algorithm)
        } catch (jwtCreationException: JWTCreationException) {
            logger.error("token 创建失败")
            null
        }
    }

    fun verifyToken(token: String): Boolean {
        return try {
            val algorithm = Algorithm.HMAC256(tokenSecret)
            val verifier = JWT.require(algorithm).build()

            verifier.verify(token)
            true
        }catch (e: JWTVerificationException) {
            logger.error("token 验证过程失败")
            false
        }
    }

    fun getUserName(token: String?): String {
        return JWT.decode(token).getClaim("username").asString()
    }

    fun getRole(token: String?): List<String> {
        return JWT.decode(token).getClaim(rolesItem).asList(String::class.java)
    }
}