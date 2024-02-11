use std::env;
use std::fs::File;
use std::io::BufReader;

use crate::utils::error::error;
use crate::virtual_machine::VirtualMachine;

mod parser;
mod lexer;
mod interpreter;
mod utils;
mod virtual_machine;
mod test;

fn main() {
    let args: Vec<String> = env::args().collect();
    if args.len() < 2 {
        error("No source file passed");
    }
    let source_file: &String = &args[1];
    let file: File = File::open(source_file).expect(&format!("File '{}' does not exist", source_file));
    let mut vm = VirtualMachine::new(BufReader::new(file));
    vm.run();
}