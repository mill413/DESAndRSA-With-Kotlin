import rsa.RSA
import java.math.BigInteger
import java.security.MessageDigest

fun main() {
    val rsa = RSA()
    val plaintext = "奚嘉艺"
    val cyphertext = rsa.encrypt(plaintext)
    val decyphertext = rsa.decrypt(cyphertext)

    println(
        """
        加密:
            明文:${plaintext}
            密文:${cyphertext}
        解密:
            密文:${cyphertext}
            明文:${decyphertext}
    """.trimIndent()
    )

    println()
    val md5 = MessageDigest.getInstance("MD5")
    md5.update(plaintext.toByteArray())
    val info = BigInteger(1, md5.digest()).toString(16)
    val signStr = rsa.sign(info)
    val verifyStr = rsa.verify(signStr)

    println(
        """
        签名:
            摘要:$info
            签名:$signStr
        验签:
            签名:$signStr
            摘要:$verifyStr
    """.trimIndent()
    )
//    println(rsa.verify("000042300000477E00000F2C"))
}