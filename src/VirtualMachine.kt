class VirtualMachine(private val code: String) {
    fun run() {
        val tokenizer = Tokenizer(code)
        val tokens = tokenizer.tokenize()

        println("Tokens: $tokens")

        val parser = Parser(tokens)
        val expr = parser.parseAll()

        println("Parsed expression: $expr")

        val interpreter = Interpreter()

    }
}