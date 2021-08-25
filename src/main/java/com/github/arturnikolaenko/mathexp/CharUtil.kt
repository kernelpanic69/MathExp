package com.github.arturnikolaenko.mathexp

private val operators = arrayOf('-', '+', '*', '/', '^')

fun Char.isOperator(): Boolean {
    return this in operators
}

fun Char.isDecimalSeparator(): Boolean {
    return this == '.'
}

fun Char.isOpenBracket(): Boolean {
    return this == '('
}

fun Char.isCloseBracket(): Boolean {
    return this == ')'
}

fun Char.isSign(): Boolean {
    return this == '-' || this == '+'
}