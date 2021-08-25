package com.github.arturnikolaenko.mathexp.builder

import java.lang.StringBuilder

internal class Token(var type: TokenType) {
    val value: String
        get() = buildValue.toString()

    private val buildValue = StringBuilder()

    fun update(ch: Char) {
        buildValue.append(ch)
    }

    override fun toString(): String {
        return "{'$value', '$type'}"
    }
}