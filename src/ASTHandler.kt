/**
 * ASTノードを管理するためのユーティリティクラス
 */
class ASTHandler {

    /**
     * 管理しているASTノードのリスト
     * @property nodes ASTノードのリスト
     */
    private val nodes = mutableListOf<ASTNode>()

    /**
     * ASTノードを追加する
     * @param node 追加するノード
     */
    fun addNode(node: ASTNode) {
        nodes.add(node)
    }
}