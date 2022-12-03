package org.mtransit.commons

@Suppress("unused")
object CollectionUtils {

    const val EMPTY = Constants.EMPTY

    @JvmStatic
    fun <T> getSize(collection: Collection<T>?): Int = collection?.size ?: 0

    @JvmStatic
    fun <T, V> getSize(map: Map<T, V>?): Int = map?.size ?: 0

    @JvmStatic
    fun <T> sort(list: MutableList<T>?, comparator: Comparator<in T>) {
        list?.sortWith(comparator)
    }

    @JvmStatic
    fun <T> getOrNull(list: List<T>?, index: Int): T? {
        return list?.getOrNull(index)
    }

    @JvmStatic
    fun <T> removeDuplicates(list: List<T>?): List<T>? {
        return list?.distinct()
    }

    @JvmStatic
    fun <T> removeDuplicatesNN(list: List<T>): List<T> {
        return list.distinct()
    }

    @JvmStatic
    fun <T> removeIfNN(list: MutableList<T>, removeIfSelector: (T) -> Boolean): Int {
        val before = list.size
        list.removeAll(removeIfSelector)
        val after = list.size
        return before - after
    }

    @JvmStatic
    fun <T> equalsList(l1: List<T>?, l2: List<T>?): Boolean {
        return l1 == l2
    }
}