package br.com.superheroes.data.model

import br.com.superheroes.library.extension.md5

fun SignData.generateHash(): String {
    return "${this.ts}${this.pvtKey}${this.apiKey}".md5()
}
