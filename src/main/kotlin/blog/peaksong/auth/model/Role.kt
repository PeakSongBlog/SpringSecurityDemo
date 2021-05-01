package blog.peaksong.auth.model

import javax.persistence.*

@Entity
@Table(name="AUTH_ROLE", schema = "DEMO")
data class Role(

    @Id
    @Column(name="ROLE_NAME", nullable = false)
    var id: String

)