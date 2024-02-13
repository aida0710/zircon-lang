pub use crate::lexer::token::Token;

mod token;

pub struct Lexer {
    input: Vec<char>,
    current: usize,
}

impl Lexer {
    pub fn new(input: &str) -> Self {
        Self {
            input: input.chars().collect(),
            current: 0,
        }
    }

    pub fn lex(&mut self) -> Vec<Token> {
        let mut tokens = Vec::new();
        println!("{:?}", self.input);
        while let Some(&c) = self.input.get(self.current) {
            match c {
                ' ' | '\n' | '\r' | '\t' => self.current += 1,
                '(' => {
                    tokens.push(Token::OpenParen);
                    self.current += 1;
                }
                ')' => {
                    tokens.push(Token::CloseParen);
                    self.current += 1;
                }
                '{' => {
                    tokens.push(Token::OpenBrace);
                    self.current += 1;
                }
                '}' => {
                    tokens.push(Token::CloseBrace);
                    self.current += 1;
                }
                '=' => {
                    tokens.push(Token::Equal);
                    self.current += 1;
                }
                '0'..='9' => {
                    let mut s = String::new();
                    while let Some('0'..='9') = self.input.get(self.current) {
                        s.push(self.input[self.current]);
                        self.current += 1;
                    }
                    tokens.push(Token::Number(s.parse().unwrap()));
                }
                'a'..='z' | 'A'..='Z' => {
                    let mut s = String::new();
                    while let Some('a'..='z' | 'A'..='Z') = self.input.get(self.current) {
                        s.push(self.input[self.current]);
                        self.current += 1;
                    }
                    match s.as_str() {
                        "let" => tokens.push(Token::Let),
                        "echo" => tokens.push(Token::Echo),
                        "if" => tokens.push(Token::If),
                        "else" => tokens.push(Token::Else),
                        _ => tokens.push(Token::Identifier(s)),
                    }
                }
                _ => panic!("Unrecognized character {}", c),
            }
        }
        tokens
    }
}