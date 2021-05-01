package blog.peaksong.auth

import blog.peaksong.auth.core.AuthDecisionManager
import blog.peaksong.auth.core.MyPasswordEncoder
import blog.peaksong.util.LoggerDelegate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource

@Configuration
@EntityScan("blog.peaksong.auth.model")
@EnableJpaRepositories("blog.peaksong.auth.repository")
class AuthConfig:  {

    private val log by LoggerDelegate()

    @Autowired
    private lateinit var FilterMetadata: FilterInvocationSecurityMetadataSource

    @Autowired
    private lateinit var authDecisionManager: AuthDecisionManager

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return MyPasswordEncoder()
    }



}