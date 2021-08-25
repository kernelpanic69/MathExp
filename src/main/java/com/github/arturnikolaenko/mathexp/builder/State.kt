package com.github.arturnikolaenko.mathexp.builder

enum class State(val valid: Boolean = false) {
    START,
    NUMBER(true),
    FUNCTION_NAME,
    SIGN,
    OPEN_BRACKET,
    DECIMAL,
    UNIT,
    OPERATOR,
    DECIMAL_BODY(true),
    CLOSE_BRACKET(true)
}