package org.littlegit.core.shell

class InvalidCommitException(override var message: String = "Commit is malformed", raw: String): Exception("$message: $raw")