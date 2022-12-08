package org.mtransit.commons

fun <T> MutableList<T>.addAllN(elements: Collection<T>?): Boolean {
    return elements?.let { this.addAll(elements) } ?: false
}

fun <T> MutableList<T>.sortWithAnd(comparator: Comparator<in T>): MutableList<T> {
    this.sortWith(comparator)
    return this
}

fun <T> MutableList<T>.removeAllAnd(predicate: (T) -> Boolean): MutableList<T> {
    this.removeAll(predicate)
    return this
}

fun <T> MutableList<T>.keepFirst(n: Int): MutableList<T> {
    while (this.size > n) {
        this.removeLast()
    }
    return this
}

fun <T> MutableList<T>.keepFirst(n: Int, predicate: (T) -> Boolean): MutableList<T> {
    var nbKeptInList = 0
    val it = this.listIterator()
    while (it.hasNext()) {
        val listItem: T = it.next()
        if (predicate(listItem)
            && nbKeptInList >= n
        ) {
            it.remove()
        } else {
            nbKeptInList++
        }
    }
    return this
}