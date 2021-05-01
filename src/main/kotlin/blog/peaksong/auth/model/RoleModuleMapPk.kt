package blog.peaksong.auth.model

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Id

data class RoleModuleMapPk (

    @Id
    @Column("ROLE_NAME", nullable = false)
    val role: String,

    @Id
    @Column("MODULE_NAME", nullable = false)
    val module: String
): Serializable