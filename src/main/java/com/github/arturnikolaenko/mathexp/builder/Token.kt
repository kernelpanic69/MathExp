package com.github.arturnikolaenko.mathexp.builder

import java.lang.StringBuilder

class Token(val type: TokenType) {
    val value: String
        get() = buildValue.toString()

    private val buildValue = StringBuilder()

    internal fun update(ch: Char) {
        buildValue.append(ch)
    }

    override fun toString(): String {
        return "{'$value', '$type'}"
    }
}