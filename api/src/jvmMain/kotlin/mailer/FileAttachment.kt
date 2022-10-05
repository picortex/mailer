package mailer

import java.io.File

class FileAttachment(
    override val content: File,
    override val type: String,
    override val name: String
) : EmailAttachment<File>