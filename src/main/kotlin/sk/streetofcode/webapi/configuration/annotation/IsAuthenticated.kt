package sk.streetofcode.webapi.configuration.annotation

import org.springframework.security.access.prepost.PreAuthorize

@PreAuthorize("@authenticationService.isAuthenticated()")
annotation class IsAuthenticated
