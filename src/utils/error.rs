use std::process;

pub fn error(message: &str) -> ! {
    eprintln!("Error: {}", message);
    process::exit(1);
}
