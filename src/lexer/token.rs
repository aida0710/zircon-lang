#[derive(Debug, Clone, PartialEq)]
pub enum Token {
    If,
    Else,
    Let,
    Echo,
    Identifier(String),
    Equal,
    OpenParen,
    CloseParen,
    OpenBrace,
    CloseBrace,
    Number(f64),
}