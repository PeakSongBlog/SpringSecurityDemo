package blog.peaksong.auth.core

import blog.peaksong.base.WebConstant
import blog.peaksong.util.LoggerDelegate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.ConfigAttribute
import org.springframework.security.access.SecurityConfig
import org.springframework.security.web.FilterInvocation
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource
import org.springframework.stereotype.Component
import org.springframework.util.AntPathMatcher

@Component
class FilterMetadata: FilterInvocationSecurityMetadataSource {

    private val log by LoggerDelegate()

    private val pathMatcher = AntPathMatcher()

    init {
        log.info("FilterMetadata 创建")
    }

    @Autowired
    private lateinit var authUserDAO: AuthUserDAO

    override fun getAttributes(obj: Any): MutableCollection<ConfigAttribute> {
        val requestUrl = (obj as FilterInvocation).requestUrl

        val urlRoleMap = authUserDAO.urlRoleMap()

        val pvgList = mutableListOf<String>()

        for (urlRoleGrp in urlRoleMap){
            if(pathMatcher.match(urlRoleGrp.key, requestUrl)){
                pvgList.addAll(urlRoleGrp.value)
            }
        }

        if(pvgList.isNotEmpty())
            return SecurityConfig.createList(*pvgList.toTypedArray())

        log.info("$requestUrl 使用默认角色")
        return SecurityConfig.createList(WebConstant.ROLE_DEFAULT)
    }

    override fun getAllConfigAttributes(): MutableCollection<ConfigAttribute>? {
        return null
    }

    // 支持所有的类
    override fun supports(clazz: Class<*>?): Boolean {
        return true
    }
}