package br.com.superheroes.library.retrofit.endpoint

import br.com.superheroes.data.model.CharacterDataWrapper
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GetCharactersEndPoint {

    @GET("v1/public/characters")
    fun searchCharacters(
        @Query("nameStartsWith") startWith: String,
        @Query("orderBy") orderBy: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("apikey") apiKey: String
    ): Single<Response<CharacterDataWrapper>>
}
