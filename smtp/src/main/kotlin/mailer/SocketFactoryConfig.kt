package mailer

import java.util.*

interface SocketFactoryConfig {
    val port: Int
    val clazz: String
    val fallback: Boolean

    companion object {
        @JvmField
        val PORT = Property("mail.smtp.socketFactory.port", 465)

        @JvmField
        val CLASS = Property("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")

        @JvmField
        val FALLBACK = Property("socketFactory.fallback", false)

        @JvmSynthetic
        operator fun invoke(
            port: Int = PORT.default,
            clazz: String = CLASS.default,
            fallback: Boolean = FALLBACK.default
        ) = object : SocketFactoryConfig {
            override val port: Int = port
            override val clazz: String = clazz
            override val fallback: Boolean = fallback
        }

        @JvmSynthetic
        operator fun invoke(properties: Properties) = invoke(
            port = properties[PORT.KEY]?.toString()?.toIntOrNull() ?: PORT.default,
            clazz = properties[CLASS.KEY]?.toString() ?: CLASS.default,
            fallback = properties[FALLBACK.KEY]?.toString()?.toBooleanStrictOrNull() ?: FALLBACK.default
        )

        @JvmStatic
        @JvmOverloads
        fun create(
            port: Int = PORT.default,
            clazz: String = CLASS.default,
            fallback: Boolean = FALLBACK.default
        ) = invoke(port, clazz, fallback)

        @JvmStatic
        fun create(properties: Properties) = invoke(properties)
    }
}