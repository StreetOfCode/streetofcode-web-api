package sk.streetofcode.courseplatformbackend.rest

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import sk.streetofcode.courseplatformbackend.api.UserService
import sk.streetofcode.courseplatformbackend.api.request.UserAddRequest
import sk.streetofcode.courseplatformbackend.api.request.UserEditRequest
import sk.streetofcode.courseplatformbackend.configuration.annotation.IsAuthenticated
import sk.streetofcode.courseplatformbackend.model.User
import sk.streetofcode.courseplatformbackend.service.AuthenticationService

@RestController
@RequestMapping("user")
class UserController(val userService: UserService, val authenticationService: AuthenticationService) {
    @GetMapping
    @IsAuthenticated
    fun get(): ResponseEntity<User> {
        return ResponseEntity.ok(userService.get(authenticationService.getUserId()))
    }

    @PostMapping
    @IsAuthenticated
    fun post(@RequestBody userRequest: UserAddRequest): ResponseEntity<User> {
        return ResponseEntity.ok(userService.add(authenticationService.getUserId(), userRequest))
    }

    @PutMapping
    @IsAuthenticated
    fun put(@RequestBody userRequest: UserEditRequest): ResponseEntity<User> {
        return ResponseEntity.ok(userService.edit(authenticationService.getUserId(), userRequest))
    }
}
