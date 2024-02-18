use crate::build_in_functions::{BuildInFunctions, BuildInFunctionsTrait};

pub struct Lexer {
    pub(crate) input: String,
}

impl Lexer {
    pub fn read_char(&mut self) -> Vec<String> {
        let result: Vec<char> = self.input.chars().collect::<Vec<char>>();
        let mut lexer: Vec<char> = self.char_filter(result);

        let token: Vec<String> = self.tokenizer(&mut lexer);
        return token;
    }

    //文字をフィルタリング
    fn char_filter(&self, result: Vec<char>) -> Vec<char> {
        let mut filtered: Vec<char> = Vec::new();
        for c in result {
            match c {
                'a'..='z' | 'A'..='Z' | '0'..='9' | '_' | '{' | '}' | ';' | '=' => filtered.push(c),
                _ => continue,
            }
        }
        return filtered;
    }

    //文字を分割
    fn tokenizer(&mut self, lexer: &mut Vec<char>) -> Vec<String> {
        let built_in_functions = BuildInFunctions.get();
        let mut tokens: Vec<String> = Vec::new();
        let mut token: String = String::new();
        for c in lexer {
            match c {
                '{' | '}' | '(' | ')' | ';' | '=' => {
                    if token.len() > 0 {
                        tokens.push(token.clone());
                        token.clear();
                    }
                    tokens.push(c.to_string());
                }
                _ => token.push(*c),
            }
            if built_in_functions.contains(&token) {
                tokens.push(token.clone());
                token.clear();
            }
        }
        return tokens;
    }
}