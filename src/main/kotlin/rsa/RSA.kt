package rsa

import bits.toBits

class RSA {

    private var pulicKey: SecretKey
    private var privateKey: SecretKey

    /**
     * 生成密钥对
     * @return 公钥，私钥组成的密钥对
     */
    private fun generateKeyPair(): Pair<SecretKey, SecretKey> {
        val a = generatePrime()
        var b = generatePrime()
        while (b == a) {
            b = generatePrime()
        }
//        println("a=$a b=$b")

        val n = a * b

        val l = (a - 1) * (b - 1)

        var e = 1L
        for (i in 2 until l) {
            if (gcd(i, l) == 1L) {
                e = i
                break
            }
        }

        var d = 1L
        for (i in 2 until l) {
            if ((i * e) % l == 1L) {
                d = i
                break
            }
        }

        return Pair(SecretKey(n, e), SecretKey(n, d))
    }

    init {
        val keyPair = generateKeyPair()
        pulicKey = keyPair.first
        privateKey = keyPair.second
    }

    /**
     * 加密,对一个十进制整数进行rsa加密
     * @param plaintext 一个整型数字
     * @return 加密后返回的数字
     */
    private fun encrypt(plaintext: Int): Int {
        return quickPower(plaintext.toLong(), pulicKey.cry, pulicKey.num).toInt()
    }

    /**
     * 加密,将每个字符的Unicode值进行加密后转成十六进制字符串，最后将所有结果拼接
     * @param plaintext 明文字符串
     * @return 加密后的字符串
     */
    fun encrypt(plaintext: String): String {
        var res = ""
        plaintext.forEach {
//            println("$it:${it.toInt()}:${encrypt(it.toInt())}:${encrypt(it.toInt()).toBits().toHex()}")
            res = res.plus(encrypt(it.toInt()).toBits().toHex())
        }
        return res.toUpperCase()
    }

    /**
     * 解密，对一个十进制整数进行rsa解密
     * @param cypherText 一个整型数字
     * @return 解密后返回的数字
     */
    private fun decrypt(cypherText: Int): Int {
        return quickPower(cypherText.toLong(), privateKey.cry, privateKey.num).toInt()
    }

    /**
     *  解密，将传入的字符串每8位进行分割，每8位16进制转成十进制后进行解密，最后将结果进行拼接
     *  @param cypherText 要解密的字符串
     *  @return 解密后返回的明文
     */
    fun decrypt(cypherText: String): String {
        var num = ""
        var res = ""
        for (i in cypherText.indices step 8) {
            for (j in 0 until 8) {
                num = num.plus(cypherText[i + j])
            }
            val dig = Integer.parseInt(num, 16)
            val digCy = decrypt(dig)
            val char = digCy.toChar()
//            println("$num:$dig:$digCy:$char")
            res += char
            num = ""
        }

        return res
    }

    /**
     * 签名
     */
    fun sign(info: String): String {
        var res = ""
        info.forEach {
//            println("$it:${it.toInt()}:${encrypt(it.toInt())}:${encrypt(it.toInt()).toBits().toHex()}")
            res = res.plus(decrypt(it.toInt()).toBits().toHex())
        }
        return res.toUpperCase()
    }

    /**
     * 验签
     */
    fun verify(info: String): String {
        var num = ""
        var res = ""
        for (i in info.indices step 8) {
            for (j in 0 until 8) {
                num = num.plus(info[i + j])
            }
            val dig = Integer.parseInt(num, 16)
            val digCy = encrypt(dig)
            val char = digCy.toChar()
//            println("$num:$dig:$digCy:$char")
            res += char
            num = ""
        }

        return res
    }
}