package QRCode;

import java.util.ArrayList;

/**
 * Created by baislsl on 17-5-18.
 */
public class QRBitBuffer {
    private int length;
    private ArrayList<Integer> buffer;

    public QRBitBuffer() {
        this.length = 0;
        this.buffer = new ArrayList<>();
    }


    public boolean get(int index) {
        int bufIndex = (int) (((double) index) / 8);
        return ((this.buffer.get(bufIndex) >>> (7 - index % 8)) & 1) == 1;
    }

    public void put(int num, int length) {
        for (int i = 0; i < length; i++) {

        }
    }

    public int getLengthInBits() {
        return this.length;
    }

    public void putBit(int bit) {
        int bufIndex = (int) (((double) length) / 8);
        if (this.buffer.size() <= bufIndex) {
            this.buffer.add(0);
        }
        if (bit != 0) {
            int var = buffer.get(bufIndex);
            var |= (0x80 >>> (this.length % 8));
            buffer.set(bufIndex, var);
        }
        length++;
    }

}
