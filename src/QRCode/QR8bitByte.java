package QRCode;

import QRCode.util.QRMode;

import java.util.ArrayList;

/**
 * Created by baislsl on 17-5-18.
 */
public class QR8bitByte {
    private QRMode mode;
    String data;
    ArrayList<Character> parsedData;

    public QR8bitByte(String data) {
        this.data = data;
        this.mode = QRMode.MODE_8BIT_BYTE;
        parsedData = new ArrayList<>();
        for (int i = 0; i < data.length(); i++) {
            char cc = data.charAt(i);
            parsedData.add(cc);
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

    public QRMode getMode(){
        return mode;
    }

}
