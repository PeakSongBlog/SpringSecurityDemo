package blog.peaksong.auth.filter

import blog.peaksong.auth.util.JwtUtil
import blog.peaksong.base.ResponseWrap
import blog.peaksong.base.WebConstant
import blog.peaksong.util.LoggerDelegate
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher
import org.springframework.web.filter.OncePerRequestFilter
import java.nio.charset.StandardCharsets
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtFilter: OncePerRequestFilter() {

    private val log by LoggerDelegate()

    private fun tokenAuthFailure(response: HttpServletResponse) {

        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = StandardCharsets.UTF_8.name()
        response.status = WebConstant.AUTH_SUCCESS

        response.writer.println(
            ResponseWrap.fail("非法请求被拦截, 请通过认证后请求数据", "Authentication Fail").toJson()
        )
        response.writer.flush()
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if(!RequestHeaderRequestMatcher(JwtUtil.tokenHeader).matches(request) &&
                request.requestURI == WebConstant.API_AUTH_URL) {
            filterChain.doFilter(request, response)
            return
        }

        log.info("命中token过滤器")

        val header = request.getHeader(JwtUtil.tokenHeader)
        if(header == null || !header.startsWith(JwtUtil.tokenPrefix) ||
                header.substring(JwtUtil.tokenPrefix.length + 1).isBlank()){
            tokenAuthFailure(response)
            log.warn("token header校验不通过")
            return
        }

        val token = header.substring(JwtUtil.tokenPrefix.length + 1)

        try{
            if(!JwtUtil.verifyToken(token)){
                tokenAuthFailure(response)
                log.warn("token 内容校验不通过")
                return
            }

            val username: String = JwtUtil.getUserName(token)
            val authorities = JwtUtil.getRole(token).stream().map { SimpleGrantedAuthority(it) }

            log.info("token 令牌验证成功")

            SecurityContextHolder.getContext().authentication =
                UsernamePasswordAuthenticationToken(username, null, authorities)

            filterChain.doFilter(request, response)

        } catch (e: Exception){
            log.error("token 解析失败")
            tokenAuthFailure(response)
            return
        }

    }
}