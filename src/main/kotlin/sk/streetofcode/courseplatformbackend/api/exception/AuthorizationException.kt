package sk.streetofcode.courseplatformbackend.api.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.FORBIDDEN)
class AuthorizationException(message: String? = "You are not authorized to make this call") : RuntimeException(message) {
}
