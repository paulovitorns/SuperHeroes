package br.com.superheroes.library.retrofit.endpoint

import br.com.superheroes.data.model.ComicDataWrapper
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchHeroComicsEndpoint {

    @GET("v1/public/characters/{characterId}/comics")
    fun searchHeroComics(
        @Path("characterId") characterId: Int,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("apikey") apiKey: String,
        @Query("ts") ts: Long,
        @Query("hash") hash: String
    ): Single<Response<ComicDataWrapper>>
}
