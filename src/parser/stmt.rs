use crate::lexer::Token;
use crate::parser::expr::{Expr, parse_expr};

#[derive(Debug, PartialEq)]
pub enum Stmt {
    VarDecl(VarDecl),
    IfStmt(IfStmt),
}

#[derive(Debug, PartialEq)]
pub struct VarDecl {
    pub name: String,
    pub initializer: Expr,
}

#[derive(Debug, PartialEq)]
pub struct IfStmt {
    pub condition: Expr,
    pub then_branch: Vec<Stmt>,
    pub else_branch: Option<Vec<Stmt>>,
}

pub fn parse_stmt(tokens: &mut Vec<Token>, current: &mut usize) -> Stmt {
    match tokens.get(*current) {
        Some(Token::Let) => {
            *current += 1;
            match tokens.get(*current) {
                Some(Token::Identifier(_)) => {
                    let name = tokens.get(*current).unwrap().clone();
                    *current += 1;
                    if let Some(Token::Equal) = tokens.get(*current) {
                        *current += 1;
                        let initializer = parse_expr(tokens, current);
                        match name {
                            Token::Identifier(name_str) => {
                                Stmt::VarDecl(VarDecl {
                                    name: name_str,
                                    initializer,
                                })
                            }
                            _ => panic!("Expected an identifier"),
                        }
                    } else {
                        panic!("Expected '='");
                    }
                }
                _ => panic!("Expected an identifier"),
            }
        }
        Some(Token::If) => {
            *current += 1;
            if let Some(Token::OpenParen) = tokens.get(*current) {
                *current += 1;
                let condition = parse_expr(tokens, current);
                if let Some(Token::CloseParen) = tokens.get(*current) {
                    *current += 1;
                    if let Some(Token::OpenBrace) = tokens.get(*current) {
                        let mut then_branch = Vec::new();
                        *current += 1;
                        while let Some(token) = tokens.get(*current) {
                            if token == &Token::CloseBrace {
                                *current += 1;
                                break;
                            }
                            then_branch.push(parse_stmt(tokens, current));
                        }
                        // else clause parsing
                        let mut else_branch = None;
                        if let Some(Token::Else) = tokens.get(*current) {
                            *current += 1;
                            if let Some(Token::OpenBrace) = tokens.get(*current) {
                                *current += 1;  // Skip '{'
                                let mut else_statements = Vec::new();
                                while let Some(token) = tokens.get(*current) {
                                    if token == &Token::CloseBrace {
                                        *current += 1;
                                        break;
                                    }
                                    else_statements.push(parse_stmt(tokens, current));
                                }
                                else_branch = Some(else_statements);
                            } else {
                                panic!("Expected '{{'");
                            }
                        }
                        Stmt::IfStmt(IfStmt {
                            condition,
                            then_branch,
                            else_branch,
                        })
                    } else {
                        panic!("Expected '{{'");
                    }
                } else {
                    panic!("Expected ')'");
                }
            } else {
                panic!("Expected '('");
            }
        }
        _ => panic!("Unexpected token"),
    }
}