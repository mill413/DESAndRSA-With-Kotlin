package rsa

/**
 * 获得n以内的质数
 * @param n 上限
 * @return n以内的质数集合
 */
fun generatePrimes(n: Int = 65535): IntArray {
    val primeList = mutableListOf<Int>()

    val isPrime = Array(n) { true }
    for (i in 2 until n) {
        if (isPrime[i]) {
            primeList.add(i)
        }
        var j = 0
        while (j < primeList.size && i * primeList[j] < n) {
            isPrime[i * primeList[j]] = false
            if (i % primeList[j] == 0) break
            j++
        }
    }
    return primeList.toIntArray()
}

/**
 * 随机生成一个质数
 * @return 一个质数
 */
fun generatePrime(n: Int = 1000): Long {
    return generatePrimes(n).random().toLong()
}

/**
 * 求a,b的最大公约数
 */
fun gcd(a: Long, b: Long): Long {
    var m = a
    var n = b
    while (n != 0L) {
        val t = m % n
        m = n
        n = t
    }
    return m
}

fun quickPower(a: Long, b: Long, p: Long): Long {
    var ans = 1L
    var base = a
    var power = b
    val mod = p
    while (power > 0) {
        if (power and 1 != 0L) {
            ans = ans * base % mod
        }
        power = power shr 1
        base = base * base % mod
    }

    return ans % mod
}

/**
 * 求n在模mod的情况下的逆元
 */
fun invMod(n: Long, mod: Long): Long {
    return quickPower(n, mod - 2, mod)
}



