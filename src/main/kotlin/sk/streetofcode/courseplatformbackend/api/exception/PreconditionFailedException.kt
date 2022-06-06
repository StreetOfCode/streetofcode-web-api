package sk.streetofcode.courseplatformbackend.api.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
class PreconditionFailedException(message: String?) : RuntimeException(message)
