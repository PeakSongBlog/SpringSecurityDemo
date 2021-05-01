package blog.peaksong.auth.handler

import blog.peaksong.base.ResponseWrap
import blog.peaksong.base.WebConstant
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.AuthenticationEntryPoint
import java.nio.charset.StandardCharsets
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthEntryPoint: AuthenticationEntryPoint {

    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = StandardCharsets.UTF_8.name()
        response.status = WebConstant.AUTH_SUCCESS

        if(authException is UsernameNotFoundException)
            response.writer.println(
                ResponseWrap.fail("系统为找到该用户", "User not found").toJson()
            )
        else
            response.writer.println(
                ResponseWrap.fail("不允许匿名访问该接口", "Unauthorized Request").toJson()
            )
        response.writer.flush()
    }
}