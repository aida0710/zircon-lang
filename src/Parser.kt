class Parser(private val input: List<Tokenizer.Token>, private val astHandler: ASTHandler = ASTHandler()) {
    // 現在の解析位置
    private var pos = 0
    // エラーメッセージ
    private var errorMessage: String? = null

    // 全てのトークンを解析する
    fun parseAll(): List<ASTNode?> {
        val results = mutableListOf<ASTNode?>()
        // トークンがなくなるまで解析を続ける
        while (pos < input.size) {
            val node = parse()
            if (node != null) {
                // 解析結果のノードを追加する
                astHandler.addNode(node)
                results.add(node)
            } else if (errorMessage != null) {
                // エラーメッセージがある場合は例外を投げる
                throw RuntimeException("Parse error: $errorMessage")
            }
        }
        return results
    }

    // トークンを解析する
    private fun parse(): ASTNode? {
        if (pos < input.size) {
            return when (val token = input[pos++]) { // 数字のトークンを解析する
                is Tokenizer.Token.Number -> ASTNode.Number(token.string.toInt()) // 文字列のトークンを解析する
                is Tokenizer.Token.String -> ASTNode.String(token.string) // 演算子のトークンを解析する
                is Tokenizer.Token.Operator -> ASTNode.Operator(token.string) // シンボルのトークンを解析する
                is Tokenizer.Token.Symbol -> {
                    return when {
                        pos < input.size && input[pos] is Tokenizer.Token.Operator && (input[pos] as Tokenizer.Token.Operator).string == "=" -> {
                            // "set" コマンドの変数名として扱う
                            val variableName = token.string
                            pos++
                            val value = parse()
                            if (value != null) {
                                ASTNode.Sequence("set", listOf(ASTNode.Symbol(variableName), value))
                            } else {
                                errorMessage = "Missing value for set command"
                                null
                            }
                        }
                        pos < input.size && input[pos] is Tokenizer.Token.SequenceStart -> {
                            // シーケンスの開始トークンが続く場合は、引数を解析する
                            pos++
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
                                null
                            } else {
                                pos++
                                ASTNode.Sequence(token.string, children)
                            }
                        }
                        else -> {
                            ASTNode.Symbol(token.string)
                        }
                    }
                }
                // シーケンス開始のトークンを解析する
                is Tokenizer.Token.SequenceStart -> {
                    val children = mutableListOf<ASTNode>()
                    // シーケンスの終了トークンが出るまで解析を続ける
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
                    return ASTNode.Sequence(token.string, children)
                }
                else -> {
                    errorMessage = "Unknown token type: $token"
                    null
                }
            }
        } else {
            if (errorMessage != null) {
                throw RuntimeException("Parse error: $errorMessage")
            }
            return null
        }
    }
}