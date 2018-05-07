package org.littlegit.core.util

class BiMap<Key, Value> {
    private val keyValue = HashMap<Key, Value>()
    private val valueKey = HashMap<Value, Key>()
    val keys = mutableSetOf<Key>()

    val size: Int; get() = keyValue.size

    fun get(key: Key): Value? {
        return keyValue[key]
    }

    fun getKey(value: Value): Key? {
        return valueKey[value]
    }

    fun put(key: Key, value: Value) {
        keyValue[key] = value
        valueKey[value] = key
        keys.add(key)
    }

    fun contains(key: Key): Boolean {
        return keyValue.containsKey(key)
    }

    fun containsVal(value: Value): Boolean {
        return valueKey.contains(value)
    }

    fun remove(key: Key) {
        val value = keyValue.remove(key)
        value?.let { valueKey.remove(it) }
        keys.remove(key)
    }
}