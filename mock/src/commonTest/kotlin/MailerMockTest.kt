import expect.expect
import identifier.Email
import kollections.iListOf
import koncurrent.later.await
import kotlinx.coroutines.test.runTest
import mailer.AddressInfo
import mailer.EmailDraft
import mailer.MockAttachment
import mailer.MockMailer
import mailer.MockMailerConfig
import kotlin.test.Test

class MailerMockTest {
    val config = MockMailerConfig(
        simulationTime = 1000L,
        separator = "="
    )
    val mailer = MockMailer(config)

    @Test
    fun should_easily_send_an_email() = runTest {
        val message = mailer.send(
            draft = EmailDraft(
                subject = "Test Draft",
                body = "This is a test email"
            ),
            from = Email("from@test.com"),
            to = Email("to@gmail.com"),
        ).await()
        expect(message).toBeNonNull()
    }

    @Test
    fun should_support_attachments() = runTest {
        val message = mailer.send(
            draft = EmailDraft(
                subject = "Test Draft",
                body = "This is a test email",
                attachments = iListOf(
                    MockAttachment(4, "number", "test attachment"),
                    MockAttachment(mailer, "mailer", "Attached the freaking mailer son"),
                )
            ),
            from = Email("from@test.com"),
            to = Email("to@gmail.com"),
        ).await()
        expect(message).toBeNonNull()
    }

    @Test
    fun should_look_good_even_on_the_console() = runTest {
        val cfg = MockMailerConfig(charsPerLine = 55)
        val m = MockMailer(cfg)
        val draft = EmailDraft(
            subject = "Look good while doing it",
            body = "When you decide to do something, make sure you do it well and make sure you look good doing it\n" +
                    "It not only makes thr whole thing wow, but even people watching you do enjoy"
        )
        m.send(
            draft,
            from = AddressInfo(
                name = "Dope Developer",
                email = "anderson@developer.com"
            ), to = AddressInfo(
                name = "Console",
                email = "test@console.com"
            )
        ).await()
    }

    @Test
    fun should_find_sent_email_in_the_outbox() = runTest {
        val cfg = MockMailerConfig(charsPerLine = 55)
        val m = MockMailer(cfg)
        val draft = EmailDraft(
            subject = "Look good while doing it",
            body = "When you decide to do something, make sure you do it well and make sure you look good doing it\n" +
                    "It not only makes thr whole thing wow, but even people watching you do enjoy"
        )
        m.send(
            draft,
            from = AddressInfo(
                name = "Dope Developer",
                email = "anderson@developer.com"
            ), to = AddressInfo(
                name = "Console",
                email = "test@console.com"
            )
        ).await()

        expect(m.outbox["test@console.com"]).toContainElements()
    }

    @Test
    fun should_print_multiple_recipients_properly() = runTest {
        val cfg = MockMailerConfig(charsPerLine = 55)
        val m = MockMailer(cfg)
        val draft = EmailDraft(
            subject = "Look good while doing it",
            body = "When you decide to do something, make sure you do it well and make sure you look good doing it\n" +
                    "It not only makes thr whole thing wow, but even people watching you do enjoy",
            attachments = iListOf(
                MockAttachment(12, "image/jpg", "picture-12.jpg"),
                MockAttachment(13, "image/jpg", "picture-13.jpg"),
                MockAttachment(14, "image/jpg", "picture-14.jpg"),
            )
        )
        m.send(
            draft,
            from = AddressInfo(
                name = "Dope Developer",
                email = "anderson@developer.com"
            ),
            to = iListOf(
                AddressInfo(
                    name = "Anderson",
                    email = "test@anderson.com"
                ),
                AddressInfo(
                    name = "Lugendo",
                    email = "test@luge.com"
                ),
                AddressInfo(
                    name = "Vladmir Putin",
                    email = "test@vlad.com"
                ),
            )
        ).await()
    }
}