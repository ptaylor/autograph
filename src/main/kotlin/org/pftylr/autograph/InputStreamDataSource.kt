package org.pftylr.autograph

import org.pftylr.autograph.InputStreamDataSource
import org.pftylr.autograph.Sampler

import java.io.InputStream

class InputStreamDataSource(val inputStream: InputStream) {

    fun process(sampler: Sampler) {

      	val reader = inputStream.bufferedReader()
	var line = reader.readLine()
	while (line != null) {
	    val strs = splitIntoStrings(line)
	    if (strs.size > 0) {
	        var nums = toNumList(strs)
		if (nums != null) {
		    sampler.newValues(nums)
		} else {
		    sampler.newNames(strs)
		}
		line = reader.readLine()
            }
	}
    }
}
