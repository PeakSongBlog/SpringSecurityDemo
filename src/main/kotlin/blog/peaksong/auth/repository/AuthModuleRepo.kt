package blog.peaksong.auth.repository

import blog.peaksong.auth.model.Module
import org.springframework.data.repository.CrudRepository

interface AuthModuleRepo: CrudRepository<Module, String> {
}