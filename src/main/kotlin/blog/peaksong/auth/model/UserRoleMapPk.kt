package blog.peaksong.auth.model

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Id

data class UserRoleMapPk(

    @Id
    @Column(name="USER_ID")
    val userId: String,

    @Id
    @Column(name="ROLE_NAME")
    val roleName: String

): Serializable
