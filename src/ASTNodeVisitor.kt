/**
 * ASTノードを走査するためのビジターインターフェース
 */
interface ASTNodeVisitor {
    /**
     * 数値ノードを処理する
     * @param node 数値ノード
     * @return 処理結果のノード
     */
    fun visit(node: ASTNode.Number): ASTNode?

    /**
     * 文字列ノードを処理する
     * @param node 文字列ノード
     * @return 処理結果のノード
     */
    fun visit(node: ASTNode.String): ASTNode?

    /**
     * シンボルノードを処理する
     * @param node シンボルノード
     * @return 処理結果のノード
     */
    fun visit(node: ASTNode.Symbol): ASTNode?

    /**
     * 演算子ノードを処理する
     * @param node 演算子ノード
     * @return 処理結果のノード
     */
    fun visit(node: ASTNode.Operator): ASTNode?

    /**
     * シーケンスノードを処理する
     * @param node シーケンスノード
     * @return 処理結果のノード
     */
    fun visit(node: ASTNode.Sequence): ASTNode?

    /**
     * 二項演算式ノードを処理する
     * @param node 二項演算式ノード
     * @return 処理結果のノード
     */
    fun visit(node: ASTNode.BinaryExpression): ASTNode?
}