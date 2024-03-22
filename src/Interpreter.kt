class Interpreter : ASTNodeVisitor {
    private val variables = mutableMapOf<String, ASTNode>()
    private val commandExecutor = CommandExecutor(variables)

    override fun visit(node: ASTNode.Number): ASTNode = node

    override fun visit(node: ASTNode.String): ASTNode = node

    override fun visit(node: ASTNode.Sequence): ASTNode? {
        return when (node.openSymbol) {
            "echo" -> {
                println("Echo command")
                val argument = node.children.getOrNull(0)?.accept(this)
                when (argument) {
                    is ASTNode.String -> println(argument.value)
                    is ASTNode.Number -> println(argument.value)
                    is ASTNode.Symbol -> {
                        val value = variables[argument.name]
                        when (value) {
                            is ASTNode.String -> println(value.value)
                            is ASTNode.Number -> println(value.value)
                            null -> throw RuntimeException("Undefined variable: ${argument.name}")
                            else -> throw RuntimeException("Invalid variable value for echo command: $value")
                        }
                    }
                    else -> throw RuntimeException("Invalid argument for echo command: $argument")
                }
                null
            }
            "set" -> {
                if (node.children.size != 2) {
                    throw RuntimeException("Invalid set command: $node")
                }
                commandExecutor.executeSetCommand(node, this)
            }
            else -> {
                println("Unknown command: $node")
                node.children.forEach { it.accept(this) }
                null
            }

        }
    }

    override fun visit(node: ASTNode.Operator): ASTNode? = null

    override fun visit(node: ASTNode.Symbol): ASTNode? = variables[node.name]
}
