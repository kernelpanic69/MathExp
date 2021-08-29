package com.github.arturnikolaenko.mathexp.evaluator.tree

class Operator : Node {
    var left: Node? = null
    var right: Node? = null
    var op: ((Double, Double) -> Double)? = null

    override fun eval(): Double {
        return op?.let { it(right?.eval() ?: .0, left?.eval() ?: .0) } ?: .0
    }

    override fun toString(): String {
        return "$left * $right"
    }
}