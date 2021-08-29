package com.github.arturnikolaenko.mathexp.evaluator

import com.github.arturnikolaenko.mathexp.builder.ExpressionBuilder
import com.github.arturnikolaenko.mathexp.builder.TokenType
import com.github.arturnikolaenko.mathexp.evaluator.converter.Converter
import com.github.arturnikolaenko.mathexp.evaluator.converter.UnitCategory
import com.github.arturnikolaenko.mathexp.evaluator.tree.Node
import com.github.arturnikolaenko.mathexp.evaluator.tree.Operator
import com.github.arturnikolaenko.mathexp.evaluator.tree.Scalar
import java.util.*
import kotlin.math.*

class Evaluator(val builder: ExpressionBuilder) {
    private var unitCategory: UnitCategory? = null
        private set

    private var unit: String? = null
        private set

    private lateinit var tree: Node

    private val operands = LinkedList<Double>()

    private val priorities = mapOf(
        "+" to 0,
        "-" to 0,
        "*" to 1,
        "/" to 1,
        "^" to 2,
        "%" to 3
    )

    private val operators = mapOf<String, (x: Double, y: Double) -> Double>(
        "+" to { x, y -> x + y },
        "-" to { x, y -> x - y },
        "*" to { x, y -> x * y },
        "/" to { x, y -> x / y },
        "^" to { x, y -> x.pow(y) },
        "%" to { x, y -> (x / y) * 100 },
    )

    private val functions = mapOf<String, (x: Double) -> Double>(
        "cos" to { x -> cos(x) },
        "sin" to { x -> sin(x) },
        "tan" to { x -> tan(x) },
        "ln" to { x -> ln(x) },
        "log10" to { x -> log10(x) },
        "sqrt" to { x -> sqrt(x) },
        "exp" to { x -> exp(x) },
    )

    private val constants = mapOf(
        "pi" to PI,
        "e" to E
    )

    init {
        setUnitCategory()
    }

    private fun setUnitCategory() {
        val token = builder.tokens.findLast { it.type == TokenType.UNIT }

        if (token != null) {
            unit = token.value
            unitCategory = Converter.getCategory(token.value)
        }
    }


    private fun buildTree() {
        var currentPriority = 0
        var lastNode: Operator? = null
        var root: Node? = null

        val numbers = LinkedList<Double>()

        var i = 0
        
        while (i < builder.tokens.size) {
            val token = builder.tokens[i]

            when (token.type) {
                TokenType.NUMBER -> {
                    numbers.push(token.value.toDouble())
                }

                TokenType.UNIT -> {
                    val converted = Converter.convert(numbers.pop(), token.value, unit!!)
                    numbers.push(converted)
                }

                TokenType.CONSTANT -> {
                    numbers.push(constants[token.value])
                }

                TokenType.SIGN -> {
                    if (i < builder.tokens.lastIndex) {
                        numbers.push(builder.tokens[++i].value.toDouble())
                    }
                }

                TokenType.OPERATOR -> {
                    val op = Operator()
                    op.left = Scalar(numbers.pop())
                    op.op = operators[token.value]!!
                    val pr = priorities[token.value] ?: 0

                    if (root == null) {
                        currentPriority = pr
                        root = op
                        lastNode = root
                    } else {
                        if (pr >= currentPriority) {
                            (lastNode as Operator).right = op
                            lastNode = op
                        } else {
                            op.right = root
                        }

                        currentPriority = pr
                    }

                }
            }

            i++
        }


        lastNode?.right = Scalar(numbers.pop())

        if (root == null) {
            root = Scalar(numbers.pop())
        }

        tree = root
    }

    fun evaluate(): Double {
        buildTree()
        return tree.eval()
    }
}