package QRCode.util;

/**
 * Created by baislsl on 17-5-18.
 */
public enum QRErrorCorrectLevel {
    M(0),
    L(1),
    H(2),
    Q(3);

    private int errorCode;
    QRErrorCorrectLevel(int errorCode){
        this.errorCode = errorCode;
    }

    public int getCode(){
        return errorCode;
    }
}
