package mailer

data class MockAttachment<T>(
    override val content: T,
    override val type: String,
    override val name: String
) : EmailAttachment<T>