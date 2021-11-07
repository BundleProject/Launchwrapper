package org.bundleproject.launchwrapper.utils

operator fun Array<String>.get(arg: String) = getNullOrNull(indexOf("--$arg").takeIf { it >= 0 }?.plus(1))
fun Array<String>.getNullOrNull(index: Int?): String? {
    if (index == null) return null
    return getOrNull(index)
}