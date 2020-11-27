package sk.streetofcode.courseplatformbackend.configuration.annotation

import org.springframework.security.access.prepost.PreAuthorize
import sk.streetofcode.courseplatformbackend.service.AuthenticationService


@PreAuthorize("hasRole('${AuthenticationService.ADMIN_GROUP_NAME}')")
annotation class IsAdmin