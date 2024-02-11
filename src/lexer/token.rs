#[derive(Debug, Clone, PartialEq)]
pub enum Token {
    If,
    Else,
    Let,
    Identifier(String),
    Equal,
    OpenParen,
    CloseParen,
    OpenBrace,
    CloseBrace,
    Number(f64),
}