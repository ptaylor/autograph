package org.pftylr.autograph

import org.pftylr.autograph.InputStreamDataSource
import org.pftylr.autograph.Sampler

import java.io.InputStream

class InputStreamDataSource constructor(val inputStream: InputStream) {

    fun process(sampler: Sampler) {

        println("is : ${inputStream.toString()}");

      	val reader = inputStream.bufferedReader()
	var line = reader.readLine()
	while (line != null) {
	    val strs = splitIntoStrings(line)
	    var nums = toNumList(strs)
	    if (nums != null) {
	       println("calling newValues ${nums}")
		sampler.newValues(nums)
	    } else {
		sampler.newHeader(strs)
            }
	    line = reader.readLine()
	}
    }
}
