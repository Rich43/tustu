package javax.swing.text.html.parser;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.BitSet;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.awt.AppContext;

/* loaded from: rt.jar:javax/swing/text/html/parser/DTD.class */
public class DTD implements DTDConstants {
    public String name;
    public Vector<Element> elements = new Vector<>();
    public Hashtable<String, Element> elementHash = new Hashtable<>();
    public Hashtable<Object, Entity> entityHash = new Hashtable<>();
    public final Element pcdata = getElement("#pcdata");
    public final Element html = getElement("html");
    public final Element meta = getElement("meta");
    public final Element base = getElement("base");
    public final Element isindex = getElement("isindex");
    public final Element head = getElement("head");
    public final Element body = getElement("body");
    public final Element applet = getElement("applet");
    public final Element param = getElement(Constants.ELEMNAME_PARAMVARIABLE_STRING);

    /* renamed from: p, reason: collision with root package name */
    public final Element f12855p = getElement("p");
    public final Element title = getElement("title");
    final Element style = getElement(Constants.ATTRNAME_STYLE);
    final Element link = getElement("link");
    final Element script = getElement("script");
    public static final int FILE_VERSION = 1;
    private static final Object DTD_HASH_KEY = new Object();

    protected DTD(String str) {
        this.name = str;
        defEntity("#RE", 65536, 13);
        defEntity("#RS", 65536, 10);
        defEntity("#SPACE", 65536, 32);
        defineElement("unknown", 17, false, true, null, null, null, null);
    }

    public String getName() {
        return this.name;
    }

    public Entity getEntity(String str) {
        return this.entityHash.get(str);
    }

    public Entity getEntity(int i2) {
        return this.entityHash.get(Integer.valueOf(i2));
    }

    boolean elementExists(String str) {
        return ("unknown".equals(str) || this.elementHash.get(str) == null) ? false : true;
    }

    public Element getElement(String str) {
        Element element = this.elementHash.get(str);
        if (element == null) {
            element = new Element(str, this.elements.size());
            this.elements.addElement(element);
            this.elementHash.put(str, element);
        }
        return element;
    }

    public Element getElement(int i2) {
        return this.elements.elementAt(i2);
    }

    public Entity defineEntity(String str, int i2, char[] cArr) {
        Entity entity = this.entityHash.get(str);
        if (entity == null) {
            entity = new Entity(str, i2, cArr);
            this.entityHash.put(str, entity);
            if ((i2 & 65536) != 0 && cArr.length == 1) {
                switch (i2 & (-65537)) {
                    case 1:
                    case 11:
                        this.entityHash.put(Integer.valueOf(cArr[0]), entity);
                        break;
                }
            }
        }
        return entity;
    }

    public Element defineElement(String str, int i2, boolean z2, boolean z3, ContentModel contentModel, BitSet bitSet, BitSet bitSet2, AttributeList attributeList) {
        Element element = getElement(str);
        element.type = i2;
        element.oStart = z2;
        element.oEnd = z3;
        element.content = contentModel;
        element.exclusions = bitSet;
        element.inclusions = bitSet2;
        element.atts = attributeList;
        return element;
    }

    public void defineAttributes(String str, AttributeList attributeList) {
        getElement(str).atts = attributeList;
    }

    public Entity defEntity(String str, int i2, int i3) {
        return defineEntity(str, i2, new char[]{(char) i3});
    }

    protected Entity defEntity(String str, int i2, String str2) {
        int length = str2.length();
        char[] cArr = new char[length];
        str2.getChars(0, length, cArr, 0);
        return defineEntity(str, i2, cArr);
    }

    protected Element defElement(String str, int i2, boolean z2, boolean z3, ContentModel contentModel, String[] strArr, String[] strArr2, AttributeList attributeList) {
        BitSet bitSet = null;
        if (strArr != null && strArr.length > 0) {
            bitSet = new BitSet();
            for (String str2 : strArr) {
                if (str2.length() > 0) {
                    bitSet.set(getElement(str2).getIndex());
                }
            }
        }
        BitSet bitSet2 = null;
        if (strArr2 != null && strArr2.length > 0) {
            bitSet2 = new BitSet();
            for (String str3 : strArr2) {
                if (str3.length() > 0) {
                    bitSet2.set(getElement(str3).getIndex());
                }
            }
        }
        return defineElement(str, i2, z2, z3, contentModel, bitSet, bitSet2, attributeList);
    }

    protected AttributeList defAttributeList(String str, int i2, int i3, String str2, String str3, AttributeList attributeList) {
        Vector vector = null;
        if (str3 != null) {
            vector = new Vector();
            StringTokenizer stringTokenizer = new StringTokenizer(str3, CallSiteDescriptor.OPERATOR_DELIMITER);
            while (stringTokenizer.hasMoreTokens()) {
                String strNextToken = stringTokenizer.nextToken();
                if (strNextToken.length() > 0) {
                    vector.addElement(strNextToken);
                }
            }
        }
        return new AttributeList(str, i2, i3, str2, vector, attributeList);
    }

    protected ContentModel defContentModel(int i2, Object obj, ContentModel contentModel) {
        return new ContentModel(i2, obj, contentModel);
    }

    public String toString() {
        return this.name;
    }

    public static void putDTDHash(String str, DTD dtd) {
        getDtdHash().put(str, dtd);
    }

    public static DTD getDTD(String str) throws IOException {
        String lowerCase = str.toLowerCase();
        DTD dtd = getDtdHash().get(lowerCase);
        if (dtd == null) {
            dtd = new DTD(lowerCase);
        }
        return dtd;
    }

    private static Hashtable<String, DTD> getDtdHash() {
        AppContext appContext = AppContext.getAppContext();
        Hashtable<String, DTD> hashtable = (Hashtable) appContext.get(DTD_HASH_KEY);
        if (hashtable == null) {
            hashtable = new Hashtable<>();
            appContext.put(DTD_HASH_KEY, hashtable);
        }
        return hashtable;
    }

    public void read(DataInputStream dataInputStream) throws IOException {
        if (dataInputStream.readInt() != 1) {
        }
        String[] strArr = new String[dataInputStream.readShort()];
        for (int i2 = 0; i2 < strArr.length; i2++) {
            strArr[i2] = dataInputStream.readUTF();
        }
        int i3 = dataInputStream.readShort();
        for (int i4 = 0; i4 < i3; i4++) {
            defEntity(strArr[dataInputStream.readShort()], dataInputStream.readByte() | 65536, dataInputStream.readUTF());
        }
        int i5 = dataInputStream.readShort();
        for (int i6 = 0; i6 < i5; i6++) {
            short s2 = dataInputStream.readShort();
            byte b2 = dataInputStream.readByte();
            byte b3 = dataInputStream.readByte();
            defElement(strArr[s2], b2, (b3 & 1) != 0, (b3 & 2) != 0, readContentModel(dataInputStream, strArr), readNameArray(dataInputStream, strArr), readNameArray(dataInputStream, strArr), readAttributeList(dataInputStream, strArr));
        }
    }

    private ContentModel readContentModel(DataInputStream dataInputStream, String[] strArr) throws IOException {
        switch (dataInputStream.readByte()) {
            case 0:
                return null;
            case 1:
                return defContentModel(dataInputStream.readByte(), readContentModel(dataInputStream, strArr), readContentModel(dataInputStream, strArr));
            case 2:
                return defContentModel(dataInputStream.readByte(), getElement(strArr[dataInputStream.readShort()]), readContentModel(dataInputStream, strArr));
            default:
                throw new IOException("bad bdtd");
        }
    }

    private String[] readNameArray(DataInputStream dataInputStream, String[] strArr) throws IOException {
        int i2 = dataInputStream.readShort();
        if (i2 == 0) {
            return null;
        }
        String[] strArr2 = new String[i2];
        for (int i3 = 0; i3 < i2; i3++) {
            strArr2[i3] = strArr[dataInputStream.readShort()];
        }
        return strArr2;
    }

    private AttributeList readAttributeList(DataInputStream dataInputStream, String[] strArr) throws IOException {
        AttributeList attributeList = null;
        for (int i2 = dataInputStream.readByte(); i2 > 0; i2--) {
            short s2 = dataInputStream.readShort();
            byte b2 = dataInputStream.readByte();
            byte b3 = dataInputStream.readByte();
            short s3 = dataInputStream.readShort();
            String str = s3 == -1 ? null : strArr[s3];
            Vector vector = null;
            int i3 = dataInputStream.readShort();
            if (i3 > 0) {
                vector = new Vector(i3);
                for (int i4 = 0; i4 < i3; i4++) {
                    vector.addElement(strArr[dataInputStream.readShort()]);
                }
            }
            attributeList = new AttributeList(strArr[s2], b2, b3, str, vector, attributeList);
        }
        return attributeList;
    }
}
