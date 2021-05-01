package blog.peaksong.auth.model

import javax.persistence.*

@Entity
@IdClass(UserRoleMapPk::class)
@Table(name = "USER_ROLE_MAP", schema = "DEMO")
data class UserRoleMap(

    @Id
    @Column(name = "USER_ID")
    val userId: String,

    @Id
    @Column(name = "ROLE_NAME")
    val roleName: String

)
