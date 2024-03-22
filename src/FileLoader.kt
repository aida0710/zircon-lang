import java.nio.file.Files
import java.nio.file.Paths

class FileLoader(private val filePath: String) {

    fun load(): String {
        val path = Paths.get(filePath)
        return if (Files.exists(path)) {
            Files.readAllLines(path).joinToString("\n")
        } else {
            throw IllegalArgumentException("No such file exists: $filePath")
        }
    }
}