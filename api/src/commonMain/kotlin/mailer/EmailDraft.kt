@file:JsExport
@file:Suppress("NON_EXPORTABLE_TYPE")

package mailer

import kotlinx.collections.interoperable.List
import kotlinx.collections.interoperable.listOf
import kotlin.js.JsExport

data class EmailDraft(
    val subject: String,
    val body: String,
    val attachments: List<EmailAttachment<*>> = listOf()
) {
    fun toMessage(
        from: AddressInfo,
        to: List<AddressInfo>
    ) = EmailMessage(
        subject = subject,
        from = from,
        to = to,
        body = body,
        attachments = attachments,
        status = listOf()
    )
}