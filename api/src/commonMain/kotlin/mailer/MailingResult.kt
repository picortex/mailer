@file:JsExport

package mailer

import kotlin.js.JsExport

sealed class MailingResult {
    object Success : MailingResult()
    data class Failure(val cause: Throwable) : MailingResult()
}