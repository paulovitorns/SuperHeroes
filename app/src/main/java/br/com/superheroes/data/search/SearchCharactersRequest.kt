package br.com.superheroes.data.search

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchCharactersRequest(
    val namesStartWith: String,
    val orderBy: String = "name",
    val limit: Int = 20,
    val offset: Int,
    val apiKey: String
) : Parcelable
