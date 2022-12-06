package mailer

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("mock")
data class MockMailerExternalConfig(
    val printToConsole: Boolean = MockMailerConfig.DEFAULT_PRINT_TO_CONSOLE
) : MailerExternalConfig()