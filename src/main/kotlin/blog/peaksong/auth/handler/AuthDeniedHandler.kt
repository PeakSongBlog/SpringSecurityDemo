package blog.peaksong.auth.handler

import blog.peaksong.base.ResponseWrap
import blog.peaksong.base.WebConstant
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import java.nio.charset.StandardCharsets
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthDeniedHandler: AccessDeniedHandler {
    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = StandardCharsets.UTF_8.name()
        response.status = WebConstant.AUTH_SUCCESS

        response.writer.println(
            ResponseWrap.fail("无权访问该接口", "Authentication Fail").toJson()
        )
        response.writer.flush()
    }
}