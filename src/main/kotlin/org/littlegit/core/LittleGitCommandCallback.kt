package org.littlegit.core

import org.littlegit.core.commandrunner.GitResult

typealias LittleGitCommandCallback<T> = (T?, GitResult) -> Unit