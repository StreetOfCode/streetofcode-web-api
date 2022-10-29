package sk.streetofcode.webapi.configuration

import io.sentry.Hint
import io.sentry.SentryEvent
import io.sentry.SentryOptions
import org.springframework.stereotype.Component

@Component
class SentryBeforeCallback : SentryOptions.BeforeSendCallback {
    override fun execute(event: SentryEvent, hint: Hint): SentryEvent? {
        if (event.throwable is org.springframework.security.access.AccessDeniedException) {
            // Don't send errors which are type of AccessDeniedException.
            // These errors are created when an unauthorized user tries to call an API which has IsAdmin annotation.
            return null
        }

        return event
    }
}
