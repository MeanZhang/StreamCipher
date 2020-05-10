import java.util.Random;
import java.util.Scanner;

/**
 * BBS发生器
 */
public class BBS {
    int p, q;
    //用于初始化x
    long s;
    long x;

    /**
     * BBS发生器，产生伪随机位
     *
     * @return 随机位
     */
    public int random() {
        //随机位
        int b;
        long n = (long) p * q;
        x = (x % n) * (x % n) % n;
        b = (int) (x % 2);
        return b;
    }

    /**
     * 初始化发生器
     */
    private void init() {
        //生成模4余3的素数p
        p = prime();
        //生成模4余3的素数q
        q = prime();
        Random random = new Random();
        while (true) {
            s = Math.abs(random.nextLong());
            //s必须与n互素
            if (s % p != 0 && s % q != 0)
                break;
        }
        x = s;
    }

    /**
     * 构造方法，首先初始化
     */
    public BBS() {
        init();
    }

    /**
     * 生成一个模4余3的素数
     *
     * @return 模4余3的素数
     */
    private static int prime() {
        Random random = new Random();
        int prime;
        boolean flag;
        //素性检测次数
        int time;
        while (true) {
            //产生一个随机数
            prime = Math.abs(random.nextInt());
            flag = true;
            if (prime % 4 == 3) {
                //素性检测次数
                time = (int) Math.log(prime) / 2;
                //素性检测
                for (int i = 0; i < time; i++)
                    //如果检测不是素数
                    if (!millerRabin(prime)) {
                        flag = false;
                        break;
                    }
                //通过测试就认为是合数
                if (flag)
                    return prime;
            }
        }
    }

    /**
     * Miller-Rabin素性检测
     *
     * @param n 待测数
     * @return 是否为素数
     */
    private static boolean millerRabin(int n) {
        int k = 1;
        int q = (n - 1) >> 1;
        // n - 1 = 2^k * q
        while (q % 2 == 0) {
            q = q / 2;
            k++;
        }
        Random random = new Random();
        int a;
        a = random.nextInt(n - 3) + 2;
        // 1 < a < n - 1
        // a^q mod n = 1
        if (fastPowMod(a, q, n) == 1)
            return true;
        if (fastPowMod(a, q, n) == n - 1)
            return true;
        for (int j = 1; j < k; j++)
            // a^(2^j * q) mod n = n-1
            if (fastPowMod(a, (2 << (j - 1)) * q, n) == n - 1)
                return true;
        return false;
    }

    /**
     * 快速幂模算法
     *
     * @param x base 底数
     * @param n index 指数
     * @param m mod 模数
     * @return remainder 余数
     */
    private static int fastPowMod(int x, int n, int m) {
        int d = 1;
        while (n > 0) {
            //该位为1
            if (n % 2 == 1)
                //结果*当前位的幂
                d = d * x % m;
            //去掉当前位
            n /= 2;
            //下一位的幂
            x = x * x % m;
        }
        return d;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("length: ");
        int length = scanner.nextInt();
        BBS bbs = new BBS();
        System.out.println("s: " + bbs.s);
        for (int i = 0; i < length; i++)
            System.out.print(bbs.random());
        scanner.close();
    }
}
