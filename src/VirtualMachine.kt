class VirtualMachine(private val code: String) {
    private val memory: MutableMap<String, Any> = mutableMapOf()

    fun run() {
        val lines = code.split("\n")
        for (line in lines) {
            execute(line.trim())
        }
    }

    private fun execute(instruction: String) {
        var cleanInstruction = instruction.trim()
        if (cleanInstruction.endsWith(";")) {
            cleanInstruction = cleanInstruction.substring(0, cleanInstruction.length - 1)
        }

        val parts = cleanInstruction.split(" ")
        when (parts[0]) {
            "print" -> {
                val output = parts.drop(1).joinToString(" ").trim('"')
                if (memory.containsKey(output)) {
                    println(memory[output])
                } else {
                    println(output)
                }
            }
            "set" -> memory[parts[1]] = parts.drop(2).joinToString(" ").trim('"')
            else -> throw IllegalArgumentException("Unknown instruction: $cleanInstruction")
        }
    }
}