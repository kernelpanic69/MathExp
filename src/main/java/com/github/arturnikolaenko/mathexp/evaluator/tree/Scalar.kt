package com.github.arturnikolaenko.mathexp.evaluator.tree

class Scalar(val value: Double) : Node {
    override fun eval() = value

    override fun toString(): String = value.toString()
}