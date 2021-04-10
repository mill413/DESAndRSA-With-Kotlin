package DES

import bits.Bits

class TripleDES() {
    var keys = arrayListOf<Bits>()

    constructor(keyList: ArrayList<Bits>) : this() {
        keys = keyList
    }

    constructor(
        key1: Bits,
        key2: Bits,
        key3: Bits
    ) : this() {
        keys = arrayListOf(key1, key2, key3)
    }

    fun encrypt(plainText: Bits): Bits {
        val des1 = DES(keys[0])
        val des2 = DES(keys[1])
        val des3 = DES(keys[2])
        return des3.encrypt(des2.decrypt(des1.encrypt(plainText)))
    }

    fun decrypt(cypherText: Bits): Bits {
        val des1 = DES(keys[0])
        val des2 = DES(keys[1])
        val des3 = DES(keys[2])
        return des1.decrypt(des2.encrypt(des3.decrypt(cypherText)))
    }
}