class Tokenizer(private val input: String) {
    private var pos = 0

    sealed class Token {
        data class SequenceStart(val string: kotlin.String) : Token() // 開始括弧: (, {, [
        data class SequenceEnd(val string: kotlin.String) : Token() // 終了括弧: ), }, ]
        data class Number(val string: kotlin.String) : Token() // 数字: 123
        data class String(val string: kotlin.String) : Token() // 文字列: "abc"
        data class Symbol(val string: kotlin.String) : Token() // シンボル: abc
        data class Operator(val string: kotlin.String) : Token() // 演算子: +, -, *, /, %, ^, <, >, =
        data class Punctuation(val string: kotlin.String) : Token() // 句読点: ., ,
    }

    fun tokenize(): List<Token> {
        val tokens = mutableListOf<Token>()

        while (pos < input.length) {
            when { // 開始括弧の解析
                "{([".contains(input[pos]) -> tokens.add(Token.SequenceStart(input[pos++].toString())) // 終了括弧の解析
                ")}]".contains(input[pos]) -> tokens.add(Token.SequenceEnd(input[pos++].toString()))
                input[pos].isDigit() -> { // 数字の解析
                    val start = pos
                    while (pos < input.length && input[pos].isDigit()) pos++
                    tokens.add(Token.Number(input.substring(start until pos)))
                }

                input[pos] == '"' -> { // 文字列の解析
                    pos++
                    val start = pos
                    while (input[pos] != '"') pos++
                    tokens.add(Token.String(input.substring(start until pos)))
                    pos++
                }

                input[pos].isLetterOrDigit() || input[pos] == '_' -> {
                    val start = pos
                    while (pos < input.length && (input[pos].isLetterOrDigit() || input[pos] == '_' || "+-*/%^<>=".contains(
                            input[pos]
                        )) && input[pos] != '='
                    ) pos++
                    tokens.add(Token.Symbol(input.substring(start until pos)))
                }

                input[pos] == '=' -> { // Added this line to handle '=' separately
                    tokens.add(Token.Operator(input[pos++].toString()))
                }

                "+-*/%^<>=".contains(input[pos]) -> { // 演算子の解析
                    tokens.add(Token.Operator(input[pos++].toString()))
                }

                ",.".contains(input[pos]) -> { // 句読点の解析
                    tokens.add(Token.Punctuation(input[pos++].toString()))
                }

                else -> pos++ // それ以外の文字は無視
            }
        }
        return tokens
    }
}