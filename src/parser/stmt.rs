use crate::lexer::Token;
use crate::parser::expr::{Expr, parse_expr};

#[derive(Debug, PartialEq)]
pub enum Stmt {
    VarDecl(VarDecl),
    IfStmt(IfStmt),
    EchoStmt(EchoStmt),
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

#[derive(Debug, PartialEq)]
pub struct EchoStmt {
    pub expr: Expr,
}

#[derive(Debug)]
pub(crate) enum ParseError {
    UnexpectedToken,
    ExpectedIdentifier,
    ExpectedEqualSign,
    ExpectedOpenParen,
    ExpectedCloseParen,
    ExpectedOpenBrace,
}

pub(crate) fn parse_stmt(tokens: &mut Vec<Token>, current: &mut usize) -> Result<Stmt, ParseError> {
    let current_token = tokens.get(*current).ok_or(ParseError::UnexpectedToken)?;
    match current_token {
        Token::Let => {
            *current += 1;
            parse_var_decl(tokens, current)
        }
        Token::Echo => {
            *current += 1;
            parse_echo_stmt(tokens, current)
        }
        Token::If => {
            *current += 1;
            parse_if_stmt(tokens, current)
        }
        _ => Err(ParseError::UnexpectedToken),
    }
}

fn parse_echo_stmt(tokens: &mut Vec<Token>, current: &mut usize) -> Result<Stmt, ParseError> {
    let expr = parse_expr(tokens, current);
    Ok(Stmt::EchoStmt(EchoStmt { expr }))
}

fn parse_var_decl(tokens: &mut Vec<Token>, current: &mut usize) -> Result<Stmt, ParseError> {
    let name = match tokens.get(*current).ok_or(ParseError::ExpectedIdentifier)? {
        Token::Identifier(_) => tokens.get(*current).unwrap().clone(),
        _ => return Err(ParseError::ExpectedIdentifier),
    };
    *current += 1;
    match tokens.get(*current).ok_or(ParseError::ExpectedEqualSign)? {
        Token::Equal => *current += 1,
        _ => return Err(ParseError::ExpectedEqualSign),
    };
    let initializer = parse_expr(tokens, current);
    match name {
        Token::Identifier(name_str) => Ok(Stmt::VarDecl(VarDecl { name: name_str, initializer })),
        _ => Err(ParseError::ExpectedIdentifier),
    }
}

fn parse_if_stmt(tokens: &mut Vec<Token>, current: &mut usize) -> Result<Stmt, ParseError> {
    match tokens.get(*current).ok_or(ParseError::ExpectedOpenParen)? {
        Token::OpenParen => *current += 1,
        _ => return Err(ParseError::ExpectedOpenParen),
    };
    let condition = parse_expr(tokens, current);
    match tokens.get(*current).ok_or(ParseError::ExpectedCloseParen)? {
        Token::CloseParen => *current += 1,
        _ => return Err(ParseError::ExpectedCloseParen),
    };
    match tokens.get(*current).ok_or(ParseError::ExpectedOpenBrace)? {
        Token::OpenBrace => *current += 1,
        _ => return Err(ParseError::ExpectedOpenBrace),
    };
    let then_branch = parse_statements_until_close_brace(tokens, current)?;
    let else_branch = if let Some(Token::Else) = tokens.get(*current) {
        *current += 1;
        match tokens.get(*current) {
            Some(Token::OpenBrace) => {
                *current += 1;
                Some(parse_statements_until_close_brace(tokens, current)?)
            }
            _ => None,
        }
    } else {
        None
    };
    Ok(Stmt::IfStmt(IfStmt { condition, then_branch, else_branch }))
}

fn parse_statements_until_close_brace(tokens: &mut Vec<Token>, current: &mut usize) -> Result<Vec<Stmt>, ParseError> {
    let mut statements = Vec::new();
    loop {
        if let Some(token) = tokens.get(*current) {
            if token == &Token::CloseBrace {
                *current += 1;
                break;
            }
        }
        match parse_stmt(tokens, current) {
            Ok(stmt) => statements.push(stmt),
            Err(_) => continue, // ステートメントのパースに失敗した場合、次のステートメントに進む
        };
    }
    Ok(statements)
}