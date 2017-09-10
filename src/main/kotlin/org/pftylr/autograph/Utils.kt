package org.pftylr.autograph;

fun splitIntoStrings(s: String) : List<String> {
   return s.trim().split("\\s+".toRegex())
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
