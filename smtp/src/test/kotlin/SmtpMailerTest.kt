@file:OptIn(ExperimentalCoroutinesApi::class)

import expect.expect
import identifier.Email
import kotlinx.collections.interoperable.listOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import koncurrent.later.await
import mailer.*
import java.io.InputStream
import java.util.*
import kotlin.test.Ignore
import kotlin.test.Test

@Ignore("This would be taking a lot of credit running it over and over")
class SmtpMailerTest {
    val prop = Properties().apply {
        val inStream = this@SmtpMailerTest::class.java.getResourceAsStream("sendgrid.properties")
        println(inStream)
        load(inStream)
    }

    val config = SmtpMailerConfig(prop)

    val mailer: Mailer = SmtpMailer(config)

    @Test
    fun should_send_an_email() = runTest {
        val cfg = config.toProperties()
        println(cfg)
        val message = mailer.send(
            draft = EmailDraft(
                subject = "Test Draft",
                body = "This is a test email"
            ),
            from = Email("support@picortex.com"),
            to = Email("andylamax@programmer.net"),
        ).await()
        expect(message).toBeNonNull()
    }

    @Test
    fun should_send_html() = runTest {
        val message = mailer.send(
            draft = EmailDraft(
                subject = "Test Draft",
                body = "<html><body><b>This is a test email</b>&nbsp;not bold</body></html>",
            ),
            from = Email("support@picortex.com"),
            to = Email("andylamax@programmer.net"),
        ).await()
        expect(message).toBeNonNull()
    }

    @Test
    fun should_send_html_with_attachments() = runTest {
        val inputStream = SmtpMailerTest::class.java.getResourceAsStream("Vonage_Guide.pdf")!!;
        val message = mailer.send(
            draft = EmailDraft(
                subject = "Test Draft",
                body = "<html><body><b>This is a test email</b>&nbsp;not bold</body></html>",
                attachments = listOf(
                    ByteArrayAttachment(
                        content = inputStream.readAllBytes(),
                        name = "Vonage_Guide.pdf",
                        type = "application/pdf"
                    )
                )
            ),
            from = Email("support@picortex.com"),
            to = Email("andylamax@programmer.net"),
        ).await()
        expect(message).toBeNonNull()
    }
}