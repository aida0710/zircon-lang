class Interpreter : ASTNodeVisitor {
    private val variables = mutableMapOf<String, ASTNode>()

    override fun visit(node: ASTNode.Number): ASTNode {
        println("Visiting Number node: $node")
        return node
    }

    override fun visit(node: ASTNode.String): ASTNode {
        println("Visiting String node: $node")
        return node
    }

    override fun visit(node: ASTNode.Symbol): ASTNode? {
        println("Visiting Symbol node: $node")
        return when (node.name) {
            "echo" -> {
                val sequenceNode = node as? ASTNode.Sequence
                if (sequenceNode != null && sequenceNode.children.isNotEmpty()) {
                    val valuesNode = sequenceNode.children[0]
                    valuesNode.accept(this)
                } else {
                    null
                }
            }

            "set" -> {
                val sequenceNode = node as? ASTNode.Sequence
                if (sequenceNode != null && sequenceNode.children.size >= 2) {
                    val variableNameNode = sequenceNode.children[0] as? ASTNode.Symbol
                    val variableValueNode = sequenceNode.children[1]

                    if (variableNameNode != null) {
                        variables[variableNameNode.name] = variableValueNode.accept(this) ?: return null
                    } else {
                        throw RuntimeException("Syntax Error: Expected a valid variable to set.")
                    }
                }
                null
            }

            "if" -> {
                val sequenceNode = node as? ASTNode.Sequence
                if (sequenceNode != null && sequenceNode.children.size >= 3) {
                    val conditionNode = node.children[0]
                    val thenNode = node.children[1]
                    val elseNode = node.children[2]

                    val conditionResult = conditionNode.accept(this)
                    if (conditionResult is ASTNode.Number && conditionResult.value != 0) {
                        thenNode.accept(this)
                    } else {
                        elseNode.accept(this)
                    }
                } else {
                    null
                }
            }

            else -> null
        }
    }

    override fun visit(node: ASTNode.Operator): ASTNode? {
        println("Visiting Operator node: $node")
        return null
    }

    override fun visit(node: ASTNode.Sequence): ASTNode? {
        println("Visiting Sequence node: $node")
        var result: ASTNode? = null
        for (childNode in node.children) {
            result = childNode.accept(this)
        }
        return result
    }
}