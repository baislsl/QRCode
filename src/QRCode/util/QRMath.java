package QRCode.util;

/**
 * Created by baislsl on 17-5-18.
 */
public class QRMath {
    private static int[] EXP_TABLE = new int[256];
    private static int[] LOG_TABLE = new int[256];

    static {
        for (int i = 0; i < 8; i++) {
            EXP_TABLE[i] = 1 << i;
        }
        for (int i = 8; i < 256; i++) {
            QRMath.EXP_TABLE[i] = QRMath.EXP_TABLE[i - 4] ^ QRMath.EXP_TABLE[i - 5] ^ QRMath.EXP_TABLE[i - 6] ^ QRMath.EXP_TABLE[i - 8];
        }
        for (int i = 0; i < 255; i++) {
            QRMath.LOG_TABLE[QRMath.EXP_TABLE[i]] = i;
        }
    }

    public static int glog(int n) {
        if(n < 1)
            System.err.print("glog(" + n + ")");
        return QRMath.LOG_TABLE[n];

    }

    public static int gexp(int n) {
        while (n < 0) {
            n += 255;
        }
        while (n >= 256) {
            n -= 255;
        }
        return QRMath.EXP_TABLE[n];
    }
}
