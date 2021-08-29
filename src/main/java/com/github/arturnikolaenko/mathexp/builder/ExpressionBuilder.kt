package com.github.arturnikolaenko.mathexp.builder

import com.github.arturnikolaenko.mathexp.util.*

/**
 *[ExpressionBuilder] allows building math expression character by character.
 *
 * ### Usage
 *      val builder = ExpressionBuilder()
 *      val expStr = "4+cos(pi/2)"
 *
 *      for (ch in expStr) {
 *          builder.add(ch)
 *      }
 *
 *      println(builder.expString)
 */
class ExpressionBuilder {
    /**
     * Expression string.
     */
    var expString = ""
        private set

    /**
     * Immutable list of tokens found in expression.
     */
    val tokens: List<Token>
        get() = _tokens.toList()

    private var numBrackets = 0
    private var state: State = State.START

    private val token: Token
        get() = _tokens.last()

    private val _tokens = ObservableListWrapper(mutableListOf())

    private val characterIndex = mutableListOf<Pair<Char, State>>()

    /**
     * Adds a character to expression. Returns false if character is not valid in current context.
     * Character won't be added if it is invalid.
     *
     * @param ch character to add
     *
     */
    fun add(ch: Char): Boolean {
        if (ch.isCloseBracket() && numBrackets <= 0) {
            return false
        }

        when (state) {
            State.START -> when {
                ch.isDigit() -> update(ch, State.NUMBER, TokenType.NUMBER)
                ch.isLetter() -> update(ch, State.STRING_LITERAL, TokenType.UNKNOWN)
                ch.isSign() -> update(ch, State.SIGN, TokenType.SIGN)
                ch.isOpenBracket() -> update(ch, State.OPEN_BRACKET, TokenType.OPEN_BRACKET)
                else -> return false
            }

            State.SIGN -> when {
                ch.isDigit() -> update(ch, State.NUMBER, TokenType.NUMBER)
                ch.isLetter() -> update(ch, State.STRING_LITERAL, TokenType.UNKNOWN)
                ch.isOpenBracket() -> update(ch, State.OPEN_BRACKET, TokenType.OPEN_BRACKET)
                else -> return false
            }

            State.NUMBER -> when {
                ch.isDigit() -> update(ch)
                ch.isDecimalSeparator() -> update(ch, State.DECIMAL)
                ch.isLetter() -> update(ch, State.UNIT, TokenType.UNIT)
                ch.isOperator() -> update(ch, State.OPERATOR, TokenType.OPERATOR)
                ch.isCloseBracket() -> update(ch, State.CLOSE_BRACKET, TokenType.CLOSE_BRACKET)
                else -> return false
            }

            State.DECIMAL -> when {
                ch.isDigit() -> update(ch, State.DECIMAL_BODY)
                else -> return false
            }

            State.DECIMAL_BODY -> when {
                ch.isDigit() -> update(ch)
                ch.isOperator() -> update(ch, State.OPERATOR, TokenType.OPERATOR)
                ch.isLetter() -> update(ch, State.STRING_LITERAL, TokenType.UNKNOWN)
                ch.isCloseBracket() -> update(ch, State.CLOSE_BRACKET, TokenType.CLOSE_BRACKET)
                else -> return false
            }

            State.UNIT -> when {
                ch.isLetter() -> update(ch)
                ch.isOperator() -> update(ch, State.OPERATOR, TokenType.OPERATOR)
                ch.isCloseBracket() -> update(ch, State.CLOSE_BRACKET, TokenType.CLOSE_BRACKET)
                else -> return false
            }

            State.OPERATOR -> when {
                ch.isDigit() -> update(ch, State.NUMBER, TokenType.NUMBER)
                ch.isLetter() -> update(ch, State.STRING_LITERAL, TokenType.UNKNOWN)
                ch.isOpenBracket() -> update(ch, State.OPEN_BRACKET, TokenType.OPEN_BRACKET)
                else -> return false
            }

            State.OPEN_BRACKET -> when {
                ch.isDigit() -> update(ch, State.NUMBER, TokenType.NUMBER)
                ch.isLetter() -> update(ch, State.STRING_LITERAL, TokenType.UNKNOWN)
                ch.isSign() -> update(ch, State.SIGN, TokenType.SIGN)
                ch.isOpenBracket() -> update(ch, State.OPEN_BRACKET, TokenType.OPEN_BRACKET)
                else -> return false
            }

            State.CLOSE_BRACKET -> when {
                ch.isCloseBracket() -> update(ch, State.CLOSE_BRACKET, TokenType.CLOSE_BRACKET)
                ch.isOperator() -> update(ch, State.OPERATOR, TokenType.OPERATOR)
                else -> return false
            }

            State.STRING_LITERAL -> when {
                ch.isLetter() -> update(ch)
                ch.isOperator() -> update(ch, State.OPERATOR, TokenType.OPERATOR)
                ch.isOpenBracket() -> update(ch, State.OPEN_BRACKET, TokenType.OPEN_BRACKET)
                ch.isCloseBracket() -> update(ch, State.CLOSE_BRACKET, TokenType.CLOSE_BRACKET)

                else -> return false
            }
        }

        return true
    }

    /**
     * Removes the last character from the expression, rolling its state back.
     *
     * @return false if expression is empty.
     */
    fun dropLast(): Boolean {
        if (characterIndex.isEmpty()) {
            return false
        }

        val chToDrop = characterIndex.last().first

        if (chToDrop.isOpenBracket()) {
            removeBrackets()
        } else if (chToDrop.isCloseBracket()) {
            addBrackets()
        }

        token.dropLast()

        if (token.isEmpty) {
            _tokens.dropLast()
        }

        characterIndex.removeAt(characterIndex.lastIndex)
        state = characterIndex.last().second
        expString = expString.dropLast(1)

        return true
    }

    /**
     * Clears expression and resets its state.
     */
    fun clear() {
        state = State.START
        characterIndex.clear()
        _tokens.clear()
        numBrackets = 0
    }

    fun addString(str: String): Boolean {
        var valid = true

        for (ch in str) {
            valid = add(ch)
        }

        return valid
    }

    private fun update(
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