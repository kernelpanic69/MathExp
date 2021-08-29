package com.github.arturnikolaenko.mathexp.evaluator.converter

import org.junit.jupiter.api.Test

internal class ConverterTest {
    @Test
    fun testUnitCreation() {
        println(Converter.convert(1.0, "yd", "cm"))
    }
}