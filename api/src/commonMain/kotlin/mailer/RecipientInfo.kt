package mailer

import identifier.Email

data class RecipientInfo(
    val email: Email,
    val name: String? = null
) {
    constructor(email: String, name: String? = null) : this(Email(email), name ?: email)
}