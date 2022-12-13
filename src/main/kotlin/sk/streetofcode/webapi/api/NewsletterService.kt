package sk.streetofcode.webapi.api

import sk.streetofcode.webapi.api.request.AddEmailToNewsletterRequest

interface NewsletterService {
    fun addToNewsletter(userId: String? = null, request: AddEmailToNewsletterRequest)
}
