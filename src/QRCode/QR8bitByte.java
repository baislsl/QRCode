package QRCode;

import util.QRMode;

/**
 * Created by baislsl on 17-5-18.
 */
public class QR8bitByte {
    private QRMode qrMode;
    String[] data;
    String parsedData;

    public QR8bitByte(String[] data) {
        this.data = data;

        for (String str : data) {
            for (int i = 0; i < str.length(); i++) {
                char cc = str.charAt(i);
            }
        }
    }

    public int geteLength() {
        return parsedData.length();
    }

    public void wirte() {
    }


}
