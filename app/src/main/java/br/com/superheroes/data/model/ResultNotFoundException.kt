package br.com.superheroes.data.model

class ResultNotFoundException(val queryString: String) : IllegalArgumentException()
