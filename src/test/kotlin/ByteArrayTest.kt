

fun main(args: Array<String>) {
    val a = "hello asdfasdf".toByteArray()

    val b = a.copyOf()

    println(a)

    println(b)
}