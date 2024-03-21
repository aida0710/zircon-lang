class Interpreter : ASTNodeVisitor {
    // 変数を保持するマップ
    private val variables = mutableMapOf<String, ASTNode>()

    /**
     * Numberノードを訪問する
     */
    override fun visit(node: ASTNode.Number): ASTNode {
        println("Visiting Number node: $node")
        return node
    }

    /**
     * Stringノードを訪問する
     */
    override fun visit(node: ASTNode.String): ASTNode {
        println("Visiting String node: $node")
        return node
    }

    /**
     * Symbolノードを訪問する
     */
    override fun visit(node: ASTNode.Symbol): ASTNode? {
        println("Visiting Symbol node: $node")
        return when (node.name) {
            "echo" -> null; //todo echoコマンドの処理（未実装）
            "set" -> null; //todo setコマンドの処理（未実装）
            "if" -> null; //todo if文の処理（未実装）
            "loop" -> null; //todo loop文の処理（未実装）
            else -> throw RuntimeException("Unknown symbol: ${node.name}")
        }
    }

    /**
     * Operatorノードを訪問する
     */
    override fun visit(node: ASTNode.Operator): ASTNode? {
        println("Visiting Operator node: $node") //todo 演算子の処理を実装する
        return null
    }

    /**
     * Sequenceノードを訪問する
     */
    override fun visit(node: ASTNode.Sequence): ASTNode? {
        println("Visiting Sequence node: $node")
        var result: ASTNode? = null // 子ノードを順に訪問する
        for (childNode in node.children) {
            result = childNode.accept(this)
        }
        return result
    }
}