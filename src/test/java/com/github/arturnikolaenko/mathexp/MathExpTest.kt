package com.github.arturnikolaenko.mathexp

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class MathExpTest {
    @Test
    fun addCharacter() {
        val exp = MathExp()
        val expString = "-21+(cos(21+(90/2)^2))-log(21)"

        for (ch in expString) {
            exp.addCharacter(ch)
        }
    }
}