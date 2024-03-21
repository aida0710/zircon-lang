interface ASTNodeVisitor {
    fun visit(node: ASTNode.Number): ASTNode?
    fun visit(node: ASTNode.String): ASTNode?
    fun visit(node: ASTNode.Symbol): ASTNode?
    fun visit(node: ASTNode.Operator): ASTNode?
    fun visit(node: ASTNode.Sequence): ASTNode?
}