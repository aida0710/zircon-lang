fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Main実行ファイルを指定してください。")
        return
    }

    val filePath = args[0]

    val fileLoader = FileLoader(filePath)
    val fileContent = fileLoader.load()

    println(fileContent)

    // TODO: fileContentを解析し、自作言語のコードを実行
    val vm = VirtualMachine(fileContent)
    vm.run()

}