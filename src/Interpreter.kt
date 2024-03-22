/**
 * インタプリタクラス。ASTを走査し、各ノードに対応するアクションを実行する
 */
class Interpreter : ASTNodeVisitor {
    private val variables = mutableMapOf<String, ASTNode>()
    private val commandExecutor = CommandExecutor(variables)

    override fun visit(node: ASTNode.Number): ASTNode = node
    override fun visit(node: ASTNode.String): ASTNode = node

    override fun visit(node: ASTNode.Sequence): ASTNode? {
        return when (node.openSymbol) {
            "echo" -> commandExecutor.executeEchoCommand(node, this)
            "set" -> commandExecutor.executeSetCommand(node, this)
            else -> {
                println("Unknown command: $node")
                node.children.forEach { it.accept(this) }
                null
            }
        }
    }

    /**
     * 二項演算式ノードを処理する
     * @param node 二項演算式ノード
     * @return 処理結果のノード
     */
    override fun visit(node: ASTNode.BinaryExpression): ASTNode? {
        val left = node.left.accept(this) as? ASTNode.Number ?: return null
        val right = node.right.accept(this) as? ASTNode.Number ?: return null
        return when (node.operator) {
            "+" -> ASTNode.Number(left.value + right.value)
            "-" -> ASTNode.Number(left.value - right.value)
            "*" -> ASTNode.Number(left.value * right.value)
            "/" -> ASTNode.Number(left.value / right.value)
            else -> throw RuntimeException("Invalid operator: ${node.operator}")
        }
    }

    override fun visit(node: ASTNode.Operator): ASTNode? = null
    override fun visit(node: ASTNode.Symbol): ASTNode? = variables[node.name]
}