class Parser(private val tokens: List<Tokenizer.Token>) {
    private var pos = 0

    data class Expr(val type: String, val subtype: String? = null, val value: Any? = null, val exprs: List<Expr>? = null)

    fun parse(): Expr {
        return parseSequence("").second
    }

    private fun parseSequence(subtype: String): Pair<Int, Expr> {
        val exprs = mutableListOf<Expr>()
        pos++ // Skip the SequenceStart token
        while (pos < tokens.size && tokens[pos] !is Tokenizer.Token.SequenceEnd) {
            val result = parseExpr()
            pos = result.first
            exprs.add(result.second)
        }
        pos++ // Skip the SequenceEnd token
        return Pair(pos, Expr(type = "Sequence", subtype = subtype, exprs = exprs))
    }

    private fun parseExpr(): Pair<Int, Expr> {
        return when (val token = tokens[pos]) {
            is Tokenizer.Token.SequenceStart -> {
                val result = parseSequence(token.string)
                Pair(result.first, result.second)
            }
            is Tokenizer.Token.Number -> Pair(pos + 1, Expr(type = "Number", value = token.string.toInt()))
            is Tokenizer.Token.String -> Pair(pos + 1, Expr(type = "String", value = token.string))
            is Tokenizer.Token.Symbol -> Pair(pos + 1, Expr(type = "Symbol", value = token.string))
            else -> throw IllegalArgumentException("Unknown token type")
        }
    }
}