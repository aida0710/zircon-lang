use crate::parser::expr::{Expr, Lit};
use crate::parser::stmt::{EchoStmt, IfStmt, Stmt, VarDecl};

pub struct Interpreter;

impl Interpreter {
    pub fn new() -> Self {
        Self {}
    }

    pub fn interpret(&mut self, stmts: Vec<Stmt>) {
        for stmt in stmts {
            self.visit_stmt(stmt);
        }
    }

    fn visit_stmt(&mut self, stmt: Stmt) {
        match stmt {
            Stmt::VarDecl(var_decl) => self.visit_var_decl(var_decl),
            Stmt::IfStmt(if_stmt) => self.visit_if_stmt(if_stmt),
            Stmt::EchoStmt(echo_stmt) => self.visit_echo_stmt(echo_stmt), // 追加
        }
    }

    fn visit_echo_stmt(&mut self, echo_stmt: EchoStmt) {
        let value = self.visit_expr(echo_stmt.expr);
        println!("{:?}", value);
    }

    fn visit_var_decl(&mut self, var_decl: VarDecl) {
        let value = self.visit_expr(var_decl.initializer);
        // ここで変数を環境に保存します
        println!("Variable {} declared with value {:?}", var_decl.name, value);
    }

    fn visit_if_stmt(&mut self, if_stmt: IfStmt) {
        let condition = self.visit_expr(if_stmt.condition);
        // ここで条件を評価し、適切なブランチを実行します
        println!("If statement with condition {:?}", condition);
    }

    fn visit_expr(&mut self, expr: Expr) -> Lit {
        match expr {
            Expr::Identifier(name) => {
                // ここで変数の値を環境から取得します
                println!("Identifier {}", name);
                Lit::Number(0.0) // 仮の値
            }
            Expr::Literal(lit) => lit,
        }
    }
}