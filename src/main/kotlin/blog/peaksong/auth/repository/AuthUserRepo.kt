package blog.peaksong.auth.repository

import blog.peaksong.auth.model.User
import org.springframework.data.repository.CrudRepository
import java.util.Optional

interface AuthUserRepo: CrudRepository<User, String> {

    override fun findById(id: String): Optional<User>
}