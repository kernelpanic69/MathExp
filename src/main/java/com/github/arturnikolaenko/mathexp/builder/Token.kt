package com.github.arturnikolaenko.mathexp.builder

import java.lang.StringBuilder

class Token(var type: TokenType) {
    val value: String
        get() = buildValue.toString()

    private val buildValue = StringBuilder()

    internal fun update(ch: Char) {
        buildValue.append(ch)
    }

    override fun toString(): String {
        return "{'$value', '$type'}"
    }

    val isEmpty: Boolean
    get() = buildValue.isEmpty()

    fun dropLast() {
        buildValue.dropLast(1)
    }
}