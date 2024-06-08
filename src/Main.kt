import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

fun main(args: Array<String>) {

    if (args.isEmpty()) {
        println("Please insert command [ -c -l -w -m ] <filename>")
        return
    }

    var command = ""
    var fileName = ""
    if(args[0] == "--help") {
        println("list of command : \n " +
                "-c : print number of bytes in a file \n " +
                "-l : print number of lines in a file \n " +
                "-w : print number of words in a file \n " +
                "-m : print number of characters in a file \n " +
                "\"\" : print -l -w -m command ")
        return
    }
    if (args.size > 1 && args[0].contains("-")) {
        command = args[0]
        fileName = args[1]
    } else if (args.size == 1 && args[0].contains("-")) {
        command = args[0]
    } else {
        fileName = args[0]
    }

    when(command) {
        "-c" -> println(readFileSize(fileName))
        "-l" -> println(readFileLines(fileName))
        "-w" -> println(readFileContent(fileName))
        "-m" -> println(readCharArray(fileName))
        "-parse" -> println(readAndParseToJson(fileName))
        else -> println(""+readFileLines(fileName)
                + " " + readFileContent(fileName)
                + " " + readCharArray(fileName))
    }
}

fun readFileContent(fileName: String): Int = readFile(fileName).readText().split("\\s+".toRegex()).size
fun readCharArray(fileName: String): Int = readFile(fileName).readText().toCharArray().size
fun readFileLines(fileName: String): Int = readFile(fileName).readLines().size
fun readFileSize(fileName: String): Int {
    val text = readFile(fileName).use { it.readText() }
    return text.toByteArray(Charsets.UTF_8).size
}
fun readAndParseToJson(fileName: String) {
    val text = readFile(fileName).readText()
    println("json valid : " + parse(text))
}
private fun parse(json: String): Boolean {

    var jsonTrim: String = json
        .replace(" ", "")
        .replace("\r", "")
        .replace("\n", "")
        .trim()

}
//private fun parse(json: String): Boolean {
//
//    var valid = true
//    if(isObject(json)) {
//        var jsonTrim: String = json
//            .replace(" ", "")
//            .replace("\r", "")
//            .replace("\n", "")
//        jsonTrim = jsonTrim.substring(1, jsonTrim.length - 1)
//        val pairs = jsonTrim.split(",")
//        pairs.forEach {
//            val keyValue = it.split(":")
//            if(keyValue.size > 2) {
//                valid = false
//            }
//            if(!isValidString(keyValue[0])) {
//                valid = false
//            }
//            if(isValidString(keyValue[1]) || isValidNumber(keyValue[1]) || isValidBoolean(keyValue[1])) {
//                return@forEach
//            } else {
//                valid = false
//            }
//
//        }
//    }
//    return valid
//}

private fun isValidString(json: String): Boolean {
    val quotes = json.count() { it == '"' }
    return quotes % 2 == 0 && quotes > 0
}

private fun isValidNumber(json: String): Boolean {
    return json.all { it.isDigit() }
}

private fun isValidBoolean(json: String): Boolean {
    return json.equals("true", ignoreCase = true) || json.equals("false", ignoreCase = true)
}
private fun isList(json: String): Boolean {
    return !(json.get(0) == '[' && json.get(json.length-1) == ']')
}

private fun isObject(json: String): Boolean {
    return json.get(0) == '{' && json.get(json.length-1) == '}'
}

fun readFile(fileName: String) : BufferedReader {
    val bufferedReader: BufferedReader
    if(fileName.isBlank()) {
        bufferedReader = BufferedReader(InputStreamReader(System.`in`))
    } else {
        bufferedReader = BufferedReader(InputStreamReader(File(fileName).inputStream()))
    }
    return bufferedReader
}