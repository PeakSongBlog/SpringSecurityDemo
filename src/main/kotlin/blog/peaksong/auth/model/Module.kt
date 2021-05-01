package blog.peaksong.auth.model

import javax.persistence.*

@Entity
@Table(name="AUTH_MODULE", schema = "DEMO")
data class Module(

    @Id
    @Column(name="MODULE_NAME", nullable = false)
    var module_name: String,

    @Basic
    @Column(name="URL_PATTERN", nullable = false)
    var pattern: String

)
