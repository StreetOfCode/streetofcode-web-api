package sk.streetofcode.webapi.api.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.FORBIDDEN)
class InvalidAuthenticationException(message: String?) : RuntimeException(message)
