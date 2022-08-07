package sk.streetofcode.courseplatformbackend.rest

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import sk.streetofcode.courseplatformbackend.api.SocUserService
import sk.streetofcode.courseplatformbackend.api.request.SocUserAddRequest
import sk.streetofcode.courseplatformbackend.api.request.SocUserEditRequest
import sk.streetofcode.courseplatformbackend.configuration.annotation.IsAuthenticated
import sk.streetofcode.courseplatformbackend.model.SocUser
import sk.streetofcode.courseplatformbackend.service.AuthenticationService

@RestController
@RequestMapping("user")
class UserController(val socUserService: SocUserService, val authenticationService: AuthenticationService) {
    @GetMapping
    @IsAuthenticated
    fun get(): ResponseEntity<SocUser> {
        return ResponseEntity.ok(socUserService.get(authenticationService.getUserId()))
    }

    @PostMapping
    @IsAuthenticated
    fun post(@RequestBody userRequest: SocUserAddRequest): ResponseEntity<SocUser> {
        return ResponseEntity.ok(socUserService.add(authenticationService.getUserId(), userRequest))
    }

    @PutMapping
    @IsAuthenticated
    fun put(@RequestBody userRequest: SocUserEditRequest): ResponseEntity<SocUser> {
        return ResponseEntity.ok(socUserService.edit(authenticationService.getUserId(), userRequest))
    }
}
