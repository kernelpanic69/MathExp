package com.github.arturnikolaenko.mathexp.builder

import com.github.arturnikolaenko.mathexp.*

internal class ExpBuilder {
    val expString: String
        get() = _tokens.joinToString("") { it.value }
    val tokens: List<Token>
        get() = _tokens.toList()

    private var numBrackets = 0
    private var state: State = State.START

    private val token: Token
        get() = _tokens.last()

    private val _tokens = mutableListOf<Token>()

    fun process(ch: Char): Boolean {
        if (ch.isCloseBracket() && numBrackets <= 0) {
            return false
        }

        when (state) {
            State.START -> when {
                ch.isDigit() -> switchState(ch, State.NUMBER, TokenType.NUMBER)

                ch.isLetter() -> switchState(ch, State.FUNCTION_NAME, TokenType.FUNCTION_NAME)

                ch.isSign() -> switchState(ch, State.SIGN, TokenType.SIGN)

                ch.isOpenBracket() -> switchState(ch, State.OPEN_BRACKET, TokenType.OPEN_BRACKET)

                else -> return false
            }

            State.SIGN -> when {
                ch.isDigit() -> switchState(ch, State.NUMBER, TokenType.NUMBER)

                ch.isLetter() -> switchState(ch, State.FUNCTION_NAME, TokenType.FUNCTION_NAME)

                ch.isOpenBracket() -> switchState(ch, State.OPEN_BRACKET, TokenType.OPEN_BRACKET)

                else -> return false
            }

            State.NUMBER -> when {
                ch.isDigit() -> token.update(ch)

                ch.isDecimalSeparator() -> switchState(ch, State.DECIMAL)

                ch.isLetter() -> switchState(ch, State.UNIT, TokenType.UNIT)

                ch.isOperator() -> switchState(ch, State.OPERATOR, TokenType.OPERATOR)

                ch.isCloseBracket() -> switchState(ch, State.CLOSE_BRACKET, TokenType.CLOSE_BRACKET)

                else -> return false
            }

            State.DECIMAL -> when {
                ch.isDigit() -> switchState(ch, State.DECIMAL_BODY)

                else -> return false
            }

            State.DECIMAL_BODY -> when {
                ch.isDigit() -> token.update(ch)

                ch.isOperator() -> switchState(ch, State.OPERATOR, TokenType.OPERATOR)

                ch.isLetter() -> switchState(ch, State.UNIT, TokenType.UNIT)

                ch.isCloseBracket() -> switchState(ch, State.CLOSE_BRACKET, TokenType.CLOSE_BRACKET)

                else -> return false
            }

            State.UNIT -> when {
                ch.isLetter() -> token.update(ch)

                ch.isOperator() -> switchState(ch, State.OPERATOR, TokenType.OPERATOR)

                ch.isCloseBracket() -> switchState(ch, State.OPERATOR, TokenType.OPERATOR)

                else -> return false
            }

            State.OPERATOR -> when {
                ch.isDigit() -> switchState(ch, State.NUMBER, TokenType.NUMBER)

                ch.isLetter() -> switchState(ch, State.FUNCTION_NAME, TokenType.FUNCTION_NAME)

//                ch.isSign() -> switchState(ch, State.SIGN, TokenType.SIGN)

                ch.isOpenBracket() -> switchState(ch, State.OPEN_BRACKET, TokenType.OPEN_BRACKET)

                else -> return false
            }

            State.OPEN_BRACKET -> when {
                ch.isDigit() -> switchState(ch, State.NUMBER, TokenType.NUMBER)

                ch.isLetter() -> switchState(ch, State.FUNCTION_NAME, TokenType.FUNCTION_NAME)

                ch.isSign() -> switchState(ch, State.SIGN, TokenType.SIGN)

                ch.isOpenBracket() -> switchState(ch, State.OPEN_BRACKET, TokenType.OPEN_BRACKET)

                else -> return false
            }

            State.CLOSE_BRACKET -> when {
                ch.isCloseBracket() -> switchState(ch, State.CLOSE_BRACKET, TokenType.CLOSE_BRACKET)

                ch.isOperator() -> switchState(ch, State.OPERATOR, TokenType.OPERATOR)

                else -> return false
            }

            State.FUNCTION_NAME -> when {
                ch.isLetter() -> token.update(ch)

                ch.isOpenBracket() -> switchState(ch, State.OPEN_BRACKET, TokenType.OPEN_BRACKET)

                else -> return false
            }
        }

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

        if (newState != null) {
            if (newState == State.OPEN_BRACKET) {
                addBrackets()
            } else if (newState == State.CLOSE_BRACKET) {
                removeBrackets()
            }

            state = newState
        }
    }

    private fun syncToken(type: TokenType) {
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