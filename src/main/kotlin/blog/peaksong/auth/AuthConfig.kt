package blog.peaksong.auth

import blog.peaksong.auth.core.AuthDecisionManager
import blog.peaksong.auth.core.FilterMetadata
import blog.peaksong.auth.core.MyPasswordEncoder
import blog.peaksong.auth.handler.AuthDeniedHandler
import blog.peaksong.auth.handler.AuthEntryPoint
import blog.peaksong.base.WebConstant
import blog.peaksong.util.LoggerDelegate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.security.config.annotation.ObjectPostProcessor
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.password.PasswordEncoder
import blog.peaksong.auth.filter.AuthFilter
import blog.peaksong.auth.filter.JwtFilter
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EntityScan("blog.peaksong.auth.model")
@EnableJpaRepositories("blog.peaksong.auth.repository")
class AuthConfig: WebSecurityConfigurerAdapter() {

    private val log by LoggerDelegate()

    @Autowired
    private lateinit var filterMetadata: FilterMetadata

    @Autowired
    private lateinit var authDecisionManager: AuthDecisionManager

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return MyPasswordEncoder()
    }


    override fun configure(http: HttpSecurity) {
        with(http) {
            csrf.disable()

            cors()

            sessionManagement().disable()

            formLogin().loginProcessingUrl(WebConstant.API_AUTH_URL).permitAll()
            authorizeRequests().antMatchers("/").permitAll()

            authorizeRequests().withObjectPostProcessor(
                object: ObjectPostProcessor<FilterSecurityInterceptor> {
                    override fun <O : FilterSecurityInterceptor?> postProcess(obj: O): O {
                        obj!!.securityMetadataSource = filterMetadata
                        obj.accessDecisionManager = authDecisionManager
                        return obj
                    }
                }
            )

            authorizeRequests().anyRequest().authenticated()

            exceptionHandling().accessDeniedHandler(AuthDeniedHandler())
            exceptionHandling().authenticationEntryPoint(AuthEntryPoint())

            addFilterBefore(
                AuthFilter(authenticationManager()),
                UsernamePasswordAuthenticationFilter::class.java
            )

            addFilterBefore(JwtFilter(), AuthFilter::class.java)

            log.info("HttpSecurity 配置完成")

        }
    }
}