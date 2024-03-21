sealed class ASTNode {
    abstract fun accept(visitor: ASTNodeVisitor): ASTNode?

    data class Number(val value: Int) : ASTNode() {
        override fun accept(visitor: ASTNodeVisitor): ASTNode? = visitor.visit(this)
    }

    data class String(val value: kotlin.String) : ASTNode() {
        override fun accept(visitor: ASTNodeVisitor): ASTNode? = visitor.visit(this)
    }

    data class Symbol(val name: kotlin.String) : ASTNode() {
        override fun accept(visitor: ASTNodeVisitor): ASTNode? = visitor.visit(this)
    }

    data class Operator(val symbol: kotlin.String) : ASTNode() {
        override fun accept(visitor: ASTNodeVisitor): ASTNode? = visitor.visit(this)
    }

    data class Sequence(val openSymbol: kotlin.String, val children: List<ASTNode>) : ASTNode() {
        override fun accept(visitor: ASTNodeVisitor): ASTNode? = visitor.visit(this)
    }
}