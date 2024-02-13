use crate::lexer::Token;
use crate::parser::stmt::ParseError;
use crate::parser::stmt::Stmt;

mod stmt;
mod expr;

pub struct Parser {
    tokens: Vec<Token>,
    current: usize,
}

impl Parser {
    pub fn new(tokens: Vec<Token>) -> Self {
        Self { tokens, current: 0 }
    }

    pub fn parse(&mut self) -> Result<Vec<Stmt>, ParseError> {
        let mut stmts: Vec<stmt> = Vec::new();
        while self.current < self.tokens.len() {
            let stmt: Stmt = stmt::parse_stmt(&mut self.tokens, &mut self.current)?;
            stmts.push(stmt);
        }
        Ok(stmts)
    }
}
