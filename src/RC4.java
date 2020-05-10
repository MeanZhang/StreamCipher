import java.io.*;
import java.util.Scanner;

/**
 * RC4数字及文件加解密
 */
public class RC4 {
    /**
     * 数字加密
     *
     * @param key   小于256字节的密钥
     * @param plain 明文
     * @return 密文
     */
    public static int[] encrypt(int[] key, int[] plain) {
        //初始化S
        int[] s = initS(key);
        //明文字节数
        int len = plain.length;
        int i = 0, j = 0;
        int tmp;
        int t;
        int[] cipher = new int[len];
        //生成伪随机数序列并加密
        for (int k = 0; k < len; k++) {
            i = (i + 1) % 256;
            j = (j + s[i]) % 256;
            tmp = s[i];
            s[i] = s[j];
            s[j] = tmp;
            t = (s[i] + s[j]) % 256;
            cipher[k] = s[t] ^ plain[k];
        }
        return cipher;
    }

    /**
     * 文件加密
     *
     * @param key 密钥
     * @param p   明文文件
     * @param c   密文文件
     */
    public static void fileEncrypt(int[] key, File p, File c) {
        try {
            InputStream in = new FileInputStream(p);
            // 如果密文文件不存在就创建
            if (!c.exists())
                c.createNewFile();
            OutputStream out = new FileOutputStream(c);
            //初始化S
            int[] s = initS(key);
            int i = 0, j = 0;
            int tmp;
            int t;
            int plain;
            //循环加密
            while ((plain = in.read()) != -1) {
                i = (i + 1) % 256;
                j = (j + s[i]) % 256;
                tmp = s[i];
                s[i] = s[j];
                s[j] = tmp;
                t = (s[i] + s[j]) % 256;
                //写入文件
                out.write(s[t] ^ plain);
            }
            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File Not Found");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化S
     *
     * @param key 密钥
     * @return S
     */
    private static int[] initS(int[] key) {
        int[] s = new int[256];
        int[] k = new int[256];
        int length = key.length;
        int j = 0;
        int tmp;
        for (int i = 0; i < 256; i++) {
            s[i] = i;
            k[i] = key[i % length];
        }
        for (int i = 0; i < 256; i++) {
            j = (j + s[i] + k[i]) % 256;
            tmp = s[i];
            s[i] = s[j];
            s[j] = tmp;
        }
        return s;
    }


    //测试
    public static void main(String[] args) {
//        int[] key = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
//        int[] p = {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
//        int[] c = {0,197,95,166,129,168,12,166,22,10819,56,225,225,106,17};
//        int[] result;
//        result=encrypt(key,p);
//        result=decrypt(key,c);
//        File p = new File("plain.txt");
//        File c = new File("cipher.txt");
//        fileEncrypt(key,p,c);
//        fileDecrypt(key,c,p);
        Scanner scan = new Scanner(System.in);
        System.out.println("1. number encrypt or decrypt\n2. file encrypt or encrypt");
        int op = scan.nextInt();
        System.out.print("key length: ");
        int l = scan.nextInt();
        int[] key = new int[l];
        System.out.println("key:");
        for (int i = 0; i < l; i++)
            key[i] = scan.nextInt();
        if (op == 1) {
            System.out.print("message length: ");
            int length = scan.nextInt();
            System.out.println("message:");
            int[] m = new int[length];
            for (int i = 0; i < length; i++)
                m[i] = scan.nextInt();
            int[] c = encrypt(key, m);
            System.out.println("cipher:");
            for (int i = 0; i < length; i++)
                System.out.print(c[i] + " ");
        } else {
            System.out.print("plain file: ");
            File p = new File(scan.next());
            System.out.print("cipher file: ");
            File c = new File(scan.next());
            fileEncrypt(key, p, c);
        }
        scan.close();
    }
}
