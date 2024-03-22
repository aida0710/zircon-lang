class CommandExecutor(private val variables: MutableMap<String, ASTNode>) {
    fun executePrintCommand(node: ASTNode.Sequence, visitor: ASTNodeVisitor): ASTNode? {
        return null
    }

    fun executeSetCommand(node: ASTNode.Sequence, visitor: ASTNodeVisitor): ASTNode? {
        if (node.children.size != 2) {
            throw RuntimeException("Invalid set command: $node")
        }

        val variableName = (node.children[0] as? ASTNode.Symbol)?.name ?: return null
        val value = node.children[1].accept(visitor) ?: return null
        variables[variableName] = value
        return value
    }

/*    fun executeIfCommand(node: ASTNode.Sequence, visitor: ASTNodeVisitor): ASTNode? {
        val condition = node.children.getOrNull(1)?.accept(visitor) as? ASTNode.Number ?: return null
        return if (condition.value != 0) {
            node.children.getOrNull(3)?.accept(visitor)
        } else if (node.children.size >= 6) {
            node.children.getOrNull(5)?.accept(visitor)
        } else {
            null
        }
    }

    fun executeLoopCommand(node: ASTNode.Sequence, visitor: ASTNodeVisitor): ASTNode? {
        val initializationNode = node.children.getOrNull(1) as? ASTNode.Sequence
        val conditionNode = node.children.getOrNull(3) as? ASTNode.Sequence
        val updateNode = node.children.getOrNull(5) as? ASTNode.Sequence
        val bodyNode = node.children.getOrNull(7) as? ASTNode.Sequence

        initializationNode?.accept(visitor)
        var condition = conditionNode?.accept(visitor) as? ASTNode.Number

        var result: ASTNode? = null
        while (condition != null && condition.value != 0) {
            result = bodyNode?.accept(visitor)
            updateNode?.accept(visitor)
            condition = conditionNode?.accept(visitor) as? ASTNode.Number
        }
        return result
    }*/
}