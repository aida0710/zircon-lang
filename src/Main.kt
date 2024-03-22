/**
 * @param args コマンドライン引数。読み込むファイルのパスを含む必要があります。
 * ./main.zr
 */
fun main(args: Array<String>) {
    // ファイルパスが指定されたことを確認
    if (args.isEmpty()) {
        println("Main実行ファイルを指定してください。")
        return
    }

    // ファイルパスを取得
    val filePath = args[0]

    // ファイルをロード
    val fileLoader = FileLoader(filePath)
    val fileContent = fileLoader.load()

    // 仮想マシンを作成し、実行
    val vm = VirtualMachine(fileContent)
    vm.run()
}