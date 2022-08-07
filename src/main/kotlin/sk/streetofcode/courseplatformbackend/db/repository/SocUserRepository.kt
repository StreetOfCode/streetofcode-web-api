package sk.streetofcode.courseplatformbackend.db.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sk.streetofcode.courseplatformbackend.model.SocUser

@Repository
interface SocUserRepository : CrudRepository<SocUser, String>
