package org.pftylr.autograph;

fun splitIntoStrings(s: String) : List<String> {
   val strings = s.trim().split("\\s+".toRegex())
   if (strings.size == 1 && strings[0].length == 0) {
       return listOf<String>()
   } else {
       return strings
   }
}


fun toNumList(s: List<String>) : List<Double>? {

   val d : MutableList<Double> = mutableListOf<Double>()

   try {
       s.forEach { v ->
           d.add(v.toDouble()) 
       }
   } catch (e: NumberFormatException) {
       return null
   }

   return d
}
