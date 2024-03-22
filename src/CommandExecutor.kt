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
        when (val argument = node.children.getOrNull(0)?.accept(visitor)) {
            is ASTNode.String -> println(argument.value)
            is ASTNode.Number -> println(argument.value)
            is ASTNode.Symbol -> {
                when (val value = variables[argument.name]) {
                    is ASTNode.String -> println(value.value)
                    is ASTNode.Number -> println(value.value)
                    null -> throw RuntimeException("Undefined variable: ${argument.name}")
                    else -> throw RuntimeException("Invalid variable value for echo command: $value")
                }
            }
            else -> throw RuntimeException("Invalid argument for echo command: $argument")
        }
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