@file:JsExport

package mailer

import identifier.Email
import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@Serializable
sealed class EmailStatus {
    abstract val to: Email

    data class Sent(override val to: Email) : EmailStatus()
    data class Failed(override val to: Email, val cause: Throwable) : EmailStatus()
}