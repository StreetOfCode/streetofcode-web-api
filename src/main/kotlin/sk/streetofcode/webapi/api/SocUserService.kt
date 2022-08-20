package sk.streetofcode.webapi.api

import sk.streetofcode.webapi.api.request.SocUserAddRequest
import sk.streetofcode.webapi.api.request.SocUserEditRequest
import sk.streetofcode.webapi.model.SocUser

interface SocUserService {
    fun get(id: String): SocUser
    fun add(id: String, socUserAddRequest: SocUserAddRequest): SocUser
    fun edit(id: String, socUserEditRequest: SocUserEditRequest): SocUser
}
