package QRCode;

import QRCode.util.QRErrorCorrectLevel;
import QRCode.util.QRMaskPattern;
import QRCode.util.QRUtil;

import java.util.ArrayList;

/**
 * Created by baislsl on 17-5-18.
 */
public class QRCodeModel {
    private int typeNumber;
    private QRErrorCorrectLevel errorCorrectLevel;
    private Boolean[][] modules;
    private int moduleCount;
    private byte[] dataCache;
    private ArrayList<QR8bitByte> dataList;
    private static int PAD0 = 0xEC;
    private static int PAD1 = 0x11;

    public QRCodeModel(int typeNumber, QRErrorCorrectLevel errorCorrectLevel) {
        this.typeNumber = typeNumber;
        this.errorCorrectLevel = errorCorrectLevel;
        this.moduleCount = 0;
        this.dataList = new ArrayList<>();
    }

    public void addData(String data) {
        QR8bitByte newData = new QR8bitByte(data);
        dataList.add(newData);
        dataCache = null;
    }

    public boolean isDark(int row, int col) {
        if (row < 0 || moduleCount <= row || col < 0 || moduleCount <= col) {
            throw new Error(row + "," + col);
        }
        return modules[row][col];
    }

    public int getModuleCount() {
        return moduleCount;
    }

    public void make() {
        this.makeImpl(false, getBestMaskPattern());
    }

    private void makeImpl(boolean test, QRMaskPattern maskPattern) {
        moduleCount = typeNumber * 4 + 17;
        modules = new Boolean[moduleCount][moduleCount];
        this.setupPositionProbePattern(0, 0);
        this.setupPositionProbePattern(this.moduleCount - 7, 0);
        this.setupPositionProbePattern(0, this.moduleCount - 7);
        this.setupPositionAdjustPattern();
        this.setupTimingPattern();
        this.setupTypeInfo(test, maskPattern);
        if (this.typeNumber >= 7) {
            this.setupTypeNumber(test);
        }
        if (this.dataCache == null) {
            this.dataCache = QRCodeModel.createData(this.typeNumber,
                    this.errorCorrectLevel, this.dataList.toArray(new QR8bitByte[0]));
        }
        this.mapData(this.dataCache, maskPattern);
    }

    private void setupPositionProbePattern(int row, int col) {
        for (int r = -1; r <= 7; r++) {
            if (row + r <= -1 || this.moduleCount <= row + r) continue;
            for (int c = -1; c <= 7; c++) {
                if (col + c <= -1 || this.moduleCount <= col + c) continue;
                if ((0 <= r && r <= 6 && (c == 0 || c == 6)) || (0 <= c && c <= 6 && (r == 0 || r == 6)) || (2 <= r && r <= 4 && 2 <= c && c <= 4)) {
                    modules[row + r][col + c] = true;
                } else {
                    modules[row + r][col + c] = false;
                }
            }
        }
    }

    private void setupPositionAdjustPattern() {
        int[] pos = QRUtil.getPatternPosition(this.typeNumber);
        for (int i = 0; i < pos.length; i++) {
            for (int j = 0; j < pos.length; j++) {
                int row = pos[i];
                int col = pos[j];
                if (this.modules[row][col] != null) {
                    continue;
                }
                for (int r = -2; r <= 2; r++) {
                    for (int c = -2; c <= 2; c++) {
                        if (r == -2 || r == 2 || c == -2 || c == 2 || (r == 0 && c == 0)) {
                            modules[row + r][col + c] = true;
                        } else {
                            modules[row + r][col + c] = false;
                        }
                    }
                }
            }
        }
    }

    private QRMaskPattern getBestMaskPattern() {
        int minLostPoint = 0, pattern = 0;
        for (int i = 0; i < 8; i++) {
            this.makeImpl(true, QRMaskPattern.values()[i]);
            int lostPoint = QRUtil.getLostPoint(this);
            if (i == 0 || minLostPoint > lostPoint) {
                minLostPoint = lostPoint;
                pattern = i;
            }
        }
        return QRMaskPattern.values()[pattern];
    }

    private void setupTimingPattern() {
        for (int r = 8; r < this.moduleCount - 8; r++) {
            if (this.modules[r][6] != null) {
                continue;
            }
            this.modules[r][6] = (r % 2 == 0);
        }
        for (int c = 8; c < this.moduleCount - 8; c++) {
            if (this.modules[6][c] != null) {
                continue;
            }
            this.modules[6][c] = (c % 2 == 0);
        }
    }

    private void setupTypeNumber(boolean test) {
        int bits = QRUtil.getBCHTypeNumber(this.typeNumber);
        for (int i = 0; i < 18; i++) {
            boolean mod = (!test && ((bits >> i) & 1) == 1);
            this.modules[(int) Math.floor(i / 3)][i % 3 + this.moduleCount - 8 - 3] = mod;
        }
        for (int i = 0; i < 18; i++) {
            boolean mod = (!test && ((bits >> i) & 1) == 1);
            this.modules[i % 3 + this.moduleCount - 8 - 3][(int) Math.floor(i / 3)] = mod;
        }
    }

    private void setupTypeInfo(boolean test, QRMaskPattern maskPattern) {
        int data = (errorCorrectLevel.getCode() << 3) | maskPattern.getCode();
        int bits = QRUtil.getBCHTypeInfo(data);
        for (int i = 0; i < 15; i++) {
            boolean mod = (!test && ((bits >> i) & 1) == 1);
            if (i < 6) {
                this.modules[i][8] = mod;
            } else if (i < 8) {
                this.modules[i + 1][8] = mod;
            } else {
                this.modules[this.moduleCount - 15 + i][8] = mod;
            }
        }
        for (int i = 0; i < 15; i++) {
            boolean mod = (!test && ((bits >> i) & 1) == 1);
            if (i < 8) {
                this.modules[8][this.moduleCount - i - 1] = mod;
            } else if (i < 9) {
                this.modules[8][15 - i - 1 + 1] = mod;
            } else {
                this.modules[8][15 - i - 1] = mod;
            }
        }
        this.modules[this.moduleCount - 8][8] = (!test);
    }

    private void mapData(byte[] data, QRMaskPattern maskPattern) {
        int inc = -1;
        int row = this.moduleCount - 1;
        int bitIndex = 7;
        int byteIndex = 0;
        for (int col = this.moduleCount - 1; col > 0; col -= 2) {
            if (col == 6) col--;
            while (true) {
                for (int c = 0; c < 2; c++) {
                    if (this.modules[row][col - c] == null) {
                        boolean dark = false;
                        if (byteIndex < data.length) {
                            dark = (((data[byteIndex] >>> bitIndex) & 1) == 1);
                        }
                        boolean mask = QRUtil.getMask(maskPattern, row, col - c);
                        if (mask) {
                            dark = !dark;
                        }
                        this.modules[row][col - c] = dark;
                        bitIndex--;
                        if (bitIndex == -1) {
                            byteIndex++;
                            bitIndex = 7;
                        }
                    }
                }
                row += inc;
                if (row < 0 || this.moduleCount <= row) {
                    row -= inc;
                    inc = -inc;
                    break;
                }
            }
        }
    }

    private static byte[] createData(int typeNumber, QRErrorCorrectLevel errorCorrectLevel,
                                    QR8bitByte[] dataList) {
        QRRSBlock[] rsBlocks = QRRSBlock.getRSBlocks(typeNumber, errorCorrectLevel);
        QRBitBuffer buffer = new QRBitBuffer();
        for(QR8bitByte data : dataList){
            buffer.put(data.getMode().getCode(), 4);
            buffer.put(data.getLength(), QRUtil.getLengthInBits(data.getMode(), typeNumber));
            data.write(buffer);
        }
        int totalDataCount = 0;
        for (QRRSBlock rsBlock : rsBlocks) {
            totalDataCount += rsBlock.dataCount;
        }
        if (buffer.getLengthInBits() > totalDataCount * 8) {
            throw new Error("code length overflow. ("
                    + buffer.getLengthInBits()
                    + ">"
                    + totalDataCount * 8
                    + ")");
        }
        if (buffer.getLengthInBits() + 4 <= totalDataCount * 8) {
            buffer.put(0, 4);
        }
        while (buffer.getLengthInBits() % 8 != 0) {
            buffer.putBit(false);
        }
        while (true) {
            if (buffer.getLengthInBits() >= totalDataCount * 8) {
                break;
            }
            buffer.put(QRCodeModel.PAD0, 8);
            if (buffer.getLengthInBits() >= totalDataCount * 8) {
                break;
            }
            buffer.put(QRCodeModel.PAD1, 8);
        }
        return QRCodeModel.createBytes(buffer, rsBlocks);
    }

    private static byte[] createBytes(QRBitBuffer buffer, QRRSBlock[] rsBlocks) {
        int offset = 0;
        int maxDcCount = 0;
        int maxEcCount = 0;

        int[][] dcdata = new int[rsBlocks.length][];
        int[][] ecdata = new int[rsBlocks.length][];

        for (int r = 0; r < rsBlocks.length; r++) {
            int dcCount = rsBlocks[r].dataCount;
            int ecCount = rsBlocks[r].totalCount - dcCount;
            maxDcCount = Math.max(maxDcCount, rsBlocks[r].dataCount);
            maxEcCount = Math.max(maxEcCount, rsBlocks[r].totalCount - rsBlocks[r].dataCount);
            dcdata[r] = new int[dcCount];
            for (int i = 0; i < dcCount; i++) {
                dcdata[r][i] = 0xff & buffer.buffer.get(i + offset);
            }
            offset += dcCount;
            QRPolynomial rsPoly = QRUtil.getErrorCorrectPolynomial(ecCount);
            QRPolynomial rawPoly = new QRPolynomial(dcdata[r], rsPoly.getLength() - 1);
            QRPolynomial modPoly = rawPoly.mod(rsPoly);
            ecdata[r] = new int[rsPoly.getLength() - 1];
            for (int i = 0; i < ecdata[r].length; i++) {
                int modIndex = i + modPoly.getLength() - ecdata[r].length;
                ecdata[r][i] = (modIndex >= 0) ? modPoly.get(modIndex) : 0;
            }
        }
        int totalCodeCount = 0;
        for(QRRSBlock rsBlock : rsBlocks){
            totalCodeCount += rsBlock.totalCount;
        }
        byte[] data = new byte[totalCodeCount];
        int index = 0;
        for (int i = 0; i < maxDcCount; i++) {
            for (int r = 0; r < rsBlocks.length; r++) {
                if (i < dcdata[r].length) {
                    data[index++] = (byte) dcdata[r][i]; // ------------------------------
                }
            }
        }
        for (int i = 0; i < maxEcCount; i++) {
            for (int r = 0; r < rsBlocks.length; r++) {
                if (i < ecdata[r].length) {
                    data[index++] = (byte) ecdata[r][i]; // ----------------------------
                }
            }
        }
        return data;
    }

}
