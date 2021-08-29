package com.github.arturnikolaenko.mathexp.builder

internal enum class State {
    START,
    NUMBER,
    STRING_LITERAL,
    SIGN,
    OPEN_BRACKET,
    DECIMAL,
    UNIT,
    OPERATOR,
    DECIMAL_BODY,
    CLOSE_BRACKET
}