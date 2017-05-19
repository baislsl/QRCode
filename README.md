#A QRCode Generator
This repository translate the input string inro QRCode and automatically 
generate a picture of the QRCode.

Main algorithm are translated from 
[davidshimjs/qrcodejs](https://github.com/davidshimjs/qrcodejs)
to java.

The original project was built in IntelliJ IDEA under Ubuntu 16.04 LTS.

#Screenshot
![](./screenshot.png)

#How to use
Include the QRCode package into the java code.
```java
import QRCode.Draw.QRDrawPanel;
```
Generate QR image
```
new QRDrawPanel("https://github.com/baislsl/QRCode").draw();
```
You can define your QRErrorCorrectLevel by 
```
import QRCode.util.QRErrorCorrectLevel;

final QRErrorCorrectLevel errorCorrectLevel = QRErrorCorrectLevel.H;
new QRDrawPanel(text, errorCorrectLevel).draw();
```
where QRErrorCorrectLevel is defined in [QRErrorCorrectLevel.java](./src/QRCode/util/QRErrorCorrectLevel.java) as
```java
public enum QRErrorCorrectLevel {
    M,
    L,
    H,
    Q
}
```
This repository use Swing to generate picture.

## License
MIT License
