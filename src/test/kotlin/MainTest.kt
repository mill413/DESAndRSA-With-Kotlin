import DES.DES
import DES.TripleDES
import bits.toBits

@ExperimentalStdlibApi
fun main() {
    val plainText = (0..99).random().toBits() + (0..99).random().toBits()
    val key = (0..99).random().toBits() + (0..99).random().toBits()

//    val plainText = "WenShiYi"
//    val key = "XiJiaYi"

//    val des = DES(key.toBits()[0])
    val des = DES(key)
    val cypherText = des.encode(plainText)
    println("DES加密:")
    println("明文:${plainText}")
//    println("明文:${plainText.toBits()}")
    println("密钥:${key}")
//    println("密钥:${key.toBits()}")
    println("密文:${cypherText}")
    println("DES解密:")
    println("密文:${cypherText}")
    println("密钥:${key}")
    println("明文:${des.decode(cypherText)}")

    val keys = arrayListOf(
        (0..99).random().toBits() + (0..99).random().toBits(),
        (0..99).random().toBits() + (0..99).random().toBits(),
        (0..99).random().toBits() + (0..99).random().toBits()
    )
    val tripleDes = TripleDES(keys)
    val tripleCypherText = tripleDes.encode(plainText)
    println("3DES加密:")
    println("明文:${plainText}")
//    println("明文:${plainText.toBits()}")
    println("密钥:${keys}")
//    println("密钥:${key.toBits()}")
    println("密文:${tripleCypherText}")
    println("3DES解密:")
    println("密文:${tripleCypherText}")
    println("密钥:${keys}")
    println("明文:${tripleDes.decode(tripleCypherText)}")
}