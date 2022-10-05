package mailer

import koncurrent.Later
import koncurrent.later
import kotlinx.collections.interoperable.List
import javax.activation.DataHandler
import javax.activation.FileDataSource
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart
import javax.mail.util.ByteArrayDataSource

class SmtpMailer(val config: SmtpMailerConfig) : Mailer {

    private val authenticator by lazy {
        object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(config.user, config.password)
            }
        }
    }

    private val session by lazy { Session.getDefaultInstance(config.toProperties(), authenticator) }

    private fun AddressInfo.toInternetAddress() = if (name == null) {
        InternetAddress(email.value)
    } else {
        InternetAddress(email.value, name)
    }

    override fun send(draft: EmailDraft, from: AddressInfo, to: List<AddressInfo>): Later<EmailMessage> = config.scope.later {
        val message = MimeMessage(session).apply {
            setFrom(from.toInternetAddress())
            addRecipients(Message.RecipientType.TO, to.map { it.toInternetAddress() }.toTypedArray())
            val multipart = MimeMultipart("mixed");
            subject = draft.subject
            val messageBodyPart = MimeBodyPart();
            messageBodyPart.setContent(draft.body, "text/html");
            multipart.addBodyPart(messageBodyPart);

            draft.attachments.forEachIndexed { index, attachment ->
                val attachmentBodyPart = MimeBodyPart()
                val dataSource = when (attachment) {
                    is ByteArrayAttachment -> ByteArrayDataSource(attachment.content, attachment.type)
                    is FileAttachment -> FileDataSource(attachment.content)
                    else -> error("Unsupported EmailAttachmentType ${attachment::class.simpleName}")
                }
                attachmentBodyPart.dataHandler = DataHandler(dataSource)
                attachmentBodyPart.setHeader("Content-ID", "<attachment-$index>")
                attachmentBodyPart.setHeader("Content-Type", attachment.type)
                attachmentBodyPart.fileName = attachment.name

                multipart.addBodyPart(attachmentBodyPart)
            }

            setContent(multipart)
        }

        Transport.send(message)
        draft.toMessage(from, to)
    }

    override fun toString(): String = "SmtpMailer(host=${config.host},port=${config.port})"
}