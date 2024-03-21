fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Main実行ファイルを指定してください。")
        return
    }

    val filePath = args[0]

    val fileLoader = FileLoader(filePath)
    val fileContent = fileLoader.load()

    val vm = VirtualMachine(fileContent)
    vm.run()
}