package br.com.superheroes.data.model

data class SignData(
    val apiKey: String,
    val pvtKey: String,
    val ts: Long = 0
)
