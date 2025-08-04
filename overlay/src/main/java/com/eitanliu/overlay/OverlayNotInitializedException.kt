package com.eitanliu.overlay

class OverlayNotInitializedException : RuntimeException {
    constructor() : this("OverlayInitializer not initialized. Please call OverlayInitializer.init(context) first.")

    constructor(message: String?) : super(message)

    constructor(message: String?, cause: Throwable?) : super(message, cause)

    constructor(
        message: String?, cause: Throwable?, enableSuppression: Boolean, writableStackTrace: Boolean
    ) : super(
        message, cause, enableSuppression, writableStackTrace
    )

    constructor(cause: Throwable?) : super(cause)
}
