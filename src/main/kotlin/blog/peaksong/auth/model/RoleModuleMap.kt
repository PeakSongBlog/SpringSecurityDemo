package blog.peaksong.auth.model

import javax.persistence.*

@Entity
@IdClass(RoleModuleMapPk::class)
@Table(name = "ROLE_MODULE_MAP", schema = "DEMO")
data class RoleModuleMap(

    @Id
    @Column("ROLE_NAME", nullable = false)
    val role: String,

    @Id
    @Column("MODULE_NAME", nullable = false)
    val module: String
)