package util;

/**
 * Created by baislsl on 17-5-18.
 */
public enum QRErrorCorrectLevel {
    L(1),
    M(0),
    Q(3),
    H(2);

    private int errorCode;
    QRErrorCorrectLevel(int errorCode){
        this.errorCode = errorCode;
    }
}
