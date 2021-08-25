package com.github.arturnikolaenko.mathexp.builder

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

internal class ExpressionBuilderTest {
    private var exp = ExpressionBuilder()

    @BeforeEach
    fun createBuilder() {
        exp = ExpressionBuilder()
    }

    @Test
    fun testSign() {
        val testStr = "--1+-(-+6)*(+4)"
        val expStr = "-1+(-6)*(+4)"
        addString(testStr)

        assertEquals(expStr, exp.expString)
    }

    @Test
    fun testBrackets() {
        val testStr = "((4+(32-2))+4)))"
        val expStr = "((4+(32-2))+4)"

        addString(testStr)

        assertEquals(expStr, exp.expString)
    }

    @Test
    fun testComplex() {
        val testStr = "42+cos(45-(56*pi*4))/log(-21)*(-3+8m)"
        val expStr = testStr

        addString(testStr)
        assertEquals(expStr, exp.expString)
    }

    @Test
    fun testDrop() {
        val testStr = "45+12-cos(21)"
        val expStr = "45+12-8"

        addString(testStr)

        for (i in 0..7)
            exp.dropLast()

        exp.add('-')
        exp.add('8')
        exp.add(')')

        assertEquals(expStr, exp.expString)
    }

    private fun addString(str: String) {
        for (ch in str) {
            exp.add(ch)
        }
    }
}