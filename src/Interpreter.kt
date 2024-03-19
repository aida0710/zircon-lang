class Interpreter {
    private val rootEnv = mutableMapOf<String, Any?>()

    fun eval(expr: Parser.Expr): Any? {
        return when (expr.type) {
            "Sequence" -> evalSequence(expr)
            "Number" -> expr.value
            "String" -> expr.value
            "Symbol" -> rootEnv[expr.value]
            else -> throw IllegalArgumentException("Unknown expression type")
        }
    }

    private fun evalSequence(expr: Parser.Expr): Any? {
        if (expr.exprs!!.isEmpty()) return mutableListOf<Any>()
        var value = eval(expr.exprs[0])
        for (i in 1 until expr.exprs.size) {
            val evaluated = eval(expr.exprs[i])
            if (evaluated == null) {
                throw IllegalArgumentException("Function at position $i is undefined")
            }
            if (evaluated !is Function2<*, *, *>) {
                throw IllegalArgumentException("Expected a function at position $i but got ${evaluated.javaClass.name}")
            }
            val func = evaluated as ((Any?, Any?) -> Any)?
            val rExpr = expr.exprs.getOrNull(i + 1)
            value = func?.invoke(value, rExpr?.let { eval(it) })
        }
        return value
    }

    init {
        rootEnv["//"] = { l: Any?, _: Any? -> l }
        rootEnv["+"] = { l: Any?, r: Any? -> (l as Int) + (r as Int) }
        rootEnv["-"] = { l: Any?, r: Any? -> (l as Int) - (r as Int) }
        rootEnv["*"] = { l: Any?, r: Any? -> (l as Int) * (r as Int) }
        rootEnv["/"] = { l: Any?, r: Any? -> (l as Int) / (r as Int) }
        rootEnv["!"] = { _: Any?, r: Any? -> !(r as Boolean) }
        rootEnv["=="] = { l: Any?, r: Any? -> l == r }
        rootEnv["!="] = { l: Any?, r: Any? -> l != r }
        rootEnv["<"] = { l: Any?, r: Any? -> (l as Int) < (r as Int) }
        rootEnv["<="] = { l: Any?, r: Any? -> (l as Int) <= (r as Int) }
        rootEnv[">"] = { l: Any?, r: Any? -> (l as Int) > (r as Int) }
        rootEnv[">="] = { l: Any?, r: Any? -> (l as Int) >= (r as Int) }
        rootEnv["var"] = { _: Any?, r: Any? -> rootEnv[r as String] = null; r }
        rootEnv["="] = { l: Any?, r: Any? -> rootEnv[l as String] = r; r }
        rootEnv["."] = { l: Any?, r: Any? -> (l as Map<*, *>)[r as String] }
        rootEnv[","] = { l: Any?, r: Any? -> (l as MutableList<Any>).apply { r?.let { add(it); Unit } } }
        rootEnv["&&"] = { l: Any?, r: Any? -> (l as Boolean) && (r as Boolean) }
        rootEnv["||"] = { l: Any?, r: Any? -> (l as Boolean) || (r as Boolean) }
        rootEnv["?"] = { l: Any?, r: Any? -> if (l as Boolean) r else null }
        rootEnv[":"] = { l: Any?, r: Any? -> mapOf(l as String to r!!) }
        rootEnv["=>"] = { l: Any?, r: Any? -> mapOf("env" to rootEnv, "argNames" to l, "expr" to r) }
        rootEnv["range"] = { _: Any?, r: Any? -> IntRange(0, r as Int).toList() }
        rootEnv["length"] = { l: Any?, _: Any? -> (l as List<*>).size }
        rootEnv["map"] = { l: Any?, r: Any? ->
            val list = l as List<*>
            val func = rootEnv[r as String] as ((Any?, Any?) -> Any)?
            list.map { func?.invoke(null, it) }
        }
        rootEnv["forEach"] = rootEnv["map"]
        rootEnv["print"] = { _: Any?, r: Any? -> println(r); r }
    }
}