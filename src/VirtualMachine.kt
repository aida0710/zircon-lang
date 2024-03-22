/**
 * VirtualMachine クラスの実装。
 * 指定のコードを読み取り、トークンにまとめてパースし、その結果を解釈、実行します。
 * @property code 実行するコード文字列
 */
class VirtualMachine(private val code: String) {

    /**
     * VirtualMachine クラスの実行関数
     */
    fun run() {
        // 指定されたコードをトークンにまとめます
        val tokenizer = Tokenizer(code)
        val tokens = tokenizer.tokenize()

        println("Tokens: $tokens")

        // 生成されたトークンを解析して式を取得します
        val parser = Parser(tokens)
        val exprs = parser.parseAll()

        println("Parsed expression: $exprs")

        // 生成された式を解釈します
        val interpreter = Interpreter()
        exprs.forEach { expr ->
            println("Interpreting expression: $expr")
            expr?.accept(interpreter)
        }
    }
}