package br.com.superheroes.data.search

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchCharactersRequest(
    val namesStartWith: String = "Spider-Man",
    val orderBy: String = "name",
    val limit: Int = 20,
    val offset: Int = 0,
    val apiKey: String,
    val pvtKey: String,
    val ts: Long = 0
) : Parcelable
