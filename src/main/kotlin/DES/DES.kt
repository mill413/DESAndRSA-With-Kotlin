package DES

import bits.Bits
import bits.toBits

class DES(key: Bits) {

    /**
     * 64位密钥
     */
    private var keyBits = key

    /**
     * 48位子密钥数组
     */
    private var subKeys = mutableListOf<Bits>()

    /**
     * 第一次置换选择表
     */
    private var firstPermutationChoiceTable = arrayOf(
        57, 49, 41, 33, 25, 17, 9,
        1, 58, 50, 42, 34, 26, 18,
        10, 2, 59, 51, 43, 35, 27,
        19, 11, 3, 60, 52, 44, 36,
        63, 55, 47, 39, 31, 23, 15,
        7, 62, 54, 46, 38, 30, 22,
        14, 6, 61, 53, 45, 37, 29,
        21, 13, 5, 28, 20, 12, 4
    )

    /**
     * 第一次置换选择
     * @param keyText 64位密钥
     * @return 56位置换结果
     */
    private fun permutedChoice1(keyText: Bits): Bits {
        val res = Bits(56)
        var ind = 0
        firstPermutationChoiceTable.forEach {
            res[ind] = keyText[it - 1]
            ind++
        }
        return res
    }

    /**
     * 循环左移表
     */
    private var rotationStep = arrayOf(1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1)

    /**
     * 循环左移
     * @param text 28位左移前序列
     * @param step 循环左移位数
     * @return 28位左移后序列
     */
    private fun rotation(text: Bits, step: Int): Bits {
        val tmp = text.plus(text)
        return tmp[step until step + 28]
    }

    /**
     * 第二次置换选择表
     */
    private var secondPermutationCTable = arrayOf(
        14, 17, 11, 24, 1, 5,
        3, 28, 15, 6, 21, 10,
        23, 19, 12, 4, 26, 8,
        16, 7, 27, 20, 13, 2,
        41, 52, 31, 37, 47, 55,
        30, 40, 51, 45, 33, 48,
        44, 49, 39, 56, 34, 53,
        46, 42, 50, 36, 29, 32
    )

    /**
     * 第二次置换
     * @param keyText 56位左移后序列
     * @return 48位置换结果
     */
    private fun permutedChoice2(keyText: Bits): Bits {
        val res = Bits(48)
        var ind = 0
        secondPermutationCTable.forEach {
            res[ind] = keyText[it - 1]
            ind++
        }
        return res
    }

    /**
     * 生成16个56位子密钥
     * @param key 64位密钥
     */
    private fun generateKeys(key: Bits) {
        subKeys.clear()
        val pc1 = permutedChoice1(key)
        var c = pc1.subList(0, 28)
        var d = pc1.subList(28, 56)
        for (i in 0 until 16) {
            c = rotation(c, rotationStep[i])
            d = rotation(d, rotationStep[i])
            val genKey = c + d
            subKeys.add(permutedChoice2(genKey))
        }
    }

    /**
     * 初始置换表
     */
    private var initialPermutationTable: Array<Int> = arrayOf(
        58, 50, 42, 34, 26, 18, 10, 2,
        60, 52, 44, 36, 28, 20, 12, 4,
        62, 54, 46, 38, 30, 22, 14, 6,
        64, 56, 48, 40, 32, 24, 16, 8,
        57, 49, 41, 33, 25, 17, 9, 1,
        59, 51, 43, 35, 27, 19, 11, 3,
        61, 53, 45, 37, 29, 21, 13, 5,
        63, 55, 47, 39, 31, 23, 15, 7
    )

    /**
     * 初始置换
     * @param plainText 64位明文
     * @return 64位置换后序列
     */
    private fun initialPermutation(plainText: Bits): Bits {
        val result = Bits(64)
        var ind = 0
        initialPermutationTable.forEach {
            result[ind] = plainText[it - 1]
            ind++
        }
        return result
    }

    /**
     * 扩充置换表
     */
    private var expandPermutationTable: Array<Int> = arrayOf(
        32, 1, 2, 3, 4, 5,
        4, 5, 6, 7, 8, 9,
        8, 9, 10, 11, 12, 13,
        12, 13, 14, 15, 16, 17,
        16, 17, 18, 19, 20, 21,
        20, 21, 22, 23, 24, 25,
        24, 25, 26, 27, 28, 29,
        28, 29, 30, 31, 32, 1
    )

    /**
     * 扩充置换
     * @param r0 32位序列
     * @return 48位置换后序列
     */
    private fun expand(r0: Bits): Bits {
        val result = Bits(48)
        var ind = 0
        expandPermutationTable.forEach {
            result[ind] = r0[it - 1]
            ind++
        }
        return result
    }

    /**
     * S盒
     */
    private var sBoxes = arrayOf(
        //S1
        arrayOf(
            arrayOf(14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7),
            arrayOf(0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8),
            arrayOf(4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0),
            arrayOf(15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13)
        ),
        //S2
        arrayOf(
            arrayOf(15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10),
            arrayOf(3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5),
            arrayOf(0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15),
            arrayOf(13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9)
        ),
        //S3
        arrayOf(
            arrayOf(10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8),
            arrayOf(13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1),
            arrayOf(13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7),
            arrayOf(1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12)
        ),
        //S4
        arrayOf(
            arrayOf(7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15),
            arrayOf(13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9),
            arrayOf(10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4),
            arrayOf(3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14)
        ),
        //S5
        arrayOf(
            arrayOf(2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9),
            arrayOf(14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6),
            arrayOf(4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14),
            arrayOf(11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3)
        ),
        //S6
        arrayOf(
            arrayOf(12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11),
            arrayOf(10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8),
            arrayOf(9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6),
            arrayOf(4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13)
        ),
        //S7
        arrayOf(
            arrayOf(4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1),
            arrayOf(13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6),
            arrayOf(1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2),
            arrayOf(6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12)
        ),
        ///S8
        arrayOf(
            arrayOf(13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7),
            arrayOf(1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2),
            arrayOf(7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8),
            arrayOf(2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11)
        ),
    )

    /**
     * 代换选择
     * @param value 48位序列
     * @return 32位变换后序列
     */
    private fun substitution(value: Bits): Bits {
        val blocks = arrayListOf<Bits>()
        for (i in 0 until 8) {
            blocks.add(value[i * 6 until i * 6 + 6])
        }

        val res = Bits(32)
        for (i in 0 until 8) {
            val inputToSbox = blocks[i]
            val row = inputToSbox[0] * 2 + inputToSbox[5]
            val col = inputToSbox[1] * 8 + inputToSbox[2] * 4 + inputToSbox[3] * 2 + inputToSbox[4]
            val resDig = sBoxes[i][row][col]
            for (j in 0..3) {
                res[i * 4 + j] = if (resDig and (1 shl j) == (1 shl j)) 1 else 0
            }
        }
        return res
    }

    /**
     * 置换选择表
     */
    private var permutation = arrayOf(
        16, 7, 20, 21,
        29, 12, 28, 17,
        1, 15, 23, 26,
        5, 18, 31, 10,
        2, 8, 24, 14,
        32, 27, 3, 9,
        19, 13, 30, 6,
        22, 11, 4, 25
    )

    /**
     * 置换选择
     * @param value 32位序列
     * @return 32位变换后序列
     */
    private fun permutation(value: Bits): Bits {
        val res = Bits(32)
        var ind = 0
        permutation.forEach {
            res[ind] = value[it - 1]
            ind++
        }
        return res
    }

    /**
     * 轮函数
     * @param rightPlainText 32位序列
     * @param subKey 48位子密钥
     * @return 32位序列
     */
    private fun feistelFun(rightPlainText: Bits, subKey: Bits): Bits {
        val expandText = expand(rightPlainText) xor subKey
        val subText = substitution(expandText)
        return permutation(subText)
    }

    /**
     * 初始逆置换
     */
    private var finalPermutationTable: Array<Int> = arrayOf(
        40, 8, 48, 16, 56, 24, 64, 32,
        39, 7, 47, 15, 55, 23, 63, 31,
        38, 6, 46, 14, 54, 22, 62, 30,
        37, 5, 45, 13, 53, 21, 61, 29,
        36, 4, 44, 12, 52, 20, 60, 28,
        35, 3, 43, 11, 51, 19, 59, 27,
        34, 2, 42, 10, 50, 18, 58, 26,
        33, 1, 41, 9, 49, 17, 57, 25
    )

    /**
     * 逆初始置换
     * @param plainText 64位序列
     * @return 64位置换后序列
     */
    private fun finalPermutation(plainText: Bits): Bits {
        val result = Bits(64)
        var ind = 0
        finalPermutationTable.forEach {
            result[ind] = plainText[it - 1]
            ind++
        }
        return result
    }

    /**
     * 加密
     * @param plainText 64位明文
     * @return 64位密文
     */
    fun encrypt(plainText: Bits): Bits {
        generateKeys(keyBits)
        val fpText = initialPermutation(plainText)
        var l = fpText[0 until 32]
        var r = fpText[32 until 64]
        subKeys.forEach {
            val tmpl = l
            val tmpr = r
            l = tmpr
            r = tmpl xor feistelFun(tmpr, it)
        }
        return finalPermutation(r + l)
    }

    /**
     * 加密
     * @param plainText 待加密字符串
     * @return 加密后的密文
     */
    fun encrypt(plainText: String): Bits {
        val resList = arrayListOf<Bits>()
        plainText.toBits().forEach {
            resList.add(encrypt(it))
        }
        var res = Bits(0)
        resList.forEach {
            res += it
        }
        return res
    }

    /**
     * 解密
     * @param cypherText 64位密文
     * @return 64位明文
     */
    fun decrypt(cypherText: Bits): Bits {
        generateKeys(keyBits)
        subKeys.reverse()
        val fpText = initialPermutation(cypherText)
        var l = fpText[0 until 32]
        var r = fpText[32 until 64]
        subKeys.forEach {
            val tmpl = l
            val tmpr = r
            l = tmpr
            r = tmpl xor feistelFun(tmpr, it)
        }

        return finalPermutation(r + l)
    }

    /**
     * 解密
     * @param plainText 待解密字符串
     * @return 解密后的明文
     */
    fun decrypt(plainText: String): Bits {
        val resList = arrayListOf<Bits>()
        plainText.toBits().forEach {
            resList.add(decrypt(it))
        }
        var res = Bits(0)
        resList.forEach {
            res += it
        }
        return res
    }
}