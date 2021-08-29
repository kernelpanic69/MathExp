package com.github.arturnikolaenko.mathexp.evaluator

import com.github.arturnikolaenko.mathexp.builder.ExpressionBuilder
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class EvaluatorTest {
    @Test
    fun tesFull() {
        val expStr = "21+9-45*9"
        val bl = ExpressionBuilder()
        val res = 21 + 9 - 45 * 9

        for (ch in expStr) {
            bl.add(ch)
        }
        val ev = Evaluator(bl)
        println(res)
        println(ev.evaluate())
    }

    @Test
    fun testSingle() {
        val bl = ExpressionBuilder()
        bl.add('-')
        bl.add('1')
        bl.add('2')
        bl.add('m')

        println(bl.tokens)

        val res = Evaluator(bl).evaluate()
        println(res)
    }
}