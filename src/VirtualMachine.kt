class VirtualMachine(private val code: String) {
    fun run() {
        val tokenizer = Tokenizer(code)
        val tokens = tokenizer.tokenize()

        println("Tokens: $tokens")

        val parser = Parser(tokens)
        val exprs = parser.parseAll()

        println("Parsed expression: $exprs")

        val interpreter = Interpreter()
        exprs.forEach { expr ->
            val result = expr?.accept(interpreter)
            println("Interpreted result: $result")
        }
    }
}