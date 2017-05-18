package util;

/**
 * Created by baislsl on 17-5-18.
 */
public enum QRMode {
    MODE_NUMBER(1 << 0),
    MODE_ALPHA_NUM(1 << 1),
    MODE_8BIT_BYTE(1 << 2),
    MODE_KANJI(1 << 3);

    private int code;

    QRMode(int code) {
        this.code = code;
    }
}
