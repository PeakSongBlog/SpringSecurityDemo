package blog.peaksong.auth.core

import org.springframework.security.crypto.password.PasswordEncoder

class MyPasswordEncoder: PasswordEncoder {
    override fun encode(rawPassword: CharSequence?): String {

        return rawPassword.toString()
    }

    override fun matches(rawPassword: CharSequence?, encodedPassword: String?): Boolean {

        if(rawPassword.isNullOrEmpty() || encodedPassword.isNullOrEmpty())
            return false

        return rawPassword == encodedPassword
    }
}