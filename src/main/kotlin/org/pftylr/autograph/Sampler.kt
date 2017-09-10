package org.pftylr.autograph

abstract class Sampler {

    abstract fun newNames(strs: List<String>)

    abstract fun newValues(nums: List<Double>)

}