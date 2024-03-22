import java.nio.file.Files
import java.nio.file.Paths

/**
 * ファイルローダークラス
 * 指定のファイルパスからファイルを読み込みます
 * @param filePath
 */
class FileLoader(private val filePath: String) {

    /**
     * ファイルの読み込みを行い、ファイルの内容を文字列として返します
     * @return ファイルの内容の文字列
     * @throws IllegalArgumentException 指定されたファイルパスに該当するファイルが存在しない場合の例外
     */
    fun load(): String {
        val path = Paths.get(filePath)
        return if (Files.exists(path)) {
            Files.readAllLines(path).joinToString("\n")
        } else {
            throw IllegalArgumentException("No such file exists: $filePath")
        }
    }
}