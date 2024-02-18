const BUILT_IN_FUNCTIONS: [&str; 11] = [
    "print",
    "let",
    "if",
    "else",
    "while",
    "for",
    "function",
    "return",
    "break",
    "continue",
    "formula"
];

pub(crate) struct BuildInFunctions;

pub(crate) trait BuildInFunctionsTrait {
    fn get(&self) -> Vec<String>;
}

impl BuildInFunctionsTrait for BuildInFunctions {
    fn get(&self) -> Vec<String> {
        BUILT_IN_FUNCTIONS.iter().map(|s| s.to_string()).collect()
    }
}
