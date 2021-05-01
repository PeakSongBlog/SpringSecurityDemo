package blog.peaksong.auth.model

import javax.persistence.*

@Entity
@Table(name="AUTH_USER", schema = "DEMO")
data class User(

    @Id
    @Column(name="USER_ID", nullable = false)
    var id: String,

    @Basic
    @Column(name="USER_NAME", nullable = false)
    var userName: String,

    @Basic
    @Column(name="USER_TOKEN", nullable = false)
    var userToken: String

)