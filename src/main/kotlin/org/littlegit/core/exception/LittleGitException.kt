package org.littlegit.core.exception

import org.littlegit.core.commandrunner.GitResult
import org.littlegit.core.model.RemoteBranch

class LittleGitException(val error: GitResult.Error) : Throwable()
class RemoteNotFoundException(val remoteBranchRef: String): Throwable() {
    override val message: String; get() = "No remote was found for remote branch ref $remoteBranchRef"
}