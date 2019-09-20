package br.com.superheroes.domain.config

import br.com.superheroes.BuildConfig
import br.com.superheroes.library.extension.safeEnumValueOf
import javax.inject.Inject

enum class ConfigName {
    STAGING, PRODUCTION
}

class EnvironmentConfig @Inject constructor() {

    private val configName: ConfigName = safeEnumValueOf(BuildConfig.FLAVOR, ConfigName.PRODUCTION)
    val baseUrl: String
    val apiKey = "656ace3b6053ed496242e3d3f7dca830"

    init {
        baseUrl = when (configName) {
            ConfigName.STAGING -> {
                "https://gateway.marvel.com:443/"
            }
            ConfigName.PRODUCTION -> {
                "https://gateway.marvel.com:443/"
            }
        }
    }
}
