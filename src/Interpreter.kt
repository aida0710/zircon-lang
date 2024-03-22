class Interpreter : ASTNodeVisitor {
    private val variables = mutableMapOf<String, ASTNode>()
    private val commandExecutor = CommandExecutor(variables)

    override fun visit(node: ASTNode.Number): ASTNode = node

    override fun visit(node: ASTNode.String): ASTNode = node

    override fun visit(node: ASTNode.Sequence): ASTNode? {
        val commandNode = node.children.firstOrNull() as? ASTNode.Symbol
            ?: return null

        return when (commandNode.name) {
            "echo" -> {
                val argument = node.children.getOrNull(1)?.accept(this)
                println(argument)
                null
            }
            "set" -> commandExecutor.executeSetCommand(node, this)
            "if" -> commandExecutor.executeIfCommand(node, this)
            "loop" -> commandExecutor.executeLoopCommand(node, this)
            else -> {
                node.children.forEach { it.accept(this) }
                null
            }
        }
    }

    override fun visit(node: ASTNode.Operator): ASTNode? = null

    override fun visit(node: ASTNode.Symbol): ASTNode? = variables[node.name]
}
