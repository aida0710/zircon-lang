class Parser(private val input: List<Tokenizer.Token>) {
    // 現在のトークンの位置を追跡
    private var pos = 0

    // ASTノードのリストを追加
    private var nodes = mutableListOf<ASTNode>()

    // エラーメッセージを保存するためのプロパティ
    private var errorMessage: kotlin.String? = null

    // すべてのトークンを解析し、ASTノードのリストを生成する新たなメソッドを追加
    fun parseAll(): List<ASTNode?> {
        val results = mutableListOf<ASTNode?>()

        while (pos < input.size) {
            val node = parse()
            if (node != null) {
                results.add(node)
            } else if (errorMessage != null) {
                throw RuntimeException("Parse error: $errorMessage")
            }
        }

        return results
    }

    private fun parse(): ASTNode? {
        if (pos < input.size) {
            return when (val token = input[pos++]) { // 数値ノードを生成
                is Tokenizer.Token.Number -> ASTNode.Number(token.string.toInt()) // 文字列ノードを生成
                is Tokenizer.Token.String -> ASTNode.String(token.string) // シンボルノードを生成
                is Tokenizer.Token.Symbol -> ASTNode.Symbol(token.string) // 演算子ノードを生成
                is Tokenizer.Token.Operator -> ASTNode.Operator(token.string) // シーケンス（括弧で囲まれた部分）のパース
                is Tokenizer.Token.SequenceStart -> {
                    val children = mutableListOf<ASTNode>() // 閉じ括弧が来るまでのトークンをパースしてノードに追加
                    while (pos < input.size && input[pos] !is Tokenizer.Token.SequenceEnd) {
                        val subTree = parse()
                        if (subTree != null) {
                            children.add(subTree) // エラーメッセージがある時点で処理を中断
                        } else if (errorMessage != null) {
                            return null
                        }
                    } // 閉じ括弧が来なかった場合のエラーチェック
                    if (pos >= input.size || input[pos] !is Tokenizer.Token.SequenceEnd) {
                        errorMessage = "Unexpected end of input"
                        return null
                    }
                    pos++ // 閉じ括弧の分1つポジションを進める
                    ASTNode.Sequence(token.string, children)
                } // パースに期待しないトークンが現れたときのエラーチェック
                else -> {
                    errorMessage = "Unknown token type: $token"
                    null
                }
            }
        } else {
            if (errorMessage != null) {
                throw RuntimeException("Parse error: $errorMessage")
            }
            return null // トークンの入力が終了
        }
    }

    // ASTノードのリスト内で次のノードを取得するヘルパーメソッドを追加
    fun ASTNode.getNextNode(): ASTNode? {
        val thisIndex = nodes.indexOf(this)
        return if (thisIndex >= 0 && thisIndex < nodes.size - 1) nodes[thisIndex + 1] else null
    }
}

sealed class ASTNode {
    data class Number(val value: Int) : ASTNode() // 数字のASTノード
    data class String(val value: kotlin.String) : ASTNode() // 文字列のASTノード
    data class Symbol(val name: kotlin.String) : ASTNode() // シンボルのASTノード
    data class Operator(val symbol: kotlin.String) : ASTNode() // 演算子のASTノード
    data class Sequence(val openSymbol: kotlin.String, val children: List<ASTNode>) : ASTNode() // シーケンスのASTノード
}