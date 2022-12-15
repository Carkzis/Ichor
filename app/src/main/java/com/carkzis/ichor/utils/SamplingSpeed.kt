package com.carkzis.ichor.utils

enum class SamplingSpeed(val descriptor: String) {
    SLOW("Slow"),
    DEFAULT("Default"),
    FAST("Fast"),
    UNKNOWN("");

    override fun toString(): String {
        return descriptor
    }

    companion object {
        fun forDescriptor(value: String): SamplingSpeed {
            return values().first { it.descriptor == value }
        }
    }
}