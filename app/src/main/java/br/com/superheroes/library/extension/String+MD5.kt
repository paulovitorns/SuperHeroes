package br.com.superheroes.library.extension

import java.math.BigInteger
import java.security.MessageDigest

fun String.md5(): String {
    return String.format(
        "%032x",
        BigInteger(
            1,
            MessageDigest.getInstance("MD5").digest(this.toByteArray(Charsets.UTF_8))
        )
    )
}
