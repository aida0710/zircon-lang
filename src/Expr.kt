sealed class Expr {
    data class Sequence(val subtype: String, val exprs: List<Expr>) : Expr()
    data class Number(val value: Int) : Expr()
    data class String(val value: String) : Expr()
    data class Symbol(val value: String) : Expr()
}