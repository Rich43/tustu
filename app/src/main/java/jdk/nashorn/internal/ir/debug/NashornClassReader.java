package jdk.nashorn.internal.ir.debug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.fxml.FXMLLoader;
import jdk.internal.org.objectweb.asm.Attribute;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.Label;
import jdk.nashorn.internal.ir.debug.NashornTextifier;
import net.lingala.zip4j.util.InternalZipConstants;

/* loaded from: nashorn.jar:jdk/nashorn/internal/ir/debug/NashornClassReader.class */
public class NashornClassReader extends ClassReader {
    private final Map<String, List<Label>> labelMap;
    private static String[] type;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !NashornClassReader.class.desiredAssertionStatus();
        type = new String[]{"<error>", InternalZipConstants.CHARSET_UTF8, "<error>", "Integer", "Float", "Long", "Double", "Class", "String", "Fieldref", "Methodref", "InterfaceMethodRef", "NameAndType", "<error>", "<error>", "MethodHandle", "MethodType", "<error>", "Invokedynamic"};
    }

    public NashornClassReader(byte[] bytecode) {
        super(bytecode);
        this.labelMap = new HashMap();
        parse(bytecode);
    }

    List<Label> getExtraLabels(String className, String methodName, String methodDesc) {
        String key = fullyQualifiedName(className, methodName, methodDesc);
        return this.labelMap.get(key);
    }

    private static int readByte(byte[] bytecode, int index) {
        return (byte) (bytecode[index] & 255);
    }

    private static int readShort(byte[] bytecode, int index) {
        return ((short) ((bytecode[index] & 255) << 8)) | (bytecode[index + 1] & 255);
    }

    private static int readInt(byte[] bytecode, int index) {
        return ((bytecode[index] & 255) << 24) | ((bytecode[index + 1] & 255) << 16) | ((bytecode[index + 2] & 255) << 8) | (bytecode[index + 3] & 255);
    }

    private static long readLong(byte[] bytecode, int index) {
        int hi = readInt(bytecode, index);
        int lo = readInt(bytecode, index + 4);
        return (hi << 32) | lo;
    }

    private static String readUTF(int index, int utfLen, byte[] bytecode) {
        int endIndex = index + utfLen;
        char[] buf = new char[utfLen * 2];
        int strLen = 0;
        int st = 0;
        char cc = 0;
        int i2 = index;
        while (i2 < endIndex) {
            int i3 = i2;
            i2++;
            byte b2 = bytecode[i3];
            switch (st) {
                case 0:
                    int c2 = b2 & 255;
                    if (c2 < 128) {
                        int i4 = strLen;
                        strLen++;
                        buf[i4] = (char) c2;
                        break;
                    } else if (c2 < 224 && c2 > 191) {
                        cc = (char) (c2 & 31);
                        st = 1;
                        break;
                    } else {
                        cc = (char) (c2 & 15);
                        st = 2;
                        break;
                    }
                case 1:
                    int i5 = strLen;
                    strLen++;
                    buf[i5] = (char) ((cc << 6) | (b2 & 63));
                    st = 0;
                    break;
                case 2:
                    cc = (char) ((cc << 6) | (b2 & 63));
                    st = 1;
                    break;
            }
        }
        return new String(buf, 0, strLen);
    }

    private String parse(byte[] bytecode) {
        int magic = readInt(bytecode, 0);
        int u2 = 0 + 4;
        if (!$assertionsDisabled && magic != -889275714) {
            throw new AssertionError((Object) Integer.toHexString(magic));
        }
        readShort(bytecode, u2);
        int u3 = u2 + 2;
        readShort(bytecode, u3);
        int u4 = u3 + 2;
        int cpc = readShort(bytecode, u4);
        int u5 = u4 + 2;
        ArrayList<Constant> cp = new ArrayList<>(cpc);
        cp.add(null);
        int i2 = 1;
        while (i2 < cpc) {
            int tag = readByte(bytecode, u5);
            u5++;
            switch (tag) {
                case 1:
                    int len = readShort(bytecode, u5);
                    int u6 = u5 + 2;
                    cp.add(new DirectInfo(cp, tag, readUTF(u6, len, bytecode)));
                    u5 = u6 + len;
                    break;
                case 2:
                case 13:
                case 14:
                case 17:
                default:
                    if (!$assertionsDisabled) {
                        throw new AssertionError(tag);
                    }
                    break;
                case 3:
                    cp.add(new DirectInfo(cp, tag, Integer.valueOf(readInt(bytecode, u5))));
                    u5 += 4;
                    break;
                case 4:
                    cp.add(new DirectInfo(cp, tag, Float.valueOf(Float.intBitsToFloat(readInt(bytecode, u5)))));
                    u5 += 4;
                    break;
                case 5:
                    cp.add(new DirectInfo(cp, tag, Long.valueOf(readLong(bytecode, u5))));
                    cp.add(null);
                    i2++;
                    u5 += 8;
                    break;
                case 6:
                    cp.add(new DirectInfo(cp, tag, Double.valueOf(Double.longBitsToDouble(readLong(bytecode, u5)))));
                    cp.add(null);
                    i2++;
                    u5 += 8;
                    break;
                case 7:
                    cp.add(new IndexInfo(cp, tag, readShort(bytecode, u5)));
                    u5 += 2;
                    break;
                case 8:
                    cp.add(new IndexInfo(cp, tag, readShort(bytecode, u5)));
                    u5 += 2;
                    break;
                case 9:
                case 10:
                case 11:
                    cp.add(new IndexInfo2(cp, tag, readShort(bytecode, u5), readShort(bytecode, u5 + 2)));
                    u5 += 4;
                    break;
                case 12:
                    cp.add(new IndexInfo2(cp, tag, readShort(bytecode, u5), readShort(bytecode, u5 + 2)));
                    u5 += 4;
                    break;
                case 15:
                    int kind = readByte(bytecode, u5);
                    if (!$assertionsDisabled && (kind < 1 || kind > 9)) {
                        throw new AssertionError(kind);
                    }
                    cp.add(new IndexInfo2(cp, tag, kind, readShort(bytecode, u5 + 1)) { // from class: jdk.nashorn.internal.ir.debug.NashornClassReader.2
                        @Override // jdk.nashorn.internal.ir.debug.NashornClassReader.IndexInfo2, jdk.nashorn.internal.ir.debug.NashornClassReader.IndexInfo
                        public String toString() {
                            return FXMLLoader.CONTROLLER_METHOD_PREFIX + this.index + ' ' + this.cp.get(this.index2).toString();
                        }
                    });
                    u5 += 3;
                    break;
                case 16:
                    cp.add(new IndexInfo(cp, tag, readShort(bytecode, u5)));
                    u5 += 2;
                    break;
                case 18:
                    cp.add(new IndexInfo2(cp, tag, readShort(bytecode, u5), readShort(bytecode, u5 + 2)) { // from class: jdk.nashorn.internal.ir.debug.NashornClassReader.1
                        @Override // jdk.nashorn.internal.ir.debug.NashornClassReader.IndexInfo2, jdk.nashorn.internal.ir.debug.NashornClassReader.IndexInfo
                        public String toString() {
                            return FXMLLoader.CONTROLLER_METHOD_PREFIX + this.index + ' ' + this.cp.get(this.index2).toString();
                        }
                    });
                    u5 += 4;
                    break;
            }
            i2++;
        }
        readShort(bytecode, u5);
        int u7 = u5 + 2;
        int cls = readShort(bytecode, u7);
        String thisClassName = cp.get(cls).toString();
        int u8 = u7 + 2 + 2;
        int ifc = readShort(bytecode, u8);
        int u9 = u8 + 2 + (ifc * 2);
        int fc = readShort(bytecode, u9);
        int u10 = u9 + 2;
        for (int i3 = 0; i3 < fc; i3++) {
            int u11 = u10 + 2;
            readShort(bytecode, u11);
            int u12 = u11 + 2 + 2;
            int ac2 = readShort(bytecode, u12);
            u10 = u12 + 2;
            for (int j2 = 0; j2 < ac2; j2++) {
                int u13 = u10 + 2;
                u10 = u13 + 4 + readInt(bytecode, u13);
            }
        }
        int mc = readShort(bytecode, u10);
        int u14 = u10 + 2;
        for (int i4 = 0; i4 < mc; i4++) {
            readShort(bytecode, u14);
            int u15 = u14 + 2;
            int methodNameIndex = readShort(bytecode, u15);
            int u16 = u15 + 2;
            String methodName = cp.get(methodNameIndex).toString();
            int methodDescIndex = readShort(bytecode, u16);
            int u17 = u16 + 2;
            String methodDesc = cp.get(methodDescIndex).toString();
            int ac3 = readShort(bytecode, u17);
            u14 = u17 + 2;
            for (int j3 = 0; j3 < ac3; j3++) {
                int nameIndex = readShort(bytecode, u14);
                int u18 = u14 + 2;
                String attrName = cp.get(nameIndex).toString();
                int attrLen = readInt(bytecode, u18);
                int u19 = u18 + 4;
                if ("Code".equals(attrName)) {
                    readShort(bytecode, u19);
                    int u20 = u19 + 2;
                    readShort(bytecode, u20);
                    int u21 = u20 + 2;
                    int len2 = readInt(bytecode, u21);
                    int u22 = u21 + 4;
                    parseCode(bytecode, u22, len2, fullyQualifiedName(thisClassName, methodName, methodDesc));
                    int u23 = u22 + len2;
                    int elen = readShort(bytecode, u23);
                    int u24 = u23 + 2 + (elen * 8);
                    int ac22 = readShort(bytecode, u24);
                    u14 = u24 + 2;
                    for (int k2 = 0; k2 < ac22; k2++) {
                        int u25 = u14 + 2;
                        int aclen = readInt(bytecode, u25);
                        u14 = u25 + 4 + aclen;
                    }
                } else {
                    u14 = u19 + attrLen;
                }
            }
        }
        int ac4 = readShort(bytecode, u14);
        int u26 = u14 + 2;
        for (int i5 = 0; i5 < ac4; i5++) {
            readShort(bytecode, u26);
            int u27 = u26 + 2;
            u26 = u27 + 4 + readInt(bytecode, u27);
        }
        return thisClassName;
    }

    private static String fullyQualifiedName(String className, String methodName, String methodDesc) {
        return className + '.' + methodName + methodDesc;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private void parseCode(byte[] bytecode, int index, int len, String desc) {
        List<Label> labels = new ArrayList<>();
        this.labelMap.put(desc, labels);
        boolean wide = false;
        int i2 = index;
        while (i2 < index + len) {
            byte b2 = bytecode[i2];
            labels.add(new NashornTextifier.NashornLabel(b2, i2 - index));
            switch (b2 & 255) {
                case 16:
                case 18:
                case 188:
                    i2 += 2;
                    break;
                case 17:
                case 19:
                case 20:
                case 153:
                case 154:
                case 155:
                case 156:
                case 157:
                case 158:
                case 159:
                case 160:
                case 161:
                case 162:
                case 163:
                case 164:
                case 165:
                case 166:
                case 167:
                case 168:
                case 178:
                case 179:
                case 180:
                case 181:
                case 182:
                case 183:
                case 184:
                case 187:
                case 189:
                case 192:
                case 193:
                case 198:
                case 199:
                    i2 += 3;
                    break;
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 54:
                case 55:
                case 56:
                case 57:
                case 58:
                    i2 += wide ? 3 : 2;
                    break;
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                case 38:
                case 39:
                case 40:
                case 41:
                case 42:
                case 43:
                case 44:
                case 45:
                case 46:
                case 47:
                case 48:
                case 49:
                case 50:
                case 51:
                case 52:
                case 53:
                case 59:
                case 60:
                case 61:
                case 62:
                case 63:
                case 64:
                case 65:
                case 66:
                case 67:
                case 68:
                case 69:
                case 70:
                case 71:
                case 72:
                case 73:
                case 74:
                case 75:
                case 76:
                case 77:
                case 78:
                case 79:
                case 80:
                case 81:
                case 82:
                case 83:
                case 84:
                case 85:
                case 86:
                case 87:
                case 88:
                case 89:
                case 90:
                case 91:
                case 92:
                case 93:
                case 94:
                case 95:
                case 96:
                case 97:
                case 98:
                case 99:
                case 100:
                case 101:
                case 102:
                case 103:
                case 104:
                case 105:
                case 106:
                case 107:
                case 108:
                case 109:
                case 110:
                case 111:
                case 112:
                case 113:
                case 114:
                case 115:
                case 116:
                case 117:
                case 118:
                case 119:
                case 120:
                case 121:
                case 122:
                case 123:
                case 124:
                case 125:
                case 126:
                case 127:
                case 128:
                case 129:
                case 130:
                case 131:
                case 133:
                case 134:
                case 135:
                case 136:
                case 137:
                case 138:
                case 139:
                case 140:
                case 141:
                case 142:
                case 143:
                case 144:
                case 145:
                case 146:
                case 147:
                case 148:
                case 149:
                case 150:
                case 151:
                case 152:
                case 172:
                case 173:
                case 174:
                case 175:
                case 176:
                case 177:
                case 190:
                case 191:
                case 194:
                case 195:
                default:
                    i2++;
                    break;
                case 132:
                    i2 += wide ? 5 : 3;
                    break;
                case 169:
                    i2 += wide ? 4 : 2;
                    break;
                case 170:
                    do {
                        i2++;
                    } while (((i2 - index) & 3) != 0);
                    readInt(bytecode, i2);
                    int i3 = i2 + 4;
                    int lo = readInt(bytecode, i3);
                    int i4 = i3 + 4;
                    int hi = readInt(bytecode, i4);
                    i2 = i4 + 4 + (4 * ((hi - lo) + 1));
                    break;
                case 171:
                    do {
                        i2++;
                    } while (((i2 - index) & 3) != 0);
                    readInt(bytecode, i2);
                    int i5 = i2 + 4;
                    int npairs = readInt(bytecode, i5);
                    i2 = i5 + 4 + (8 * npairs);
                    break;
                case 185:
                case 186:
                case 200:
                case 201:
                    i2 += 5;
                    break;
                case 196:
                    wide = true;
                    i2++;
                    break;
                case 197:
                    i2 += 4;
                    break;
            }
            if (wide) {
                wide = false;
            }
        }
    }

    @Override // jdk.internal.org.objectweb.asm.ClassReader
    public void accept(ClassVisitor classVisitor, Attribute[] attrs, int flags) {
        super.accept(classVisitor, attrs, flags);
    }

    @Override // jdk.internal.org.objectweb.asm.ClassReader
    protected Label readLabel(int offset, Label[] labels) {
        Label label = super.readLabel(offset, labels);
        label.info = Integer.valueOf(offset);
        return label;
    }

    /* loaded from: nashorn.jar:jdk/nashorn/internal/ir/debug/NashornClassReader$Constant.class */
    private static abstract class Constant {
        protected ArrayList<Constant> cp;
        protected int tag;

        protected Constant(ArrayList<Constant> cp, int tag) {
            this.cp = cp;
            this.tag = tag;
        }

        final String getType() {
            String str = NashornClassReader.type[this.tag];
            while (true) {
                String str2 = str;
                if (str2.length() < 16) {
                    str = str2 + " ";
                } else {
                    return str2;
                }
            }
        }
    }

    /* loaded from: nashorn.jar:jdk/nashorn/internal/ir/debug/NashornClassReader$IndexInfo.class */
    private static class IndexInfo extends Constant {
        protected final int index;

        IndexInfo(ArrayList<Constant> cp, int tag, int index) {
            super(cp, tag);
            this.index = index;
        }

        public String toString() {
            return this.cp.get(this.index).toString();
        }
    }

    /* loaded from: nashorn.jar:jdk/nashorn/internal/ir/debug/NashornClassReader$IndexInfo2.class */
    private static class IndexInfo2 extends IndexInfo {
        protected final int index2;

        IndexInfo2(ArrayList<Constant> cp, int tag, int index, int index2) {
            super(cp, tag, index);
            this.index2 = index2;
        }

        @Override // jdk.nashorn.internal.ir.debug.NashornClassReader.IndexInfo
        public String toString() {
            return super.toString() + ' ' + this.cp.get(this.index2).toString();
        }
    }

    /* loaded from: nashorn.jar:jdk/nashorn/internal/ir/debug/NashornClassReader$DirectInfo.class */
    private static class DirectInfo<T> extends Constant {
        protected final T info;

        DirectInfo(ArrayList<Constant> cp, int tag, T info) {
            super(cp, tag);
            this.info = info;
        }

        public String toString() {
            return this.info.toString();
        }
    }
}
