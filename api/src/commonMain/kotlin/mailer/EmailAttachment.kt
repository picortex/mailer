package mailer

interface EmailAttachment<out T> {
    val content: T
    val type: String
    val name: String
}