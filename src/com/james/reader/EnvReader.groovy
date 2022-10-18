import groovy.io.FileVisitResult

File topDir = new File('E:\\project\\placeholder-reader\\src\\resource')
// TODO 用于去重，加快效率
LinkedHashMap map = new HashMap<String, List<String>>();

topDir.traverse { file ->
    // 目录文件
    if (file.directory) {
        // 只遍历resource下的文件
        if (file.name != 'resource') {
            return FileVisitResult.TERMINATE
        }
        // mapper文件要排除
        if (file.name == 'mapper') {
            return FileVisitResult.TERMINATE
        }
    }

    // 配置文件则捕获占位符
    if (needCollectPlaceHolder(file)) {
        file.eachLine { line ->
            if (containsPlaceholder(line)) {
                storePlaceholder(line)
            }
        }
    }
    return FileVisitResult.CONTINUE;
}

static def needCollectPlaceHolder(File file) {
    file?.isFile() && file.name =~ /(.*)(\.)(ya?ml|properties|xml)/
}

static def containsPlaceholder(line) {
    line =~ /(?=\$\{)(\$\{)(.*?)(?=})(})/
}

def storePlaceholder(line) {
    // 去掉行的首部空格
    line = line.trim()
    def matchers = line =~ /(?=\$\{)(\$\{)(.*?)(?=})(})/
    matchers.each { matcher -> {
            String original = matcher[0]
            String content = matcher[2]
            def remote = content
            def local = '未设置本地变量'
            if (content.contains(':')) {
                def temp = content.split(':')
                if (temp.size() == 2) {
                    (remote, local) = temp
                } else {
                    remote = remote - ':'
                    local = '本地变量为空字符串'
                }
            }
            println "${line}\t${original}\t${content}\t${remote}\t${local}"
        }
    }
}