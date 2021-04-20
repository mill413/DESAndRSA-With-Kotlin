package rsa

import bits.toBits

class RSA {

    private var pulicKey: SecretKey
    private var privateKey: SecretKey

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

    fun encrypt(plaintext: Int): Int {
        return quickPower(plaintext.toLong(), pulicKey.cry, pulicKey.num).toInt()
    }

    fun encrypt(plaintext: String): String {
        var res = ""
        plaintext.forEach {
//            println("$it:${it.toInt()}:${encrypt(it.toInt())}:${encrypt(it.toInt()).toBits().toHex()}")
            res = res.plus(encrypt(it.toInt()).toBits().toHex())
        }
        return res.toUpperCase()
    }

    fun decrypt(cypherText: Int): Long {
        return quickPower(cypherText.toLong(), privateKey.cry, privateKey.num)
    }

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
}