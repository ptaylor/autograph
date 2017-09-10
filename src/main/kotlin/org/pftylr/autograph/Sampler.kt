package org.pftylr.autograph

abstract class Sampler {

    abstract fun newValues(nums: List<Double>)

    abstract fun newHeader(strs: List<String>)
}