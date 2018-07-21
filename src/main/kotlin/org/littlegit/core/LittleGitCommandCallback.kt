package org.littlegit.core

import org.littlegit.core.commandrunner.GitResult

data class LittleGitCommandResult<out T>(val data: T?, val result: GitResult)