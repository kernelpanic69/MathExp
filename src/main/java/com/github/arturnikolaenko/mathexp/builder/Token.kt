package com.github.arturnikolaenko.mathexp.builder

import java.lang.StringBuilder

class Token(var type: TokenType) {
    val value: String
        get() = buildValue.toString()

    private var buildValue = StringBuilder()

    internal fun update(ch: Char) {
        buildValue.append(ch)
    }

    override fun toString(): String {
        return "{'$value', '$type'}"
    }

    internal val isEmpty: Boolean
        get() = buildValue.isEmpty()

    internal fun dropLast() {
        if (!isEmpty) {
            buildValue = buildValue.deleteAt(buildValue.lastIndex)
        }
    }
}