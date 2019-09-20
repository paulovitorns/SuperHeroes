package br.com.superheroes.data.search

class ResultNotFoundException(val queryString: String) : IllegalArgumentException()
