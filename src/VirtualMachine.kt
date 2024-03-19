class VirtualMachine(private val code: String) {
    fun run() {
        val tokenizer = Tokenizer(code)
        val tokens = tokenizer.tokenize()

        val parser = Parser(tokens)
        val expr = parser.parse()

        val interpreter = Interpreter()
        val result = interpreter.eval(expr)

        println(result)
    }
}