use crate::lexer::Token;

#[derive(Debug, PartialEq)]
pub enum Lit {
    Number(f64),
}

#[derive(Debug, PartialEq)]
pub enum Expr {
    Identifier(String),
    Literal(Lit),
}

pub fn parse_expr(tokens: &mut Vec<Token>, current: &mut usize) -> Expr {
    match tokens.get(*current) {
        Some(Token::Identifier(x)) => {
            *current += 1;
            Expr::Identifier(x.clone())
        }
        Some(Token::Number(x)) => {
            *current += 1;
            Expr::Literal(Lit::Number(*x))
        }
        _ => panic!("Expected an expression"),
    }
}