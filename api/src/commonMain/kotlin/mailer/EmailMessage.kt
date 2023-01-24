@file:JsExport
@file:Suppress("NON_EXPORTABLE_TYPE")

package mailer

import kollections.List
import kollections.iListOf
import kotlinx.serialization.Serializable
import kotlin.js.JsExport

data class EmailMessage(
    val subject: String,
    val from: AddressInfo,
    val to: List<AddressInfo>,
    val body: String,
    val attachments: List<EmailAttachment<Any?>> = iListOf(),
    val status: List<EmailStatus>
)