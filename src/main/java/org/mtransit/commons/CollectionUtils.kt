package org.mtransit.commons

@Suppress("unused")
object CollectionUtils {

    const val EMPTY = Constants.EMPTY

    @JvmStatic
    fun <T> getSize(collection: Collection<T>?): Int = collection?.size ?: 0

    @JvmStatic
    fun <T, V> getSize(map: Map<T, V>?): Int = map?.size ?: 0

    @JvmStatic
    fun <T : Comparable<T>> sorted(list: Iterable<T>?): List<T>? {
        return list?.toMutableList()?.sorted()
    }

    @JvmStatic
    fun <T> sort(list: MutableList<T>?, comparator: Comparator<in T>) {
        list?.sortWith(comparator)
    }

    @JvmStatic
    fun <T> getOrNull(list: List<T>?, index: Int): T? {
        return list?.getOrNull(index)
    }

    @JvmStatic
    fun <T> removeDuplicates(iterable: Iterable<T>?): List<T>? {
        return iterable?.distinct()
    }

    @JvmStatic
    fun <T> removeDuplicatesNN(iterable: Iterable<T>): List<T> {
        return iterable.distinct()
    }

    @JvmStatic
    fun <T> removeIfNN(list: MutableList<T>, removeIfSelector: (T) -> Boolean): Int {
        val before = list.size
        list.removeAll(removeIfSelector)
        val after = list.size
        return before - after
    }

    @JvmStatic
    fun <T> count(iterable: Iterable<T>?, countSelector: (T) -> Boolean): Int {
        return iterable?.count(countSelector) ?: 0
    }

    @JvmStatic
    fun <T> equalsList(l1: List<T>?, l2: List<T>?): Boolean {
        return l1 == l2
    }

    @JvmStatic
    fun <T> equalsSet(l1: Set<T>?, l2: Set<T>?): Boolean {
        return l1 == l2
    }

    @JvmStatic
    fun <T> equalsCollectionContent(l1: Collection<T>, l2: Collection<T>): Boolean {
        return l1.containsAll(l2) && l2.containsAll(l1)
    }

    @JvmStatic
    fun <T> addAllN(list: MutableList<T>, collection: Collection<T>?) {
        list.addAllN(collection)
    }

    @JvmStatic
    fun <T> addNotNull(collection: MutableCollection<T>, vararg elements: T?) {
        collection.addAll(elements.filterNotNull())
    }

    @JvmStatic
    fun <T> filterN(collection: Iterable<T>?, predicate: (T) -> Boolean): List<T>? {
        return collection?.filter(predicate)
    }

    @JvmStatic
    fun <K, V> putNotNull(collection: MutableMap<K, V>, key: K, value: V?) {
        value ?: return
        collection[key] = value
    }

    @JvmStatic
    fun <K, V> getOrDefault(map: Map<K, V>, key: K, defaultValue: V): V {
        return map.getOrDefault(key, defaultValue) ?: defaultValue
    }

    @JvmStatic
    fun <K, V> MutableMap<K, MutableList<V>>.addMapListValue(key: K, newValue: V) {
        this[key] = this.getOrDefault(key, mutableListOf<V>()).apply {
            add(newValue)
        }
    }

    @JvmStatic
    fun totalMapSize(map: Map<*, List<*>>?): Int {
        return map?.values?.sumOf { it.size } ?: 0
    }
}