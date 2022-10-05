package mailer

import identifier.Email
import kotlinx.serialization.Serializable

@Serializable
data class AddressInfo(
    val email: Email,
    val name: String? = null
) {
    constructor(email: String, name: String? = null) : this(Email(email), name)
}
