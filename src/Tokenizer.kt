/**
 * 字句解析器クラス
 * @property input 入力文字列(ファイルデータ)
 */
class Tokenizer(private val input: String) {
    private var pos = 0

    /**
     * トークンを表すシールドクラス
     */
    sealed class Token(val value: kotlin.String) {
        /**
         * 開始括弧を表すトークン
         * @property value 括弧文字列
         */
        data class SequenceStart(val string: kotlin.String) : Token(string)

        /**
         * 終了括弧を表すトークン
         * @property value 括弧文字列
         */
        data class SequenceEnd(val string: kotlin.String) : Token(string)

        /**
         * 数字を表すトークン
         * @property value 数字文字列
         */
        data class Number(val string: kotlin.String) : Token(string)

        /**
         * 文字列を表すトークン
         * @property value 文字列
         */
        data class String(val string: kotlin.String) : Token(string)

        /**
         * シンボルを表すトークン
         * @property value シンボル文字列
         */
        data class Symbol(val string: kotlin.String) : Token(string)

        /**
         * 演算子を表すトークン
         * @property value 演算子文字列
         */
        data class Operator(val string: kotlin.String) : Token(string)

        /**
         * 句読点を表すトークン
         * @property value 句読点文字列
         */
        data class Punctuation(val string: kotlin.String) : Token(string)
    }

    /**
     * 入力文字列をトークン化
     * @return トークンのリスト
     */
    fun tokenize(): List<Token> {
        val tokens = mutableListOf<Token>()

        while (pos < input.length) {
            when {
                input[pos] in "{([" -> tokens.add(Token.SequenceStart(input[pos++].toString()))
                input[pos] in ")}]" -> tokens.add(Token.SequenceEnd(input[pos++].toString()))
                input[pos].isDigit() -> tokens.add(parseNumber())
                input[pos] == '"' -> tokens.add(parseString())
                input[pos].isLetterOrDigit() || input[pos] == '_' -> tokens.add(parseSymbol())
                input[pos] == '=' -> tokens.add(Token.Operator(input[pos++].toString()))
                input[pos] in "+-*/%^<>" -> tokens.add(Token.Operator(input[pos++].toString()))
                input[pos] in ",." -> tokens.add(Token.Punctuation(input[pos++].toString()))
                else -> pos++
            }
        }
        return tokens
    }

    /**
     * 数字トークンを解析
     * @return 数字トークン
     */
    private fun parseNumber(): Token.Number {
        val start = pos
        while (pos < input.length && input[pos].isDigit()) pos++
        return Token.Number(input.substring(start until pos))
    }

    /**
     * 文字列トークンを解析
     * @return 文字列トークン
     */
    private fun parseString(): Token.String {
        pos++
        val start = pos
        while (input[pos] != '"') pos++
        val result = Token.String(input.substring(start until pos))
        pos++
        return result
    }

    /**
     * シンボルトークンを解析
     * @return シンボルトークン
     */
    private fun parseSymbol(): Token.Symbol {
        val start = pos
        while (pos < input.length && (input[pos].isLetterOrDigit() || input[pos] == '_' || input[pos] in "+-*/%^<>")) pos++
        return Token.Symbol(input.substring(start until pos))
    }
}