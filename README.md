包含BBS发生器、RC4算法、梅森旋转算法和ZUC-128（祖冲之算法）。

# <font size=3>【实验目的】</font>

1.   了解常用的流密码算法，并对其进行实现；
2.    了解常用的伪随机数生成算法，并对其进行实现。

# <font size=3>【原理简介】</font>

流密码（Stream Cipher）也称为序列密码，它是对称密码算法的一种。流密码具有实现简单、便于硬件实施、加解密处理速度快、没有或只有有限的错误传播等特点，因此在实际应用中，特别是专用或机密机构中保持着优势，典型的应用领域包括无线通信、外交通信。

# <font size=3>【实验环境】</font>

Windows 10，IntelliJ IDEA Community Edition 2020.1.1，JDK 13.0.2。

# <font size=3>【实验内容】</font>

## 一、BBS发生器

### 1. 算法原理

Blum与Shub发明了最简单有效的伪随机发生器，称为 Blum，Blum 和Shub 发生器，简称为BBS，有时也称二次剩余发生器，BBS 发生器的理论是必须做模n的二次剩余。

### 2. 算法流程

BBS发生器大致流程如下：

<img src="pictures\01.jpg" alt="01" style="zoom: 25%;" />

伪代码如下：

```c
random()
int b;
p = prime();//p,q为模4余3的素数
q = prime();
n = p * q;
s;//s与n互素
x = s * s % n;
while(true)
    x = x * x % n;
	b = x % 2;
```

由于BBS可以产生不限长度的随机位序列，所以将BBS发生器构造为一个类，实例化后可通过调用`public int random()`产生随机位序列。

### 3. 测试样例及运行结果

由于s是随机选取的，故输出结果也是随机的，下面给出多个50位随机位序列。

![02](pictures\02.png)

![03](pictures\03.png)

![04](pictures\04.png)

![05](pictures\05.png)

![06](pictures\06.png)

### 4. 总结

本算法的难点在于p、q、s的选取，我采用了生成随机数再检验是否符合条件，如果不符合就重新生成的方法获取。

## 二、 RC4算法

### 1. 算法原理

RC4使用了一个2^8^字节大小的非线性数据表(简称S表)，S表的值S~0~，S~1~，…，S~255~是数字0到255的一个排列。对S表进行非线性变换，得到密钥流。 

### 2. 算法流程

RC4加密和解密算法相同，大致流程如下：

![07](pictures\07.png)

伪代码如下：

```c
encrypt(key, plain):
for i = 0 to 255
    s[i] = i;
	k[i] = key[i % k.length];
for i = 0 to 255
    j = (j + s[i] + k[i]) % 256;
    swap(s[i], s[j]);
int i = 0, j = 0;
for k = 0 to plain.length
    i = (i + 1) % 256;
	j = (j +s[i]) % 256;
	swap(s[i], s[j]);
	t = (s[i] + s[j]) % 256;
	cipher[k] = s[t] ^ plain[k];
return cipher;
```

### 3. 测试样例及运行结果

对数字加密：

![08](pictures\08.png)

对文件加密：

![09](pictures\09.png)

明文：

![10](pictures\10.png)

密文：

![11](pictures\11.png)

### 4. 总结

RC4加解密算法相同，较为简单。由于RC4是流密码，无需分组和填充，比较适合文件加密，所以加入了文件的加解密。

## 三、 梅森旋转算法

### 1. 算法原理

梅森旋转算法（Mersenne twister）是一个伪随机数发生算法。由松本真和西村拓士[1]在1997年开发，基于有限二进制字段上的矩阵线性递归。可以快速产生高质量的伪随机数，修正了古典随机数发生算法的很多缺陷。

### 2. 算法流程

梅森旋转算法流程如下：

![12](pictures\12.png)

伪代码如下：

```c
//创建一个长度为624的数组来存储发生器的状态
int[0..623] MT
int index = 0
 
//初始化产生器，种子作为首项内容
initialize_generator(int seed) {
     i = 0
     MT[0] = seed
     //便历剩下的元素
     for i from 1 to 623
         MT[i] := last 32 bits of(1812433253 * 
                                  (MT[i-1] xor (right shift by 30 bits(MT[i-1]))) + i)
 
//根据第index个值提取经过调整的伪随机数，
//每624个数字调用一次generate_numbers()
function extract_number() {
    if index == 0 {
        generate_numbers()
    }

    int y := MT[index]
    y := y xor (right shift by 11 bits(y))
    y := y xor (left shift by 7 bits(y) and (2636928640))// 2636928640 == 0x9d2c5680
    y := y xor (left shift by 15 bits(y) and (4022730752))// 4022730752 == 0xefc60000
    y := y xor (right shift by 18 bits(y))

    index := (index + 1) mod 624
    return y
}

function generate_numbers() {
    for i from 0 to 623 {
        int y := (MT[i] & 0x80000000)
                       + (MT[(i+1) mod 624] & 0x7fffffff)
        MT[i] := MT[(i + 397) mod 624] xor (right shift by 1 bit(y))
        if (y mod 2) != 0 {
            MT[i] := MT[i] xor (2567483615) // 2567483615 == 0x9908b0df
        }
    }
}
```

梅森旋转算法也是产生随机数序列的算法，有时可能需要很多随机数，所以每次使用时将该类实例化，即可不断产生随机数。

### 3. 测试样例及运行结果

由于随机序列的长度不定，所以选取前20个随机数。

| 种子 | 1                                                       | 4986                                                    | 98645                                                   |
| ---- | ------------------------------------------------------- | ------------------------------------------------------- | ------------------------------------------------------- |
| 结果 | ![13](pictures\13.png) | ![14](pictures\14.png) | ![15](pictures\15.png) |

### 4. 总结

本算法主要的难度是找到的相关介绍比较少，最后在维基百科上看到了比较详细的描述，根据维基百科上的步骤就比较容易了。

## 四、 ZUC-128

### 1. 算法原理

祖冲之算法集（ZUC算法）是由我国学者自主设计的加密和完整性算法，包括祖冲之算法、加密算法128-EEA3和完整性算法128-EIA3，已经被国际组织3GPP推荐为4G无线通信的第三套国际加密和完整性标准的侯选算法。

### 2. 算法流程

祖冲之算法的核心部分是生成密钥序列的算法，这里仅实现了生成密钥序列的算法。

ZUC-128大致流程如下：

![16](pictures\16.png)

伪代码如下：

```c
zuc-128(k, iv):
for i = 0 to 16
    s[i] = k[i] | d[i] | iv[i];
r1 = r2 = 0;
for i = 0 to 32
    bitReconstruction();
    w = f();
    LFSRWithInitialisationMode(w >> 1);
bitReconstruction();
f();
LFSRWithWorkMode();
while(true)
    bitReconstruction();
    z = f() ^ x[3];//z即为生成的密钥
    LFSRWithWorkMode();


LFSRWithInitialisationMode(u)
v = (2^15*s[15] + 2^17*s[13] +2^15*s[15] + 2^21*s[10] 
    	+ 2^20*s[4] + (2^8+1)*s[0]) % (2^31-1);
s[16] = (v+u) % (2^31-1);
if(s[16]==0)
    s16 = 2^31-1;
for i = 0 to 15
	s[i] = s[i+1];


LFSRWithWorkMode()
s16 = (2^15*s[15] + 2^17*s[13] +2^15*s[15] + 2^21*s[10] 
    	+ 2^20*s[4] + (2^8+1)*s[0]) % (2^31-1);
if(s[16]==0)
    s16 = 2^31-1;
for i = 0 to 15
	s[i] = s[i+1];
```

### 3. 测试样例及运行结果

测试结果取前15个输出密钥。

![17](pictures\17.png)

![18](pictures\18.png)

![19](pictures\19.png)

### 4. 总结

本算法的难点在于位运算较多，比较麻烦，而且位运算的优先级低于加减法，实验过程中很容易出错，而且优先级错误容易被忽略，导致很难检查。不过只要仔细调试，把运算分解就可以定位出错的地方了。

## 五、收获与建议

这次实验都是流密码，如果要产生任意长度的随机数序列，就需要用到面向对象编程。但是我的思维一直是面向过程的，这次的实验加深了我对面向对象的理解，体会到了面向对象编程的一些便利之处。