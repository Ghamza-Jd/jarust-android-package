package com.ghamza.jarust.wrappers

import com.ghamza.jarust.internal.RawJaConnection
import com.ghamza.jarust.internal.RawJaConnectionCallback
import com.ghamza.jarust.internal.RawJaSession
import com.ghamza.jarust.internal.rawJarustConnect

class JaConnection(private var ctx: JaContext): RawJaConnectionCallback {
    private var rawConnection: RawJaConnection? = null

    private var onConnectionSuccessCallback: () -> Unit = { }
    private var onConnectionFailureCallback: () -> Unit = { }
    private var onSessionCreationSuccessCallback: (RawJaSession) -> Unit = { }
    private var onSessionCreationFailureCallback: () -> Unit = { }

    public fun connect(config: JaConfig, onSuccess: () -> Unit, onFailure: () -> Unit) {
        this.onConnectionSuccessCallback = onSuccess
        this.onConnectionFailureCallback = onFailure
        rawJarustConnect(ctx = ctx.intoRaw(), config = config.intoRaw(), this)
    }

    public fun createSession(keepAliveInterval: UInt, onSuccess: (JaSession) -> Unit, onFailure: () -> Unit) {
        this.onSessionCreationSuccessCallback = { onSuccess(JaSession(ctx = ctx, rawSession = it)) }
        this.onSessionCreationFailureCallback = onFailure
        this.rawConnection?.create(ctx = ctx.intoRaw(), kaInterval = keepAliveInterval, this)
    }

    override fun onConnectionSuccess(connection: RawJaConnection) {
        this.rawConnection = connection
        this.onConnectionSuccessCallback()
    }

    override fun onConnectionFailure() {
        this.onConnectionFailureCallback()
    }

    override fun onSessionCreationSuccess(session: RawJaSession) {
        this.onSessionCreationSuccessCallback(session)
    }

    override fun onSessionCreationFailure() {
        this.onSessionCreationFailureCallback()
    }
}
