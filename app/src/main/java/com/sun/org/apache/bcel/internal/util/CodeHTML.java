package com.sun.org.apache.bcel.internal.util;

import com.sun.org.apache.bcel.internal.Constants;
import com.sun.org.apache.bcel.internal.classfile.Attribute;
import com.sun.org.apache.bcel.internal.classfile.ClassFormatException;
import com.sun.org.apache.bcel.internal.classfile.Code;
import com.sun.org.apache.bcel.internal.classfile.CodeException;
import com.sun.org.apache.bcel.internal.classfile.ConstantFieldref;
import com.sun.org.apache.bcel.internal.classfile.ConstantInterfaceMethodref;
import com.sun.org.apache.bcel.internal.classfile.ConstantMethodref;
import com.sun.org.apache.bcel.internal.classfile.ConstantNameAndType;
import com.sun.org.apache.bcel.internal.classfile.ConstantPool;
import com.sun.org.apache.bcel.internal.classfile.LocalVariable;
import com.sun.org.apache.bcel.internal.classfile.LocalVariableTable;
import com.sun.org.apache.bcel.internal.classfile.Method;
import com.sun.org.apache.bcel.internal.classfile.Utility;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.BitSet;
import javafx.fxml.FXMLLoader;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/util/CodeHTML.class */
final class CodeHTML implements Constants {
    private String class_name;
    private Method[] methods;
    private PrintWriter file;
    private BitSet goto_set;
    private ConstantPool constant_pool;
    private ConstantHTML constant_html;
    private static boolean wide = false;

    CodeHTML(String dir, String class_name, Method[] methods, ConstantPool constant_pool, ConstantHTML constant_html) throws IOException, ClassFormatException {
        this.class_name = class_name;
        this.methods = methods;
        this.constant_pool = constant_pool;
        this.constant_html = constant_html;
        this.file = new PrintWriter(new FileOutputStream(dir + class_name + "_code.html"));
        this.file.println("<HTML><BODY BGCOLOR=\"#C0C0C0\">");
        for (int i2 = 0; i2 < methods.length; i2++) {
            writeMethod(methods[i2], i2);
        }
        this.file.println("</BODY></HTML>");
        this.file.close();
    }

    private final String codeToHTML(ByteSequence bytes, int method_number) throws IOException, ClassFormatException {
        int vindex;
        int constant;
        int class_index;
        int index;
        int vindex2;
        short opcode = (short) bytes.readUnsignedByte();
        int default_offset = 0;
        int no_pad_bytes = 0;
        StringBuffer buf = new StringBuffer("<TT>" + OPCODE_NAMES[opcode] + "</TT></TD><TD>");
        if (opcode == 170 || opcode == 171) {
            int remainder = bytes.getIndex() % 4;
            no_pad_bytes = remainder == 0 ? 0 : 4 - remainder;
            for (int i2 = 0; i2 < no_pad_bytes; i2++) {
                bytes.readByte();
            }
            default_offset = bytes.readInt();
        }
        switch (opcode) {
            case 18:
                int index2 = bytes.readUnsignedByte();
                buf.append("<A HREF=\"" + this.class_name + "_cp.html#cp" + index2 + "\" TARGET=\"ConstantPool\">" + Class2HTML.toHTML(this.constant_pool.constantToString(index2, this.constant_pool.getConstant(index2).getTag())) + "</a>");
                break;
            case 19:
            case 20:
                int index3 = bytes.readShort();
                buf.append("<A HREF=\"" + this.class_name + "_cp.html#cp" + index3 + "\" TARGET=\"ConstantPool\">" + Class2HTML.toHTML(this.constant_pool.constantToString(index3, this.constant_pool.getConstant(index3).getTag())) + "</a>");
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
            case 169:
                if (wide) {
                    vindex2 = bytes.readShort();
                    wide = false;
                } else {
                    vindex2 = bytes.readUnsignedByte();
                }
                buf.append(FXMLLoader.RESOURCE_KEY_PREFIX + vindex2);
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
            case 186:
            case 190:
            case 191:
            case 194:
            case 195:
            default:
                if (NO_OF_OPERANDS[opcode] > 0) {
                    for (int i3 = 0; i3 < TYPE_OF_OPERANDS[opcode].length; i3++) {
                        switch (TYPE_OF_OPERANDS[opcode][i3]) {
                            case 8:
                                buf.append(bytes.readUnsignedByte());
                                break;
                            case 9:
                                buf.append((int) bytes.readShort());
                                break;
                            case 10:
                                buf.append(bytes.readInt());
                                break;
                            default:
                                System.err.println("Unreachable default case reached!");
                                System.exit(-1);
                                break;
                        }
                        buf.append("&nbsp;");
                    }
                    break;
                }
                break;
            case 132:
                if (wide) {
                    vindex = bytes.readShort();
                    constant = bytes.readShort();
                    wide = false;
                } else {
                    vindex = bytes.readUnsignedByte();
                    constant = bytes.readByte();
                }
                buf.append(FXMLLoader.RESOURCE_KEY_PREFIX + vindex + " " + constant);
                break;
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
            case 198:
            case 199:
                int index4 = (bytes.getIndex() + bytes.readShort()) - 1;
                buf.append("<A HREF=\"#code" + method_number + "@" + index4 + "\">" + index4 + "</A>");
                break;
            case 170:
                int low = bytes.readInt();
                int high = bytes.readInt();
                int offset = ((bytes.getIndex() - 12) - no_pad_bytes) - 1;
                int default_offset2 = default_offset + offset;
                buf.append("<TABLE BORDER=1><TR>");
                int[] jump_table = new int[(high - low) + 1];
                for (int i4 = 0; i4 < jump_table.length; i4++) {
                    jump_table[i4] = offset + bytes.readInt();
                    buf.append("<TH>" + (low + i4) + "</TH>");
                }
                buf.append("<TH>default</TH></TR>\n<TR>");
                for (int i5 = 0; i5 < jump_table.length; i5++) {
                    buf.append("<TD><A HREF=\"#code" + method_number + "@" + jump_table[i5] + "\">" + jump_table[i5] + "</A></TD>");
                }
                buf.append("<TD><A HREF=\"#code" + method_number + "@" + default_offset2 + "\">" + default_offset2 + "</A></TD></TR>\n</TABLE>\n");
                break;
            case 171:
                int npairs = bytes.readInt();
                int offset2 = ((bytes.getIndex() - 8) - no_pad_bytes) - 1;
                int[] jump_table2 = new int[npairs];
                int default_offset3 = default_offset + offset2;
                buf.append("<TABLE BORDER=1><TR>");
                for (int i6 = 0; i6 < npairs; i6++) {
                    int match = bytes.readInt();
                    jump_table2[i6] = offset2 + bytes.readInt();
                    buf.append("<TH>" + match + "</TH>");
                }
                buf.append("<TH>default</TH></TR>\n<TR>");
                for (int i7 = 0; i7 < npairs; i7++) {
                    buf.append("<TD><A HREF=\"#code" + method_number + "@" + jump_table2[i7] + "\">" + jump_table2[i7] + "</A></TD>");
                }
                buf.append("<TD><A HREF=\"#code" + method_number + "@" + default_offset3 + "\">" + default_offset3 + "</A></TD></TR>\n</TABLE>\n");
                break;
            case 178:
            case 179:
            case 180:
            case 181:
                int index5 = bytes.readShort();
                ConstantFieldref c1 = (ConstantFieldref) this.constant_pool.getConstant(index5, (byte) 9);
                int class_index2 = c1.getClassIndex();
                String name = this.constant_pool.getConstantString(class_index2, (byte) 7);
                String name2 = Utility.compactClassName(name, false);
                int index6 = c1.getNameAndTypeIndex();
                String field_name = this.constant_pool.constantToString(index6, (byte) 12);
                if (name2.equals(this.class_name)) {
                    buf.append("<A HREF=\"" + this.class_name + "_methods.html#field" + field_name + "\" TARGET=Methods>" + field_name + "</A>\n");
                    break;
                } else {
                    buf.append(this.constant_html.referenceConstant(class_index2) + "." + field_name);
                    break;
                }
            case 182:
            case 183:
            case 184:
            case 185:
                int m_index = bytes.readShort();
                if (opcode == 185) {
                    bytes.readUnsignedByte();
                    bytes.readUnsignedByte();
                    ConstantInterfaceMethodref c2 = (ConstantInterfaceMethodref) this.constant_pool.getConstant(m_index, (byte) 11);
                    class_index = c2.getClassIndex();
                    this.constant_pool.constantToString(c2);
                    index = c2.getNameAndTypeIndex();
                } else {
                    ConstantMethodref c3 = (ConstantMethodref) this.constant_pool.getConstant(m_index, (byte) 10);
                    class_index = c3.getClassIndex();
                    this.constant_pool.constantToString(c3);
                    index = c3.getNameAndTypeIndex();
                }
                String name3 = Class2HTML.referenceClass(class_index);
                String str = Class2HTML.toHTML(this.constant_pool.constantToString(this.constant_pool.getConstant(index, (byte) 12)));
                ConstantNameAndType c22 = (ConstantNameAndType) this.constant_pool.getConstant(index, (byte) 12);
                String signature = this.constant_pool.constantToString(c22.getSignatureIndex(), (byte) 1);
                String[] args = Utility.methodSignatureArgumentTypes(signature, false);
                String type = Utility.methodSignatureReturnType(signature, false);
                buf.append(name3 + ".<A HREF=\"" + this.class_name + "_cp.html#cp" + m_index + "\" TARGET=ConstantPool>" + str + "</A>(");
                for (int i8 = 0; i8 < args.length; i8++) {
                    buf.append(Class2HTML.referenceType(args[i8]));
                    if (i8 < args.length - 1) {
                        buf.append(", ");
                    }
                }
                buf.append("):" + Class2HTML.referenceType(type));
                break;
            case 187:
            case 192:
            case 193:
                int index7 = bytes.readShort();
                buf.append(this.constant_html.referenceConstant(index7));
                break;
            case 188:
                buf.append("<FONT COLOR=\"#00FF00\">" + TYPE_NAMES[bytes.readByte()] + "</FONT>");
                break;
            case 189:
                int index8 = bytes.readShort();
                buf.append(this.constant_html.referenceConstant(index8));
                break;
            case 196:
                wide = true;
                buf.append("(wide)");
                break;
            case 197:
                int index9 = bytes.readShort();
                int dimensions = bytes.readByte();
                buf.append(this.constant_html.referenceConstant(index9) + CallSiteDescriptor.TOKEN_DELIMITER + dimensions + "-dimensional");
                break;
            case 200:
            case 201:
                int windex = (bytes.getIndex() + bytes.readInt()) - 1;
                buf.append("<A HREF=\"#code" + method_number + "@" + windex + "\">" + windex + "</A>");
                break;
        }
        buf.append("</TD>");
        return buf.toString();
    }

    private final void findGotos(ByteSequence bytes, Method method, Code code) throws IOException, ClassFormatException {
        this.goto_set = new BitSet(bytes.available());
        if (code != null) {
            CodeException[] ce = code.getExceptionTable();
            int len = ce.length;
            for (int i2 = 0; i2 < len; i2++) {
                this.goto_set.set(ce[i2].getStartPC());
                this.goto_set.set(ce[i2].getEndPC());
                this.goto_set.set(ce[i2].getHandlerPC());
            }
            Attribute[] attributes = code.getAttributes();
            int i3 = 0;
            while (true) {
                if (i3 >= attributes.length) {
                    break;
                }
                if (attributes[i3].getTag() != 5) {
                    i3++;
                } else {
                    LocalVariable[] vars = ((LocalVariableTable) attributes[i3]).getLocalVariableTable();
                    for (int j2 = 0; j2 < vars.length; j2++) {
                        int start = vars[j2].getStartPC();
                        int end = start + vars[j2].getLength();
                        this.goto_set.set(start);
                        this.goto_set.set(end);
                    }
                }
            }
        }
        int i4 = 0;
        while (bytes.available() > 0) {
            int opcode = bytes.readUnsignedByte();
            switch (opcode) {
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
                case 198:
                case 199:
                    int index = (bytes.getIndex() + bytes.readShort()) - 1;
                    this.goto_set.set(index);
                    break;
                case 169:
                case 172:
                case 173:
                case 174:
                case 175:
                case 176:
                case 177:
                case 178:
                case 179:
                case 180:
                case 181:
                case 182:
                case 183:
                case 184:
                case 185:
                case 186:
                case 187:
                case 188:
                case 189:
                case 190:
                case 191:
                case 192:
                case 193:
                case 194:
                case 195:
                case 196:
                case 197:
                default:
                    bytes.unreadByte();
                    codeToHTML(bytes, 0);
                    break;
                case 170:
                case 171:
                    int remainder = bytes.getIndex() % 4;
                    int no_pad_bytes = remainder == 0 ? 0 : 4 - remainder;
                    for (int j3 = 0; j3 < no_pad_bytes; j3++) {
                        bytes.readByte();
                    }
                    int default_offset = bytes.readInt();
                    if (opcode == 170) {
                        int low = bytes.readInt();
                        int high = bytes.readInt();
                        int offset = ((bytes.getIndex() - 12) - no_pad_bytes) - 1;
                        this.goto_set.set(default_offset + offset);
                        for (int j4 = 0; j4 < (high - low) + 1; j4++) {
                            int index2 = offset + bytes.readInt();
                            this.goto_set.set(index2);
                        }
                        break;
                    } else {
                        int npairs = bytes.readInt();
                        int offset2 = ((bytes.getIndex() - 8) - no_pad_bytes) - 1;
                        this.goto_set.set(default_offset + offset2);
                        for (int j5 = 0; j5 < npairs; j5++) {
                            bytes.readInt();
                            int index3 = offset2 + bytes.readInt();
                            this.goto_set.set(index3);
                        }
                        break;
                    }
                case 200:
                case 201:
                    int index4 = (bytes.getIndex() + bytes.readInt()) - 1;
                    this.goto_set.set(index4);
                    break;
            }
            i4++;
        }
    }

    private void writeMethod(Method method, int method_number) throws IOException, ClassFormatException {
        String str;
        String signature = method.getSignature();
        String[] args = Utility.methodSignatureArgumentTypes(signature, false);
        String type = Utility.methodSignatureReturnType(signature, false);
        String name = method.getName();
        String html_name = Class2HTML.toHTML(name);
        String access = Utility.accessToString(method.getAccessFlags());
        String access2 = Utility.replace(access, " ", "&nbsp;");
        Attribute[] attributes = method.getAttributes();
        this.file.print("<P><B><FONT COLOR=\"#FF0000\">" + access2 + "</FONT>&nbsp;<A NAME=method" + method_number + ">" + Class2HTML.referenceType(type) + "</A>&nbsp<A HREF=\"" + this.class_name + "_methods.html#method" + method_number + "\" TARGET=Methods>" + html_name + "</A>(");
        for (int i2 = 0; i2 < args.length; i2++) {
            this.file.print(Class2HTML.referenceType(args[i2]));
            if (i2 < args.length - 1) {
                this.file.print(",&nbsp;");
            }
        }
        this.file.println(")</B></P>");
        Code c2 = null;
        byte[] code = null;
        if (attributes.length > 0) {
            this.file.print("<H4>Attributes</H4><UL>\n");
            for (int i3 = 0; i3 < attributes.length; i3++) {
                byte tag = attributes[i3].getTag();
                if (tag != -1) {
                    this.file.print("<LI><A HREF=\"" + this.class_name + "_attributes.html#method" + method_number + "@" + i3 + "\" TARGET=Attributes>" + ATTRIBUTE_NAMES[tag] + "</A></LI>\n");
                } else {
                    this.file.print("<LI>" + ((Object) attributes[i3]) + "</LI>");
                }
                if (tag == 2) {
                    c2 = (Code) attributes[i3];
                    Attribute[] attributes2 = c2.getAttributes();
                    code = c2.getCode();
                    this.file.print("<UL>");
                    for (int j2 = 0; j2 < attributes2.length; j2++) {
                        this.file.print("<LI><A HREF=\"" + this.class_name + "_attributes.html#method" + method_number + "@" + i3 + "@" + j2 + "\" TARGET=Attributes>" + ATTRIBUTE_NAMES[attributes2[j2].getTag()] + "</A></LI>\n");
                    }
                    this.file.print("</UL>");
                }
            }
            this.file.println("</UL>");
        }
        if (code != null) {
            ByteSequence stream = new ByteSequence(code);
            stream.mark(stream.available());
            findGotos(stream, method, c2);
            stream.reset();
            this.file.println("<TABLE BORDER=0><TR><TH ALIGN=LEFT>Byte<BR>offset</TH><TH ALIGN=LEFT>Instruction</TH><TH ALIGN=LEFT>Argument</TH>");
            int i4 = 0;
            while (stream.available() > 0) {
                int offset = stream.getIndex();
                String str2 = codeToHTML(stream, method_number);
                String anchor = "";
                if (this.goto_set.get(offset)) {
                    anchor = "<A NAME=code" + method_number + "@" + offset + "></A>";
                }
                if (stream.getIndex() == code.length) {
                    str = "<A NAME=code" + method_number + "@" + code.length + ">" + offset + "</A>";
                } else {
                    str = "" + offset;
                }
                String anchor2 = str;
                this.file.println("<TR VALIGN=TOP><TD>" + anchor2 + "</TD><TD>" + anchor + str2 + "</TR>");
                i4++;
            }
            this.file.println("<TR><TD> </A></TD></TR>");
            this.file.println("</TABLE>");
        }
    }
}
