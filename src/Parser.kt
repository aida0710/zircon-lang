class Parser(private val input: List<Tokenizer.Token>, private val astHandler: ASTHandler = ASTHandler()) {
    private var pos = 0
    private var errorMessage: kotlin.String? = null

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

    private fun parse(): ASTNode? {
        if (pos < input.size) {
            return when (val token = input[pos++]) {
                is Tokenizer.Token.Number -> ASTNode.Number(token.string.toInt())
                is Tokenizer.Token.String -> ASTNode.String(token.string)
                is Tokenizer.Token.Operator -> ASTNode.Operator(token.string)
                is Tokenizer.Token.Symbol -> {
                    val children = mutableListOf<ASTNode>()
                    while (pos < input.size && input[pos] !is Tokenizer.Token.SequenceEnd) {
                        val subTree = parse()
                        if (subTree != null) {
                            children.add(subTree)
                        } else if (errorMessage != null) {
                            return null
                        }
                    }
                    ASTNode.Sequence(token.string, children)
                }
                is Tokenizer.Token.SequenceStart -> {
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
                    ASTNode.Sequence(token.string, children)
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