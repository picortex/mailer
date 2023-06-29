import kotlin.test.Test
import expect.expect
import kommander.expect

class RedanduntTest {
    @Test
    fun should_pass() {
        expect<Int>(1 + 1).toBe(2)
    }
}