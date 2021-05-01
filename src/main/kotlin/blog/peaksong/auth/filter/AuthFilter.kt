package blog.peaksong.auth.filter

import blog.peaksong.auth.util.JwtUtil
import blog.peaksong.base.ResponseWrap
import blog.peaksong.base.WebConstant
import blog.peaksong.util.LoggerDelegate
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import java.nio.charset.StandardCharsets
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.streams.toList

class AuthFilter(authenticationManager: AuthenticationManager) :
    UsernamePasswordAuthenticationFilter(authenticationManager) {

    private val log by LoggerDelegate()

    init {
        super.setAuthenticationManager(authenticationManager)
        setRequiresAuthenticationRequestMatcher(AntPathRequestMatcher(WebConstant.API_AUTH_URL))
    }


    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        if (request.method != "POST")
            throw AuthenticationServiceException("不被支持的认证方法: ${request.method}")

        // 可以在此验证header
//        if (request.getHeader(""))
//            throw AuthenticationServiceException("")

        var username = obtainUsername(request) ?: throw AuthenticationServiceException("用户名不允许为空")
        val password = obtainPassword(request) ?: throw AuthenticationServiceException("密码不允许为空")

        username = username.trim()

        val authRequest = UsernamePasswordAuthenticationToken(username, password)

        setDetails(request, authRequest)

        return this.authenticationManager.authenticate(authRequest)
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication
    ) {
        SecurityContextHolder.getContext().authentication = authResult

        val username = authResult.name

        val token = JwtUtil.createToken(username, authResult.authorities.stream().map { it.authority }.toList())

        log.info("用户${JwtUtil.getUserName(token)}认证成功, 角色: ${JwtUtil.getRole(token)}")

        response.setHeader(JwtUtil.tokenHeader, "${JwtUtil.tokenPrefix} $token")

        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = StandardCharsets.UTF_8.name()
        response.status = WebConstant.AUTH_SUCCESS

        response.writer.println(
            ResponseWrap.success("认证成功", "Authentication Success").toJson()
        )
        response.writer.flush()
    }

    override fun unsuccessfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        failed: AuthenticationException
    ) {

        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = StandardCharsets.UTF_8.name()
        response.status = WebConstant.AUTH_SUCCESS

        response.writer.println(
            ResponseWrap.fail(failed.message.toString(), "Authentication Fail").toJson()
        )
        response.writer.flush()
    }
}