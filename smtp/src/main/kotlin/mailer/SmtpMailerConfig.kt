package mailer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.io.InputStream
import java.lang.IllegalArgumentException
import java.util.*

interface SmtpMailerConfig {
    val host: String
    val port: Int
    val user: String?
    val password: String?
    val auth: Boolean
    val startTls: Boolean
    val debug: Boolean
    val socketFactory: SocketFactoryConfig
    val scope: CoroutineScope

    companion object {
        @JvmField
        val HOST = Property<String?>("mail.smtp.host", null)

        @JvmField
        val PORT = Property("mail.smtp.port", 465)

        @JvmField
        val USER = Property<String?>("mail.smtp.user", null)

        @JvmField
        val PASSWORD = Property<String?>("mail.smtp.password", null)

        @JvmField
        val AUTH = Property("mail.smtp.auth", true)

        @JvmField
        val START_TLS = Property("mail.smtp.starttls.enable", true)

        @JvmField
        val DEBUG = Property("mail.smtp.debug", true)

        @JvmField
        val DEFAULT_SCOPE = CoroutineScope(SupervisorJob() + Dispatchers.IO)

        @JvmSynthetic
        operator fun invoke(
            host: String,
            port: Int = PORT.default,
            auth: Boolean = AUTH.default,
            user: String? = USER.default,
            password: String? = PASSWORD.default,
            startTls: Boolean = START_TLS.default,
            debug: Boolean = DEBUG.default,
            socketFactory: SocketFactoryConfig = SocketFactoryConfig(),
            scope: CoroutineScope = DEFAULT_SCOPE
        ) = object : SmtpMailerConfig {
            init {
                if (auth && user == null) {
                    throw IllegalArgumentException("User must not be null when auth is set to true (which is the default behaviour)")
                }
            }

            override val host: String = host
            override val port: Int = port
            override val user: String? = user
            override val password: String? = password
            override val auth: Boolean = auth
            override val startTls: Boolean = startTls
            override val debug: Boolean = debug
            override val socketFactory: SocketFactoryConfig = socketFactory
            override val scope: CoroutineScope = scope
        }

        @JvmSynthetic
        operator fun invoke(
            properties: Properties,
            scope: CoroutineScope = DEFAULT_SCOPE
        ) = invoke(
            host = properties[HOST.KEY]?.toString() ?: error("Key ${HOST.KEY} is not available in properties"),
            port = properties[PORT.KEY]?.toString()?.toIntOrNull() ?: PORT.default,
            auth = properties[AUTH.KEY]?.toString()?.toBooleanStrict() ?: AUTH.default,
            user = properties[USER.KEY]?.toString() ?: USER.default,
            password = properties[PASSWORD.KEY]?.toString() ?: PASSWORD.default,
            startTls = properties[START_TLS]?.toString()?.toBooleanStrict() ?: START_TLS.default,
            debug = properties[DEBUG.KEY]?.toString()?.toBooleanStrict() ?: DEBUG.default,
            socketFactory = SocketFactoryConfig(properties),
            scope = scope
        )

        @JvmStatic
        @JvmOverloads
        fun create(
            host: String,
            port: Int = PORT.default,
            auth: Boolean = AUTH.default,
            user: String? = USER.default,
            password: String? = PASSWORD.default,
            startTls: Boolean = START_TLS.default,
            debug: Boolean = DEBUG.default,
            socketFactory: SocketFactoryConfig = SocketFactoryConfig(),
            scope: CoroutineScope = DEFAULT_SCOPE
        ) = invoke(host, port, auth, user, password, startTls, debug, socketFactory, scope)

        @JvmStatic
        @JvmOverloads
        fun create(
            properties: Properties,
            scope: CoroutineScope = DEFAULT_SCOPE
        ) = invoke(properties, scope)

        fun from(stream: InputStream): SmtpMailerConfig {
            val props = Properties().apply { load(stream) }
            return invoke(props)
        }
    }

    fun toProperties(): Properties {
        val props = Properties()
        props[HOST.KEY] = host
        props[PORT.KEY] = port
        props[USER.KEY] = user
        props[PASSWORD.KEY] = password
        props[AUTH.KEY] = auth
        props[START_TLS.KEY] = startTls
        props[DEBUG.KEY] = debug
        props[SocketFactoryConfig.PORT.KEY] = socketFactory.port
        props[SocketFactoryConfig.CLASS.KEY] = socketFactory.clazz
        props[SocketFactoryConfig.FALLBACK.KEY] = socketFactory.fallback
        return props
    }
}