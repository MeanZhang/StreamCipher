import java.util.Scanner;

/**
 * 梅森旋转算法，参考维基百科
 */
public class MersenneTwister {
    //创建一个长度为624的数组来存储发生器的状态
    long[] MT = new long[624];
    int index = 0;

    /**
     * 产生下一个随机数
     *
     * @return 随机数
     */
    public long random() {
        if (index == 0)
            generate();
        long y = MT[index];
        y = y ^ (y >> 11);
        // 2636928640 = 0x9d2c5680
        y = y ^ ((y << 7) & 2636928640L);
        // 4022730752 == 0xefc60000
        y = y ^ ((y << 15) & 4022730752L);
        y = y ^ (y >> 18);
        index = (index + 1) % 624;
        return y;
    }

    /**
     * 初始化发生器
     *
     * @param seed 随机种子
     */
    private void init(int seed) {
        //种子作为第一项
        MT[0] = seed;
        for (int i = 1; i < 624; i++) {
            long a = 1812433253L * (MT[i - 1] ^ (MT[i - 1] >> 30)) + i;
            // 1812433253 = 0x6c078965
            MT[i] = (1812433253L * (MT[i - 1] ^ (MT[i - 1] >> 30)) + i) & 0xffffffffL;
        }
    }

    private void generate() {
        long y;
        for (int i = 0; i < 624; i++) {
            y = (MT[i] & 0x80000000L) + (MT[(i + 1) % 624] & 0x7fffffffL);
            MT[i] = MT[(i + 397) % 624] ^ (y >> 1);
            if (y % 2 != 0)
                // 2567483615 = 0x9908b0df
                MT[i] = MT[i] ^ 2567483615L;
        }
    }

    /**
     * 构造方法，先初始化
     *
     * @param seed 随机种子
     */
    public MersenneTwister(int seed) {
        init(seed);
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("seed: ");
        MersenneTwister mt = new MersenneTwister(scan.nextInt());
        System.out.print("length: ");
        int length = scan.nextInt();
        for (int i = 0; i < length; i++)
            System.out.println(mt.random());
        scan.close();
    }
}