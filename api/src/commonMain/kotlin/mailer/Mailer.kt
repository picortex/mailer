package mailer

import identifier.Email
import koncurrent.Later
import kotlinx.collections.interoperable.List
import kotlinx.collections.interoperable.listOf

/**
 * An interface to be used to send emails
 */
interface Mailer {
    fun send(draft: EmailDraft, from: AddressInfo, to: List<AddressInfo>): Later<EmailMessage>

    fun send(draft: EmailDraft, from: AddressInfo, to: AddressInfo) = send(draft, from, listOf(to))

    fun send(draft: EmailDraft, from: String, to: String) = send(draft, Email(from), Email(to))

    fun send(draft: EmailDraft, from: Email, to: Email) = send(draft, AddressInfo(from), listOf(AddressInfo(to)))
}