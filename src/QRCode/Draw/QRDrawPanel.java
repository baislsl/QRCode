package QRCode.Draw;

import QRCode.util.QRErrorCorrectLevel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by baislsl on 17-5-19.
 */
public class QRDrawPanel {
    private final JFrame frame;

    public QRDrawPanel(String text){
        this(text, QRErrorCorrectLevel.H);
    }

    public QRDrawPanel(String text, QRErrorCorrectLevel errorCorrectLevel){
        frame = new JFrame();
        frame.setSize(QRCanvas.getDefaultSize());
        frame.setLayout(new GridLayout());
        frame.add(new QRCanvas(text, errorCorrectLevel));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void draw(){
        frame.setVisible(true);
    }
}
