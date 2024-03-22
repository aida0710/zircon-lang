/**
 * インタプリタがサポートする各種コマンドの実行ロジックを含むクラス
 */
class CommandExecutor(private val variables: MutableMap<String, ASTNode>) {

    /**
     * echoコマンドを実行する
     * @param node echoコマンドのノード
     * @return 処理結果のノード
     */
    fun executeEchoCommand(node: ASTNode.Sequence, visitor: ASTNodeVisitor): ASTNode? {
        println("Echo command")
        val arguments = node.children.map { child ->
            when (val result = child.accept(visitor)) {
                is ASTNode.String -> result.value
                is ASTNode.Number -> result.value.toString()
                is ASTNode.Symbol -> {
                    when (val value = variables[result.name]) {
                        is ASTNode.String -> value.value
                        is ASTNode.Number -> value.value.toString()
                        null -> throw RuntimeException("Undefined variable: ${result.name}")
                        else -> throw RuntimeException("Invalid variable value for echo command: $value")
                    }
                }
                is ASTNode.Sequence -> {
                    when (val evaluatedSequence = result.accept(visitor)) {
                        is ASTNode.String -> evaluatedSequence.value
                        is ASTNode.Number -> evaluatedSequence.value.toString()
                        else -> throw RuntimeException("Invalid sequence result for echo command: $evaluatedSequence")
                    }
                }
                else -> throw RuntimeException("Invalid argument for echo command: $result")
            }
        }
        println(arguments.joinToString(" "))
        return null
    }

    /**
     * 変数設定コマンドを実行する
     * @param node 変数設定コマンドのノード
     * @param visitor ASTノードビジター
     * @return 処理結果のノード
     */
    fun executeSetCommand(node: ASTNode.Sequence, visitor: ASTNodeVisitor): ASTNode? {
        if (node.children.size != 2) {
            throw RuntimeException("Invalid set command: $node")
        }

        val variableName = (node.children[0] as? ASTNode.Symbol)?.name ?: return null
        val value = node.children[1].accept(visitor) ?: return null

        variables[variableName] = value

        return value
    }
}