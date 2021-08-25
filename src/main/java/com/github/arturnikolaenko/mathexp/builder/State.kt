package com.github.arturnikolaenko.mathexp.builder

internal enum class State(val valid: Boolean = false) {
    START,
    NUMBER(true),
    STRING_LITERAL,
    SIGN,
    OPEN_BRACKET,
    DECIMAL,
    UNIT,
    OPERATOR,
    DECIMAL_BODY(true),
    CLOSE_BRACKET(true)
}