package com.ghamza.jarust

import com.ghamza.jarust.internal.rawJarustInitLogger

class JarustLogger {
    companion object {
        fun initialize() {
            rawJarustInitLogger()
        }
    }
}
