package blog.peaksong.auth.core

import org.springframework.stereotype.Component
import org.springframework.security.access.AccessDecisionManager
import org.springframework.security.access.ConfigAttribute
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.Authentication

@Component
class AuthDecisionManager: AccessDecisionManager {
    override fun decide(
        authentication: Authentication,
        o: Any?,
        configAttributes: MutableCollection<ConfigAttribute>
    ) {

        for (authority in authentication.authorities){
            for(configAttribute in configAttributes){
                if(authority.authority == configAttribute.attribute)
                    return
            }
        }

        throw AccessDeniedException("权限不足, 无法访问")
    }

    override fun supports(attribute: ConfigAttribute?): Boolean {
        return true
    }

    override fun supports(clazz: Class<*>?): Boolean {
        return true
    }


}