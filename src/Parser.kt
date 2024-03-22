/**
 * 構文解析器クラス
 * @property input トークンのリスト
 * @property astHandler ASTハンドラー
 */
class Parser(private val input: List<Tokenizer.Token>, private val astHandler: ASTHandler = ASTHandler()) {
    private var pos = 0
    private var errorMessage: String? = null

    /**
     * 全てのトークンを解析
     * @return 解析結果のASTノードのリスト
     */
    fun parseAll(): List<ASTNode?> {
        val results = mutableListOf<ASTNode?>()
        while (pos < input.size) {
            val node = parse()
            if (node != null) {
                astHandler.addNode(node)
                results.add(node)
            } else if (errorMessage != null) {
                throw RuntimeException("Parse error: $errorMessage")
            }
        }
        return results
    }

    /**
     * 単一のトークンを解析
     * @return 解析結果のASTノード
     */
    private fun parse(): ASTNode? {
        if (pos >= input.size) {
            if (errorMessage != null) {
                throw RuntimeException("Parse error: $errorMessage")
            }
            return null
        }

        return when (val token = input[pos++]) {
            is Tokenizer.Token.Number -> parseNumber(token)
            is Tokenizer.Token.String -> parseString(token)
            is Tokenizer.Token.Operator -> parseOperator(token)
            is Tokenizer.Token.Symbol -> parseSymbol(token)
            is Tokenizer.Token.SequenceStart -> parseSequence(token)
            else -> {
                errorMessage = "Unknown token type: $token"
                null
            }
        }
    }

    /**
     * 数字トークンを解析
     * @param token 数字トークン
     * @return 数字のASTノード
     */
    private fun parseNumber(token: Tokenizer.Token.Number): ASTNode.Number {
        return ASTNode.Number(token.string.toInt())
    }

    /**
     * 文字列トークンを解析
     * @param token 文字列トークン
     * @return 文字列のASTノード
     */
    private fun parseString(token: Tokenizer.Token.String): ASTNode.String {
        return ASTNode.String(token.string)
    }

    /**
     * 演算子トークンを解析
     * @param token 演算子トークン
     * @return 演算子のASTノード
     */
    private fun parseOperator(token: Tokenizer.Token.Operator): ASTNode.Operator {
        return ASTNode.Operator(token.string)
    }

    /**
     * シンボルトークンを解析
     * @param token シンボルトークン
     * @return シンボルのASTノード、またはsetコマンドのシーケンスノード
     */
    private fun parseSymbol(token: Tokenizer.Token.Symbol): ASTNode? {
        return when {
            pos < input.size && input[pos] is Tokenizer.Token.Operator && (input[pos] as Tokenizer.Token.Operator).string == "=" -> {
                pos++
                val value = parse()
                if (value != null) {
                    ASTNode.Sequence("set", listOf(ASTNode.Symbol(token.string), value))
                } else {
                    errorMessage = "Missing value for set command"
                    null
                }
            }

            pos < input.size && input[pos] is Tokenizer.Token.SequenceStart -> parseSequence(token)
            else -> ASTNode.Symbol(token.string)
        }
    }

    /**
     * シーケンストークンを解析
     * @param token シーケンス開始トークン、またはシンボルトークン
     * @return シーケンスのASTノード
     */
    private fun parseSequence(token: Tokenizer.Token): ASTNode? {
        if (token is Tokenizer.Token.Symbol) {
            pos++
        }
        val children = mutableListOf<ASTNode>()
        while (pos < input.size && input[pos] !is Tokenizer.Token.SequenceEnd) {
            val subTree = parse()
            if (subTree != null) {
                children.add(subTree)
            } else if (errorMessage != null) {
                return null
            }
        }
        if (pos >= input.size || input[pos] !is Tokenizer.Token.SequenceEnd) {
            errorMessage = "Unexpected end of input"
            return null
        }
        pos++
        return ASTNode.Sequence(token.value, children)
    }

    /**
     * 算術演算式を解析する
     * @return 算術演算式のASTノード
     */
    private fun parseExpression(): ASTNode? {
        var left = parseTerm()
        while (pos < input.size) {
            val token = input[pos]
            if (token is Tokenizer.Token.Operator && token.value in listOf("+", "-")) {
                pos++
                val right = parseTerm()
                left = ASTNode.BinaryExpression(token.value, left!!, right!!)
            } else {
                break
            }
        }
        return left
    }

    /**
     * 項を解析する
     * @return 項のASTノード
     */
    private fun parseTerm(): ASTNode? {
        var left = parseFactor()
        while (pos < input.size) {
            val token = input[pos]
            if (token is Tokenizer.Token.Operator && token.value in listOf("*", "/")) {
                pos++
                val right = parseFactor()
                left = ASTNode.BinaryExpression(token.value, left!!, right!!)
            } else {
                break
            }
        }
        return left
    }

    /**
     * 因子を解析する
     * @return 因子のASTノード
     */
    private fun parseFactor(): ASTNode? {
        return when (val token = input[pos]) {
            is Tokenizer.Token.Number -> parseNumber(token)
            is Tokenizer.Token.Symbol -> parseSymbol(token)
            is Tokenizer.Token.SequenceStart -> {
                pos++
                val expression = parseExpression()
                if (input[pos] is Tokenizer.Token.SequenceEnd) {
                    pos++
                    expression
                } else {
                    errorMessage = "Missing closing parenthesis"
                    null
                }
            }
            else -> {
                errorMessage = "Unexpected token: $token"
                null
            }
        }
    }

}