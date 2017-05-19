package QRCode;

import QRCode.util.QRMode;

import java.util.ArrayList;

/**
 * Created by baislsl on 17-5-18.
 */

/**
 * UTF-8
 * 1111_0000 | (0001_1100_0000_0000_0000_0000 & code) >> 18
 * 1110_0000 | (1111_0000_0000_0000 & code) >>> 12
 * 1000_0000 | (1111_1100_0000 & code) >>> 6
 *
 * 1100_0000 | (0111_1100_0000 & code) >>> 6
 * 1000_0000 | 0011_1111 & code
 */

public class QR8bitByte {
    private QRMode mode;
    private String data;
    private ArrayList<Byte> parsedData;

    public QR8bitByte(String data) {
        this.data = data;
        this.mode = QRMode.MODE_8BIT_BYTE;
        parsedData = new ArrayList<>();
        for (int i = 0; i < data.length(); i++) {
            int count;
            int[] byteArray = new int[4];
            int code = (int) (data.charAt(i));
            if (code > 0x10000) {
                count = 4;
                byteArray[0] = 0xF0 | ((code & 0x1C0000) >>> 18);
                byteArray[1] = 0x80 | ((code & 0x3F000) >>> 12);
                byteArray[2] = 0x80 | ((code & 0xFC0) >>> 6);
                byteArray[3] = 0x80 | (code & 0x3F);
            } else if (code > 0x800) {
                count = 3;
                byteArray[0] = 0xE0 | ((code & 0xF000) >>> 12);
                byteArray[1] = 0x80 | ((code & 0xFC0) >>> 6);
                byteArray[2] = 0x80 | (code & 0x3F);
            } else if (code > 0x80) {
                count = 2;
                byteArray[0] = 0xC0 | ((code & 0x7C0) >>> 6);
                byteArray[1] = 0x80 | (code & 0x3F);
            } else {
                count = 1;
                byteArray[0] = code;
            }
            for (int j = 0; j < count; j++) {
                parsedData.add((byte) byteArray[j]);
            }
        }

    }

    public int getLength() {
        return parsedData.size();
    }

    public void write(QRBitBuffer buffer) {
        for (int i = 0, l = this.parsedData.size(); i < l; i++) {
            buffer.put(parsedData.get(i), 8);
        }
    }

    public QRMode getMode() {
        return mode;
    }

}
