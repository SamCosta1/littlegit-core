package org.littlegit.core

import org.littlegit.core.shell.GitResult

typealias LittleGitCommandCallback<T> = (T?, GitResult) -> Unit