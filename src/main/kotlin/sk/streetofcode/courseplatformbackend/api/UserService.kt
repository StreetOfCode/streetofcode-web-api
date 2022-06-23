package sk.streetofcode.courseplatformbackend.api

import sk.streetofcode.courseplatformbackend.api.request.UserAddRequest
import sk.streetofcode.courseplatformbackend.api.request.UserEditRequest
import sk.streetofcode.courseplatformbackend.model.User

interface UserService {
    fun get(id: String): User
    fun add(id: String, userAddRequest: UserAddRequest): User
    fun edit(id: String, userEditRequest: UserEditRequest): User
}
