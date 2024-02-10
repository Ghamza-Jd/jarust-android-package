package com.ghamza.jarust.wrappers

import com.ghamza.jarust.internal.RawJaConfig

class JaConfig(uri: String, apisecret: String? = null, rootNamespace: String? = null) {
    private val rawConfig: RawJaConfig

    init {
        this.rawConfig = RawJaConfig(uri = uri, apisecret = apisecret, rootNamespace = rootNamespace)
    }

    fun intoRaw(): RawJaConfig = this.rawConfig
}
