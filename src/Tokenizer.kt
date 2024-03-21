class Tokenizer(private val input: String) {
    private var pos = 0

    sealed class Token {
        data class SequenceStart(val string: kotlin.String) : Token() // (, {, [
        data class SequenceEnd(val string: kotlin.String) : Token() // ), }, ]
        data class Number(val string: kotlin.String) : Token() // 123
        data class String(val string: kotlin.String) : Token() // "abc"
        data class Symbol(val string: kotlin.String) : Token() // abc
        data class Operator(val string: kotlin.String): Token() // +, -, *, /, %, ^, <, >, =
        data class Punctuation(val string: kotlin.String) : Token() // ., ,
    }

    fun tokenize(): List<Token> {
        val tokens = mutableListOf<Token>()

        while (pos < input.length) {
            when {
                "{([".contains(input[pos]) -> tokens.add(Token.SequenceStart(input[pos++].toString()))
                ")}]".contains(input[pos]) -> tokens.add(Token.SequenceEnd(input[pos++].toString()))
                input[pos].isDigit() -> {
                    val start = pos
                    while (pos < input.length && input[pos].isDigit()) pos++
                    tokens.add(Token.Number(input.substring(start until pos)))
                }
                input[pos] == '"' -> {
                    pos++
                    val start = pos
                    while (input[pos] != '"') pos++
                    tokens.add(Token.String(input.substring(start until pos)))
                    pos++
                }
                input[pos].isLetterOrDigit() || input[pos] == '_' -> {
                    val start = pos
                    while (pos < input.length && (input[pos].isLetterOrDigit() || input[pos] == '_') ||
                        "+-*/%^<>=".contains(input[pos])) pos++
                    tokens.add(Token.Symbol(input.substring(start until pos)))
                }
                "+-*/%^<>=".contains(input[pos]) -> {
                    tokens.add(Token.Operator(input[pos++].toString()))
                }
                ",.".contains(input[pos]) -> {
                    tokens.add(Token.Punctuation(input[pos++].toString()))
                }
                else -> pos++
            }
        }
        println(tokens)
        return tokens
    }
}