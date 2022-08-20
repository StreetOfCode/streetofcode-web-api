package sk.streetofcode.webapi.db.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sk.streetofcode.webapi.model.SocUser

@Repository
interface SocUserRepository : CrudRepository<SocUser, String>
