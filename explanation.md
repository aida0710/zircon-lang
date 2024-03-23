```zircon
echo "hello world";
```

**字句解析**
字句解析では、入力の文字列を意味のある最小単位(トークン)に分割します。
以下が字句解析の結果として得られるトークン例です。

- `echo`
- `"hello world"`
- `;`

ここで、`echo`はプログラム内で定義された予約語、`"hello world"`は文字列リテラル、`;`は文の終端を示す記号として認識されます。

実際の出力
```console
Tokens: [Symbol(string=echo), SequenceStart(string=(), String(string=Hello World!), SequenceEnd(string=))]
```

**構文解析**
構文解析では、字句解析で得られたトークン列を言語の文法ルールに基づいて解析し、構文木(Abstract Syntax Tree)を生成します。

```plaintext
            PrintStatement
            /            \
        echo            StringLiteral
                         /
                   "hello world"
```
`PrintStatement`ノードは「出力文」を表し、その子ノードとしてキーワード`echo`と、出力する文字列リテラル`"hello world"`が格納されます。

```console
Parsed expression: [Sequence(openSymbol=echo, children=[String(value=Hello World!)])]
```

インタープリターはこの構文木を基に、意味解析や実行処理を行っていきます。

```console
Interpreting expression: Sequence(openSymbol=echo, children=[String(value=Hello World!)])
Hello World!
```