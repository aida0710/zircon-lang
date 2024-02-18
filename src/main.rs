use std::env;
use std::fs::File;
use std::io::{BufReader, Write};

use crate::virtual_machine::VirtualMachine;

mod virtual_machine;
mod test;
mod interpreter;
mod parser;
mod lexer;
mod build_in_functions;
mod error;

fn main() {
    let args: Vec<String> = env::args().collect();
    // 引数が1つ未満の場合はエラーメッセージを表示
    if args.len() < 2 {
        println!("Usage: {} <source_file>", args[0]);
    }
    file_exists(&args[1]);
    let source_file: &String = &args[1];
    let file: File = File::open(source_file).expect(&format!("File '{}' does not exist", source_file));
    let mut vm = VirtualMachine::new(BufReader::new(file));
    vm.run();
}

// ファイルが存在するかどうかを確認し、存在しない場合は新規ファイルを作成する
fn file_exists(file: &str) {
    let file_path = std::path::Path::new(file);
    if file_path.exists() {
        println!("ファイルを読み込みました。");
    } else {
        println!("ファイルが存在しません。新規ファイルを作成します。");
        File::create("main.zr")
            .expect("新規ファイルの作成に失敗しました。")
            .write("print(\"Hello, World!\")".as_bytes())
            .expect("ファイルへの書き込みに失敗しました。");
        println!("新規ファイルを作成しました。");
    }
}
