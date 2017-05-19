package QRCode.Draw;

import QRCode.QR.QRCodeModel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * Created by baislsl on 17-5-19.
 */
public class MainFrame extends JComponent {
    private int count;
    private final static int DEFAULT_LENGTH = 400;
    private final static int offset = 10;
    private int size;
    private  QRCodeModel codeModel;
    MainFrame(QRCodeModel codeModel){
        this.codeModel = codeModel;
        this.count = codeModel.getModuleCount();
        size = (DEFAULT_LENGTH - 2*offset) / count;
    }

    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        for(int row = 0;row < count ;row ++){
            for(int col = 0;col < count; col++){
                Color color = codeModel.isDark(row, col) ? Color.BLACK : Color.WHITE;
                g2.setPaint(color);
                g2.fill(new Rectangle(offset + col*size, offset + row*size, size, size));
            }
        }
    }

    public static Dimension getDefaultSize(){
        return new Dimension(DEFAULT_LENGTH, DEFAULT_LENGTH);
    }
}
