package QRCode.Draw;

import QRCode.QR.QRCodeModel;
import QRCode.util.Decode;
import QRCode.util.QRErrorCorrectLevel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by baislsl on 17-5-19.
 */
public class QRCanvas extends JComponent {
    private int count;
    private final static int DEFAULT_LENGTH = 400;
    private int offset = 10;
    private int size;
    private QRCodeModel codeModel;

    QRCanvas(String text, QRErrorCorrectLevel errorCorrectLevel) {
        codeModel = new QRCodeModel(Decode.getTypeNumber(text, errorCorrectLevel), errorCorrectLevel);
        codeModel.addData(text);
        codeModel.make();
        this.count = codeModel.getModuleCount();
        size = (DEFAULT_LENGTH - 2 * offset) / count;
        offset = (DEFAULT_LENGTH - size*count) / 2; // rid the deviation of division
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        for (int row = 0; row < count; row++) {
            for (int col = 0; col < count; col++) {
                Color color = codeModel.isDark(row, col) ? Color.BLACK : Color.WHITE;
                g2.setPaint(color);
                g2.fill(new Rectangle(offset + col * size, offset + row * size, size, size));
            }
        }
    }

    public static Dimension getDefaultSize() {
        return new Dimension(DEFAULT_LENGTH, DEFAULT_LENGTH);
    }
}
