package sk.streetofcode.webapi.configuration.annotation

import org.springframework.security.access.prepost.PreAuthorize

@PreAuthorize("isAuthenticated()")
annotation class IsAuthenticated
