package blog.peaksong.auth.model

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Id

data class UserRoleMapPk(

    @Id
    @Column("USER_ID")
    val userId: String,

    @Id
    @Column("ROLE_NAME")
    val roleName: String

): Serializable
