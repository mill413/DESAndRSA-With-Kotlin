package bits

import kotlin.experimental.and

class Bits(bitSize: Int = 64) {
    var bits: MutableList<Int> = mutableListOf()

    init {
        for (i in 0 until bitSize) {
            bits.add(0)
        }
    }

    var size: Int
        get() {
            return bits.size
        }
        set(value) {

        }

    constructor(externBits: Bits) : this() {
        if (size >= externBits.size) {
            bits.clear()
            externBits.bits.forEach {
                bits.add(it)
            }
        } else {
            bits = externBits.bits
        }
    }

    constructor(bitArray: MutableList<Int>) : this() {
        bits = bitArray
    }

    /**
     * 获取位序列,下标从0开始，左闭右开
     * @param fromIndex 左下标
     * @param toIndex 右下标
     */
    fun subList(fromIndex: Int = 0, toIndex: Int): Bits {
        if (fromIndex > toIndex) throw IndexOutOfBoundsException("fromIndex $fromIndex can't be larger than toIndex ${toIndex}.")
        if (toIndex > bits.size) throw IndexOutOfBoundsException("toIndex $toIndex is out of bound for length ${bits.size}.")

        val res = Bits()
        res.bits = bits.subList(fromIndex, toIndex)
        return res
    }

    override fun toString(): String {
        var returnStr = ""
        bits.forEach {
            returnStr = returnStr.plus(it.toString())
        }
        return returnStr
    }

    operator fun get(index: Int): Int {
        try {
            return this.bits[index]
        } catch (e: IndexOutOfBoundsException) {
            throw IndexOutOfBoundsException("index $index is out of bound for length ${bits.size}.")
        }
    }

    operator fun set(index: Int, value: Int) {
        this.bits[index] = value
    }

    operator fun plus(addend: Bits): Bits {
        val res = Bits()
        res.bits = bits
        res.bits = res.bits.plus(addend.bits).toMutableList()
        return res
    }

    infix fun xor(xorVal: Bits): Bits {
        val resSize =
            if (xorVal.size > size) xorVal.size
            else size

        if (xorVal.size > size) {
            for (i in size until xorVal.size) {
                bits.add(0)
            }
        } else {
            for (i in xorVal.size until size) {
                xorVal.bits.add(0)
            }
        }

        val res = Bits(resSize)
        for (i in (0 until resSize)) {
//            println("${i}-th $resSize $size ${xorVal.size} ${bits[i]} ${xorVal[i]}")
            res[i] = (bits[i] + xorVal[i]) % 2
        }
        return res
    }

    /**
     * 从低位开始获取位序列
     * @param intRange 范围，低位到高位
     */
    operator fun get(intRange: IntRange): Bits {
        val res = Bits(this.bits.subList(intRange.first, intRange.last + 1))
        return res
    }
}

fun Byte.toBits(): Bits {
    val typeLength = 8
    val res = Bits(typeLength)
    val value = this
    for (i in 0 until typeLength) {
        res.bits[i] =
            if ((value and (1 shl i).toByte()) == (1 shl i).toByte())
                1
            else
                0
    }
    return res
}

fun Int.toBits(): Bits {
    val typeLength = 32
    val res = Bits(typeLength)
    val value = this
    for (i in 0 until typeLength) {
        res.bits[i] =
            if ((value and (1 shl i)) == (1 shl i))
                1
            else
                0
    }
    return res
}

fun Long.toBits(): Bits {
    val typeLength = 64
    val res = Bits(typeLength)
    val value = this
    for (i in 0 until typeLength) {
        res.bits[i] =
            if ((value and ((1 shl i).toLong())) == (1 shl i).toLong())
                1
            else
                0
    }
    return res
}

fun Char.toBits(): Bits {
    return this.toByte().toBits()
}

/**
 * 每8个字符转成64位Bits,不足8字符填充
 * @return Bits数组
 */
fun String.toBits(): ArrayList<Bits> {
    var plainText = this
    if (plainText.length % 8 != 0) {
        val remain = 8 - plainText.length % 8
        plainText = plainText.plus(plainText.substring(0, remain))
    }

    var strArr = arrayListOf<String>()
    var tmp = ""
    for (i in plainText.indices) {
        if (i % 8 == 0 && i != 0) {
            strArr.add(tmp)
            tmp = ""
        }
        tmp += plainText[i]
    }
    strArr.add(tmp)
    var bitsArr = arrayListOf<Bits>()
    strArr.forEach { _str ->
        var res = Bits(0)
        _str.forEach {
            res = res.plus(it.toBits())
        }
        bitsArr.add(res)
    }
    return bitsArr
}