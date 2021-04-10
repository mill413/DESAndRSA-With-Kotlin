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
    val cipherText = des.encrypt(plainText)
    println("DES加密:")
    println("明文:${plainText}")
//    println("明文:${plainText.toBits()}")
    println("密钥:${key}")
//    println("密钥:${key.toBits()}")
    println("密文:${cipherText}")
    println("DES解密:")
    println("密文:${cipherText}")
    println("密钥:${key}")
    println("明文:${des.decrypt(cipherText)}")

    val keys = arrayListOf(
        (0..99).random().toBits() + (0..99).random().toBits(),
        (0..99).random().toBits() + (0..99).random().toBits(),
        (0..99).random().toBits() + (0..99).random().toBits()
    )
    val tripleDes = TripleDES(keys)
    val tripleCiphertext = tripleDes.encrypt(plainText)
    println("3DES加密:")
    println("明文:${plainText}")
//    println("明文:${plainText.toBits()}")
    println("密钥:${keys}")
//    println("密钥:${key.toBits()}")
    println("密文:${tripleCiphertext}")
    println("3DES解密:")
    println("密文:${tripleCiphertext}")
    println("密钥:${keys}")
    println("明文:${tripleDes.decrypt(tripleCiphertext)}")
}