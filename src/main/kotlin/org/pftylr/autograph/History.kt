package org.pftylr.autograph

class History(val maxSize: Int) : LinkedHashMap<Int, List<Double>>(maxSize) {

    override fun removeEldestEntry(eldest: MutableMap.MutableEntry<Int, List<Double>>?): Boolean  {
        return size > maxSize
    }
}