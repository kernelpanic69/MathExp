package com.github.arturnikolaenko.mathexp.evaluator.tree

class Func() : Node {
    lateinit var func: (Double) -> Double
    lateinit var arg: Node

    override fun eval(): Double {
        return func(arg.eval())
    }

    override fun toString(): String {
        return "$func($arg)"
    }
}