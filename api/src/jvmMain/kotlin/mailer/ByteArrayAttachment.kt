package mailer

class ByteArrayAttachment(
    override val content: ByteArray,
    override val type: String,
    override val name: String
) : EmailAttachment<ByteArray>