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
        let mut line = String::new();
        while let Ok(bytes_read) = self.reader.read_line(&mut line) {
            if bytes_read == 0 {
                break;
            }

            // Lexing
            let mut lexer = Lexer::new(&line);
            let tokens = lexer.lex();

            // Parsing
            let mut parser = Parser::new(tokens);
            let stmts = parser.parse();

            // Output
            for stmt in stmts {
                println!("{:?}", stmt);
            }

            line.clear();
        }
    }
}