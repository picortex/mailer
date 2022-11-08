package mailer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlin.jvm.JvmField
import kotlin.jvm.JvmName
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic
import kotlin.jvm.JvmSynthetic

interface MockMailerConfig {
    val printToConsole: Boolean
    val separator: String
    val simulationTime: Long

    /**
     * It looks better when this value is an odd number
     */
    val charsPerLine: Int
    val marginWidth: String
    val border: String
    val paddingWidth: String
    val scope: CoroutineScope

    companion object {
        @JvmField
        val DEFAULT_PRINT_TO_CONSOLE = true

        @JvmField
        val DEFAULT_SIMULATION_TIME = 0L

        @JvmField
        val DEFAULT_SCOPE = CoroutineScope(SupervisorJob())

        @JvmField
        val DEFAULT_CHARS_PER_LINE = 55

        @JvmField
        val DEFAULT_SEPERATOR = "="

        @JvmField
        val DEFAULT_BORDER = "|"

        @JvmField
        val DEFAULT_MARGIN_WIDTH = "\t".repeat(6)

        @JvmField
        val DEFAULT_PADDING_WIDTH = " "

        @JvmOverloads
        @JvmStatic
        @JvmName("create")
        operator fun invoke(
            printToConsole: Boolean = DEFAULT_PRINT_TO_CONSOLE,
            simulationTime: Long = DEFAULT_SIMULATION_TIME,
            separator: String = DEFAULT_SEPERATOR,
            charsPerLine: Int = DEFAULT_CHARS_PER_LINE,
            marginWidth: String = DEFAULT_MARGIN_WIDTH,
            border: String = DEFAULT_BORDER,
            paddingWidth: String = DEFAULT_PADDING_WIDTH,
            scope: CoroutineScope = DEFAULT_SCOPE
        ) = object : MockMailerConfig {
            override val printToConsole: Boolean = printToConsole
            override val simulationTime: Long = simulationTime
            override val separator: String = separator
            override val charsPerLine = charsPerLine
            override val marginWidth = marginWidth
            override val border = border
            override val paddingWidth = paddingWidth
            override val scope: CoroutineScope = scope
        }
    }
}