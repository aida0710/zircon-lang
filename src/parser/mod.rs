pub(crate) struct Parser {
    pub(crate) tokens: Vec<String>,
}

impl Parser {
    pub(crate) fn read_lexer(&mut self) -> Vec<String> {
        let mut tokens: Vec<String> = Vec::new();
        for token in &self.tokens {
            tokens.push(token.clone());
        }
        return tokens;
    }
}