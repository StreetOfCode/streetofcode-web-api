package sk.streetofcode.webapi.configuration.annotation

import org.springframework.security.access.prepost.PreAuthorize
import sk.streetofcode.webapi.service.AuthenticationService

@PreAuthorize("hasRole('${AuthenticationService.ADMIN_GROUP_NAME}')")
annotation class IsAdmin
