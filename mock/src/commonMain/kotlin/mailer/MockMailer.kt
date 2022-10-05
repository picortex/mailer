package mailer

import koncurrent.Later
import koncurrent.later
import kotlinx.collections.interoperable.List
import kotlinx.coroutines.delay
import kotlin.math.max

class MockMailer(val config: MockMailerConfig = MockMailerConfig()) : Mailer {
    private fun AddressInfo.toDetailsString() = if (name == null) email.value else "$name <${email.value}>"

    private fun StringBuilder.appendRecipients(recipients: List<AddressInfo>) {
        if (recipients.size == 1) {
            val address = recipients.first().toDetailsString()
            appendLine("To:          $address")
        } else {
            val first = recipients.first()
            val rest = recipients - first
            appendLine("To:          ${first.toDetailsString()}")
            for (recipient in rest) {
                appendLine("             ${recipient.toDetailsString()}")
            }
        }
    }

    override fun send(draft: EmailDraft, from: AddressInfo, to: List<AddressInfo>): Later<EmailMessage> = config.scope.later {
        delay(config.simulationTime)
        if (config.printToConsole) {
            val separator = "${config.separator} ".repeat(3) + config.separator
            val output = buildString {
                appendLine(separator)
                appendLine("Mock Email [Mock Mailer]")
                appendLine(separator)
                appendLine("Subject:     ${draft.subject}")
                appendLine("From:        ${from.toDetailsString()}")
                appendRecipients(to)
                appendLine(separator)
                appendLine("Attachments: ${draft.attachments.joinToString(",") { it.name }}")
                appendLine(separator)
                appendMultiLines(draft.body)
                append(separator)
            }
            val outLines = output.split("\n")
            val width = max(outLines.maxOf { it.length }, config.charsPerLine)
            val adjustedLines = outLines.joinToString(separator = "\n") {
                "${config.marginWidth}${config.border}${config.paddingWidth}" +
                        if (it == separator) {
                            if (width.mod(2) == 0) {
                                val multiples = width / 2
                                "${config.separator} ".repeat(multiples - 1) + " ${config.separator}"
                            } else {
                                val multiples = (width - 1) / 2
                                "${config.separator} ".repeat(multiples) + config.separator
                            }

                        } else {
                            "$it${" ".repeat(width - it.length)}"
                        } +
                        "${config.paddingWidth}${config.border}"
            }
            println(adjustedLines)
        }
        draft.toMessage(from, to)
    }

    private fun StringBuilder.appendMultiLines(body: String) = body.split("\n").flatMap {
        it.chunked(config.charsPerLine)
    }.forEach { appendLine(it) }

    override fun toString(): String = "MockMailer(printToConsole=${config.printToConsole})"
}