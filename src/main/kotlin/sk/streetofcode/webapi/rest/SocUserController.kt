package sk.streetofcode.webapi.rest

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sk.streetofcode.webapi.api.SocUserService
import sk.streetofcode.webapi.api.request.SocUserAddRequest
import sk.streetofcode.webapi.api.request.SocUserEditRequest
import sk.streetofcode.webapi.configuration.annotation.IsAuthenticated
import sk.streetofcode.webapi.model.SocUser
import sk.streetofcode.webapi.service.AuthenticationService

@RestController
@RequestMapping("user")
class SocUserController(val socUserService: SocUserService, val authenticationService: AuthenticationService) {
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
