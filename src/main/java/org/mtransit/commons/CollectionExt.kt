package org.mtransit.commons

fun <T> MutableList<T>.addAllN(elements: Collection<T>?): Boolean {
    return elements?.let { this.addAll(elements) } ?: false
}

fun <T> MutableList<T>.addAllNNE(elements: Collection<T?>?): Boolean {
    return elements?.let { addAllNE(elements) } ?: false
}

fun <T> MutableList<T>.addAllNE(elements: Collection<T?>): Boolean {
    return elements.filterNotNull().let { this.addAll(it) }
}

fun <T> MutableList<T>.sortWithAnd(comparator: Comparator<in T>): MutableList<T> {
    this.sortWith(comparator)
    return this
}

fun <T> MutableList<T>.removeAllAnd(predicate: (T) -> Boolean): MutableList<T> {
    this.removeAll(predicate)
    return this
}

fun <T> MutableList<T>.takeAnd(n: Int): MutableList<T> {
    if (n < 0) return this
    while (this.size > n) {
        this.removeAt(lastIndex)
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

inline fun <T> Iterable<T>.dropWhile(minSize: Int = 0, predicate: (T) -> Boolean): List<T> {
    val list = this.toMutableList()
    val it = list.listIterator()
    while (it.hasNext() && list.size > minSize) {
        if (!predicate(it.next())) {
            it.previous()
            break
        }
        it.remove()
    }
    return list
}
