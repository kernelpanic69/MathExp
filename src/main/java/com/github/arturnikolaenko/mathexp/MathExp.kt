package com.github.arturnikolaenko.mathexp

import com.github.arturnikolaenko.mathexp.builder.ExpressionBuilder
import kotlin.properties.Delegates

class MathExp {
    val displayListener: (old: String, new: String) -> Unit = { _, _ -> }
    private var displayString by Delegates.observable("") { _, old, new ->
        displayListener(old, new)
    }

    private val parser = ExpressionBuilder()


    fun addCharacter(ch: Char) {
        parser.add(ch)
        displayString = parser.expString
    }
}