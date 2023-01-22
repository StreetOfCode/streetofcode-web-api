package sk.streetofcode.webapi.configuration.annotation

import org.springframework.security.access.prepost.PreAuthorize

@PreAuthorize("@authenticationService.isAdmin()")
annotation class IsAdmin
