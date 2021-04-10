# 基于Kotlin实现的DES和3DES算法
## 0.算法原理
略，请自行搜索
## 1.主要类介绍
单击查看源码

[Bits类](/src/main/kotlin/bits/Bits.kt)

[DES类](/src/main/kotlin/DES/DES.kt)

[TripleDES类](/src/main/kotlin/DES/TripleDES.kt)

## 2.用法

构造[DES类](/src/main/kotlin/DES/DES.kt)
```kotlin
val key = Bits() //密钥，Bits类对象
var des = DES(key) //DES类，参数为Bits类的对象
```
DES加、解密
```kotlin
val plainText = Bits() //明文，Bits类对象
val cipherText = des.encrypt(plainText) //加密，返回Bits类对象
val finalText = des.decrypt(cipherText) //解密，返回Bits类对象
```

构造[TripleDES类](/src/main/kotlin/DES/TripleDES.kt)
```kotlin
val key = Bits() //密钥，Bits类对象
var des = TripleDES(key) //DES类，参数为Bits类的对象
```
3DES加、解密
```kotlin
val plainText = Bits() //明文，Bits类对象
val cipherText = des.encrypt(plainText) //加密，返回Bits类对象
val finalText = des.decrypt(cipherText) //解密，返回Bits类对象
```