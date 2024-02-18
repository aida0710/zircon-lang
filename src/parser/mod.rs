#[derive(Debug)]
enum Statement {
    Let(String, i32),
    Print(Expression),
    Conditional(Box<Expression>, Box<Statement>, Box<Statement>),
}

#[derive(Debug)]
enum Expression {
    Identifier(String),
    Literal(String),
}

pub(crate) struct Parser {
    pub(crate) tokens: Vec<String>,
}

impl Parser {
    pub(crate) fn lexer_parse(&mut self) -> Vec<String> {
        let mut parser: Vec<String> = Vec::new();
        let mut ast = Vec::new();
        let tokens = &mut self.tokens.iter();

        while let Some(token) = tokens.next() {
            match token.as_str() {
                "let" => {
                    let var_name = match tokens.next() {
                        Some(name) => name.to_string(),
                        None => break,
                    };
                    assert_eq!(tokens.next().map(String::as_str), Some("="));
                    let value = match tokens.next() {
                        Some(val) => {
                            match val.trim().parse::<i32>() {
                                Ok(v) => v,
                                Err(_) => {
                                    eprintln!("Failed to parse value to i32: {}", val);
                                    break;
                                }
                            }
                        }
                        None => break,
                    };
                    ast.push(Statement::Let(var_name, value));
                    assert_eq!(tokens.next().map(String::as_str), Some(";"));
                }
                "print" => {
                    assert_eq!(tokens.next().map(String::as_str), Some("("));
                    let var_name = match tokens.next() {
                        Some(x) if x.starts_with("\"") => Expression::Literal(x.trim_matches('"').to_owned()),
                        Some(name) => Expression::Identifier(name.to_string()),
                        None => break,
                    };
                    assert_eq!(tokens.next().map(String::as_str), Some(")"));
                    ast.push(Statement::Print(var_name));
                    assert_eq!(tokens.next().map(String::as_str), Some(";"));
                }
                _ => parser.push(token.to_string()),
            }
        }
        println!("{:#?}", ast);
        parser
    }
}