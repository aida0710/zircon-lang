use std::io::BufRead;
use std::io::BufReader;
use std::io::Read;

use crate::lexer::Lexer;
use crate::parser::Parser;

pub struct VirtualMachine<R: Read> {
    reader: BufReader<R>,
}


impl<R: Read> VirtualMachine<R> {
    pub fn new(stream: R) -> Self {
        Self {
            reader: BufReader::new(stream),
        }
    }

    pub fn run(&mut self) {
        let mut line: String = String::new();
        while let Ok(bytes_read) = self.reader.read_line(&mut line) {
            if bytes_read == 0 {
                break;
            }
        }

        println!("file => {}", line);

        // Lexing
        let mut lexer = Lexer { input: line.clone() };
        let tokens: Vec<String> = lexer.read_char();
        println!("lexer => {:?}", tokens);

        // Parsing
        let mut parser = Parser { tokens: tokens.clone() };
        let parser: Vec<String> = parser.lexer_parse();

        println!("parser => {:?}", parser);
    }
}