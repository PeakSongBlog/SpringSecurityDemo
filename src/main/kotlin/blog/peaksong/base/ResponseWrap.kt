package blog.peaksong.base

import com.fasterxml.jackson.databind.ObjectMapper

class ResponseWrap<T>(code: String, msg: String, content: T) {

    var content: T? = null
    var msg: String? = ""
    var code: String? = ""

    fun toJson(): String {
        return ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(this)
    }

    init {
        this.code = code
        this.content = content
        this.msg = msg
    }

    companion object {
        @JvmStatic
        private val SUCCESS = "SUCCESS"

        @JvmStatic
        private val SUCCESS_MSG = "访问成功"

        @JvmStatic
        private val FAIL = "FAIL"

        fun <T> fail(msg: String, content: T): ResponseWrap<T> {
            return ResponseWrap(FAIL, msg, content)
        }

        fun <T> success(content: T): ResponseWrap<T> {
            return ResponseWrap(SUCCESS, SUCCESS_MSG, content)
        }

        fun <T> success(content: T, msg: String): ResponseWrap<T> {
            return ResponseWrap(SUCCESS, msg, content)
        }
    }
}