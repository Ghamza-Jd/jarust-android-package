package com.ghamza.jarust.wrappers

import android.content.Context
import android.util.Log
import com.ghamza.jarust.internal.RawJaContext

class JaContext(applicationContext: Context, numWorkers: UByte? = null, name: String? = null) {
    private val rawCtx: RawJaContext

    init {
        this.rawCtx = RawJaContext(numWorkers = numWorkers, name = name)
        this.initTls(applicationContext)
        Log.d("Jarust", ": Initialized JaContext")
    }

    companion object {
        init {
            System.loadLibrary("jarust")
            Log.d("Jarust", ": lib loaded")
        }
    }

    private external fun initTls(context: Any)

    fun intoRaw(): RawJaContext = this.rawCtx
}
