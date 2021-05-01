package blog.peaksong.auth

import blog.peaksong.auth.util.JwtUtil
import blog.peaksong.util.LoggerDelegate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class JwtProperty {

    private val log by LoggerDelegate()

    @Value("\${jwt.secret}")
    lateinit var secret: String

    @Value("\${jwt.expiration}")
    var expiration: Long = 3600_000

    @Value("\${jwt.header}")
    lateinit var header: String

    @Value("\${jwt.prefix}")
    lateinit var prefix: String

    @PostConstruct
    fun setJwtUtil() {
        JwtUtil.expireTime = this.expiration
        JwtUtil.tokenHeader = this.header
        JwtUtil.tokenPrefix = this.prefix
        JwtUtil.tokenSecret = this.secret

        log.info("JwtProperty 配置 JwtUtil 静态变量")
    }
}