/**
 * ASTノードを表す抽象基底クラス
 */
sealed class ASTNode {
    /**
     * ASTノードビジター
     * @property visitor ASTノードビジター
     */
    abstract fun accept(visitor: ASTNodeVisitor): ASTNode?

    /**
     * 数値を表すASTノード
     * @property value 数値
     */
    data class Number(val value: Int) : ASTNode() {
        override fun accept(visitor: ASTNodeVisitor): ASTNode? = visitor.visit(this)
    }

    /**
     * 文字列を表すASTノード
     * @property value 文字列
     */
    data class String(val value: kotlin.String) : ASTNode() {
        override fun accept(visitor: ASTNodeVisitor): ASTNode? = visitor.visit(this)
    }

    /**
     * シンボルを表すASTノード
     * @property name シンボル名
     */
    data class Symbol(val name: kotlin.String) : ASTNode() {
        override fun accept(visitor: ASTNodeVisitor): ASTNode? = visitor.visit(this)
    }

    /**
     * 演算子を表すASTノード
     * @property symbol 演算子
     */
    data class Operator(val symbol: kotlin.String) : ASTNode() {
        override fun accept(visitor: ASTNodeVisitor): ASTNode? = visitor.visit(this)
    }

    /**
     * シーケンスを表すASTノード
     * @property openSymbol 開始シンボル
     * @property children 子ノードのリスト
     */
    data class Sequence(val openSymbol: kotlin.String, val children: List<ASTNode>) : ASTNode() {
        override fun accept(visitor: ASTNodeVisitor): ASTNode? = visitor.visit(this)
    }

    /**
     * 二項演算式を表すASTノード
     * @property operator 演算子
     * @property left 左辺
     * @property right 右辺
     */
    data class BinaryExpression(val operator: kotlin.String, val left: ASTNode, val right: ASTNode) : ASTNode() {
        override fun accept(visitor: ASTNodeVisitor): ASTNode? = visitor.visit(this)
    }
}