package org.littlegit.core.util

import java.lang.ref.WeakReference

val <T> T.weak: WeakReference<T>; get() = WeakReference(this)
