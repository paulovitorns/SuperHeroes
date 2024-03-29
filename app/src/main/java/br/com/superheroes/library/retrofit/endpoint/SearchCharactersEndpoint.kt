package br.com.superheroes.library.retrofit.endpoint

import br.com.superheroes.data.model.CharacterDataWrapper
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchCharactersEndpoint {

    @GET("v1/public/characters")
    fun searchCharacters(
        @Query("nameStartsWith") startWith: String,
        @Query("orderBy") orderBy: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("apikey") apiKey: String,
        @Query("ts") ts: Long,
        @Query("hash") hash: String
    ): Single<Response<CharacterDataWrapper>>

    @GET("v1/public/characters")
    fun searchCharactersWithoutReference(
        @Query("orderBy") orderBy: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("apikey") apiKey: String,
        @Query("ts") ts: Long,
        @Query("hash") hash: String
    ): Single<Response<CharacterDataWrapper>>
}
