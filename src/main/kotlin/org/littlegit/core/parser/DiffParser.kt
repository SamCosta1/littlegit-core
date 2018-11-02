import java.nio.file.Path
import java.nio.file.Paths
    fun parse(lines: List<String>, repoPath: Path): Diff {
                fileDiffs.add(parseFileDiff(lines, startIndex, endIndex, repoPath))
    private fun parseFileDiff(lines: List<String>, startIndex: Int, endIndex: Int, repoPath: Path): FileDiff {
            return parseEmptyFile(lines, startIndex, repoPath)
        val aFilePathStr = lines[aFilePathIndex].removePrefix("--- ")
        val bFilePathStr = lines[bFilePathIndex].removePrefix("+++ ")
        val aFilePath = Paths.get(repoPath.toString(), stripQuotesIfNeeded(aFilePathStr).removePrefix("a/"))
        val bFilePath = Paths.get(repoPath.toString(), stripQuotesIfNeeded(bFilePathStr).removePrefix("b/"))
            aFilePathStr == "/dev/null" -> FileDiff.NewFile(bFilePath, hunks)
            bFilePathStr == "/dev/null" -> FileDiff.DeletedFile(aFilePath, hunks)
    private fun parseEmptyFile(lines: List<String>, startIndex: Int, repoPath: Path): FileDiff {
            return FileDiff.RenamedFile(Paths.get(repoPath.toString(), fromPath), Paths.get(repoPath.toString(), toPath), emptyList())
                FileDiff.NewFile(Paths.get(repoPath.toString(), path), emptyList())
                FileDiff.DeletedFile(Paths.get(repoPath.toString(), path), emptyList())