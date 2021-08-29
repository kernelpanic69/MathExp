package com.github.arturnikolaenko.mathexp.builder

typealias ListChangeListener = (list: List<Token>, item: Token, index: Int) -> Unit

class ObservableListWrapper(private val list: MutableList<Token>) {
    var addListener: ListChangeListener? = null
    var deleteListener: ListChangeListener? = null
    var clearListener: ((List<Token>) -> Unit)? = null

    fun add(item: Token) {
        list.add(item)
        addListener?.invoke(list, item, list.lastIndex)
    }

    fun dropLast() {
        val token = list.removeAt(list.lastIndex)
        deleteListener?.invoke(list, token, list.lastIndex + 1)
    }

    fun last() = list.last()
    fun toList() = list.toList()
    fun clear() {
        list.clear()
        clearListener?.invoke(list)
    }

    fun isNotEmpty() = list.isNotEmpty()
}