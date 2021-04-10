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

    fun encode(plainText: Bits): Bits {
        val des1 = DES(keys[0])
        val des2 = DES(keys[1])
        val des3 = DES(keys[2])
        return des3.encode(des2.decode(des1.encode(plainText)))
    }

    fun decode(cypherText: Bits): Bits {
        val des1 = DES(keys[0])
        val des2 = DES(keys[1])
        val des3 = DES(keys[2])
        return des1.decode(des2.encode(des3.decode(cypherText)))
    }
}