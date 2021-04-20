package rsa

data class SecretKey(val num: Long, val cry: Long) {
    override fun toString(): String {
        return "num = $num, cry = $cry"
    }
}