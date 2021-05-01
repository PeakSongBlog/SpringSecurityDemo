package blog.peaksong.auth.model

import javax.persistence.*

@Entity
@IdClass(UserRoleMapPk::class.java)
@Table("USER_ROLE_MAP", schema = "DEMO")
data class UserRoleMap(

    @Id
    @Column("USER_ID")
    val userId: String,

    @Id
    @Column("ROLE_NAME")
    val roleName: String

)
