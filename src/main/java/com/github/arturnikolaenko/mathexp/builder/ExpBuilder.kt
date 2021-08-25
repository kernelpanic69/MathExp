package com.github.arturnikolaenko.mathexp.builder

import com.github.arturnikolaenko.mathexp.*

internal class ExpBuilder {
    var expString = ""
    val tokens: List<Token>
        get() = _tokens.toList()

    private var numBrackets = 0
    private var state: State = State.START

    private val token: Token
        get() = _tokens.last()

    private val _tokens = mutableListOf<Token>()

    private val functions = listOf("cos", "sin", "ln", "exp")
    private val constanta = listOf("pi", "e")
    private val units = listOf("m", "km", "mm", "deg", "rad")

    private val characterIndex = mutableListOf<Pair<Char, State>>()

    fun add(ch: Char): Boolean {
        if (ch.isCloseBracket() && numBrackets <= 0) {
            return false
        }

        when (state) {
            State.START -> when {
                ch.isDigit() -> switchState(ch, State.NUMBER, TokenType.NUMBER)

                ch.isLetter() -> switchState(ch, State.STRING_LITERAL, TokenType.UNKNOWN)

                ch.isSign() -> switchState(ch, State.SIGN, TokenType.SIGN)

                ch.isOpenBracket() -> switchState(ch, State.OPEN_BRACKET, TokenType.OPEN_BRACKET)

                else -> return false
            }

            State.SIGN -> when {
                ch.isDigit() -> switchState(ch, State.NUMBER, TokenType.NUMBER)

                ch.isLetter() -> switchState(ch, State.STRING_LITERAL, TokenType.UNKNOWN)

                ch.isOpenBracket() -> switchState(ch, State.OPEN_BRACKET, TokenType.OPEN_BRACKET)

                else -> return false
            }

            State.NUMBER -> when {
                ch.isDigit() -> switchState(ch)

                ch.isDecimalSeparator() -> switchState(ch, State.DECIMAL)

                ch.isLetter() -> switchState(ch, State.STRING_LITERAL, TokenType.UNKNOWN)

                ch.isOperator() -> switchState(ch, State.OPERATOR, TokenType.OPERATOR)

                ch.isCloseBracket() -> switchState(ch, State.CLOSE_BRACKET, TokenType.CLOSE_BRACKET)

                else -> return false
            }

            State.DECIMAL -> when {
                ch.isDigit() -> switchState(ch, State.DECIMAL_BODY)

                else -> return false
            }

            State.DECIMAL_BODY -> when {
                ch.isDigit() -> switchState(ch)

                ch.isOperator() -> switchState(ch, State.OPERATOR, TokenType.OPERATOR)

                ch.isLetter() -> switchState(ch, State.STRING_LITERAL, TokenType.UNKNOWN)

                ch.isCloseBracket() -> switchState(ch, State.CLOSE_BRACKET, TokenType.CLOSE_BRACKET)

                else -> return false
            }

            State.UNIT -> when {
                ch.isLetter() -> switchState(ch)

                ch.isOperator() -> switchState(ch, State.OPERATOR, TokenType.OPERATOR)

                ch.isCloseBracket() -> switchState(ch, State.OPERATOR, TokenType.OPERATOR)

                else -> return false
            }

            State.OPERATOR -> when {
                ch.isDigit() -> switchState(ch, State.NUMBER, TokenType.NUMBER)

                ch.isLetter() -> switchState(ch, State.STRING_LITERAL, TokenType.UNKNOWN)

//                ch.isSign() -> switchState(ch, State.SIGN, TokenType.SIGN)

                ch.isOpenBracket() -> switchState(ch, State.OPEN_BRACKET, TokenType.OPEN_BRACKET)

                else -> return false
            }

            State.OPEN_BRACKET -> when {
                ch.isDigit() -> switchState(ch, State.NUMBER, TokenType.NUMBER)

                ch.isLetter() -> switchState(ch, State.STRING_LITERAL, TokenType.UNKNOWN)

                ch.isSign() -> switchState(ch, State.SIGN, TokenType.SIGN)

                ch.isOpenBracket() -> switchState(ch, State.OPEN_BRACKET, TokenType.OPEN_BRACKET)

                else -> return false
            }

            State.CLOSE_BRACKET -> when {
                ch.isCloseBracket() -> switchState(ch, State.CLOSE_BRACKET, TokenType.CLOSE_BRACKET)

                ch.isOperator() -> switchState(ch, State.OPERATOR, TokenType.OPERATOR)

                else -> return false
            }

            State.STRING_LITERAL -> when {
                ch.isLetter() -> switchState(ch)
                ch.isOperator() -> switchState(ch, State.OPERATOR, TokenType.OPERATOR)
                ch.isOpenBracket() -> switchState(ch, State.OPEN_BRACKET, TokenType.OPEN_BRACKET)
                ch.isCloseBracket() -> switchState(ch, State.CLOSE_BRACKET, TokenType.CLOSE_BRACKET)

                else -> return false
            }
        }

        return true
    }

    fun dropLast(): Boolean {
        if (characterIndex.isEmpty()) {
            return false
        }

        token.dropLast()

        if (token.isEmpty) {
            _tokens.dropLast(1)
        }

        characterIndex.dropLast(1)
        state = characterIndex.last().second

        return true
    }

    private fun switchState(
        ch: Char,
        newState: State? = null,
        tokenType: TokenType? = null
    ) {
        if (tokenType != null) {
            syncToken(tokenType)
        }

        token.update(ch)
        expString += ch

        if (newState != null) {
            characterIndex += ch to newState

            if (newState == State.OPEN_BRACKET) {
                addBrackets()
            } else if (newState == State.CLOSE_BRACKET) {
                removeBrackets()
            }

            state = newState
        } else {
            characterIndex += ch to state
        }
    }

    private fun syncToken(type: TokenType) {
        if (_tokens.isNotEmpty() && token.type == TokenType.UNKNOWN) {
            token.type = when (token.value) {
                in functions -> TokenType.FUNCTION
                in constanta -> TokenType.CONSTANT
                in units -> TokenType.UNIT
                else -> TokenType.UNKNOWN
            }
        }

        _tokens.add(Token(type))
    }

    private fun addBrackets() {
        numBrackets++
    }

    private fun removeBrackets() {
        if (numBrackets > 0) {
            numBrackets--
        }
    }
}