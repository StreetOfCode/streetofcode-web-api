package sk.streetofcode.courseplatformbackend.api

import sk.streetofcode.courseplatformbackend.api.request.SocUserAddRequest
import sk.streetofcode.courseplatformbackend.api.request.SocUserEditRequest
import sk.streetofcode.courseplatformbackend.model.SocUser

interface SocUserService {
    fun get(id: String): SocUser
    fun add(id: String, socUserAddRequest: SocUserAddRequest): SocUser
    fun edit(id: String, socUserEditRequest: SocUserEditRequest): SocUser
}
