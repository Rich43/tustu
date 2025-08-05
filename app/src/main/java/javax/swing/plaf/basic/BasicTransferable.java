package javax.swing.plaf.basic;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import javax.swing.plaf.UIResource;
import sun.awt.datatransfer.DataTransferer;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicTransferable.class */
class BasicTransferable implements Transferable, UIResource {
    protected String plainData;
    protected String htmlData;
    private static DataFlavor[] htmlFlavors;
    private static DataFlavor[] stringFlavors;
    private static DataFlavor[] plainFlavors;

    static {
        try {
            htmlFlavors = new DataFlavor[3];
            htmlFlavors[0] = new DataFlavor("text/html;class=java.lang.String");
            htmlFlavors[1] = new DataFlavor("text/html;class=java.io.Reader");
            htmlFlavors[2] = new DataFlavor("text/html;charset=unicode;class=java.io.InputStream");
            plainFlavors = new DataFlavor[3];
            plainFlavors[0] = new DataFlavor("text/plain;class=java.lang.String");
            plainFlavors[1] = new DataFlavor("text/plain;class=java.io.Reader");
            plainFlavors[2] = new DataFlavor("text/plain;charset=unicode;class=java.io.InputStream");
            stringFlavors = new DataFlavor[2];
            stringFlavors[0] = new DataFlavor("application/x-java-jvm-local-objectref;class=java.lang.String");
            stringFlavors[1] = DataFlavor.stringFlavor;
        } catch (ClassNotFoundException e2) {
            System.err.println("error initializing javax.swing.plaf.basic.BasicTranserable");
        }
    }

    public BasicTransferable(String str, String str2) {
        this.plainData = str;
        this.htmlData = str2;
    }

    @Override // java.awt.datatransfer.Transferable
    public DataFlavor[] getTransferDataFlavors() {
        DataFlavor[] richerFlavors = getRicherFlavors();
        int length = richerFlavors != null ? richerFlavors.length : 0;
        int length2 = isHTMLSupported() ? htmlFlavors.length : 0;
        int length3 = isPlainSupported() ? plainFlavors.length : 0;
        int length4 = isPlainSupported() ? stringFlavors.length : 0;
        DataFlavor[] dataFlavorArr = new DataFlavor[length + length2 + length3 + length4];
        int i2 = 0;
        if (length > 0) {
            System.arraycopy(richerFlavors, 0, dataFlavorArr, 0, length);
            i2 = 0 + length;
        }
        if (length2 > 0) {
            System.arraycopy(htmlFlavors, 0, dataFlavorArr, i2, length2);
            i2 += length2;
        }
        if (length3 > 0) {
            System.arraycopy(plainFlavors, 0, dataFlavorArr, i2, length3);
            i2 += length3;
        }
        if (length4 > 0) {
            System.arraycopy(stringFlavors, 0, dataFlavorArr, i2, length4);
            int i3 = i2 + length4;
        }
        return dataFlavorArr;
    }

    @Override // java.awt.datatransfer.Transferable
    public boolean isDataFlavorSupported(DataFlavor dataFlavor) {
        for (DataFlavor dataFlavor2 : getTransferDataFlavors()) {
            if (dataFlavor2.equals(dataFlavor)) {
                return true;
            }
        }
        return false;
    }

    @Override // java.awt.datatransfer.Transferable
    public Object getTransferData(DataFlavor dataFlavor) throws UnsupportedFlavorException, IOException {
        getRicherFlavors();
        if (isRicherFlavor(dataFlavor)) {
            return getRicherData(dataFlavor);
        }
        if (isHTMLFlavor(dataFlavor)) {
            String hTMLData = getHTMLData();
            String str = hTMLData == null ? "" : hTMLData;
            if (String.class.equals(dataFlavor.getRepresentationClass())) {
                return str;
            }
            if (Reader.class.equals(dataFlavor.getRepresentationClass())) {
                return new StringReader(str);
            }
            if (InputStream.class.equals(dataFlavor.getRepresentationClass())) {
                return createInputStream(dataFlavor, str);
            }
        } else if (isPlainFlavor(dataFlavor)) {
            String plainData = getPlainData();
            String str2 = plainData == null ? "" : plainData;
            if (String.class.equals(dataFlavor.getRepresentationClass())) {
                return str2;
            }
            if (Reader.class.equals(dataFlavor.getRepresentationClass())) {
                return new StringReader(str2);
            }
            if (InputStream.class.equals(dataFlavor.getRepresentationClass())) {
                return createInputStream(dataFlavor, str2);
            }
        } else if (isStringFlavor(dataFlavor)) {
            String plainData2 = getPlainData();
            return plainData2 == null ? "" : plainData2;
        }
        throw new UnsupportedFlavorException(dataFlavor);
    }

    private InputStream createInputStream(DataFlavor dataFlavor, String str) throws UnsupportedFlavorException, IOException {
        String textCharset = DataTransferer.getTextCharset(dataFlavor);
        if (textCharset == null) {
            throw new UnsupportedFlavorException(dataFlavor);
        }
        return new ByteArrayInputStream(str.getBytes(textCharset));
    }

    protected boolean isRicherFlavor(DataFlavor dataFlavor) {
        DataFlavor[] richerFlavors = getRicherFlavors();
        int length = richerFlavors != null ? richerFlavors.length : 0;
        for (int i2 = 0; i2 < length; i2++) {
            if (richerFlavors[i2].equals(dataFlavor)) {
                return true;
            }
        }
        return false;
    }

    protected DataFlavor[] getRicherFlavors() {
        return null;
    }

    protected Object getRicherData(DataFlavor dataFlavor) throws UnsupportedFlavorException {
        return null;
    }

    protected boolean isHTMLFlavor(DataFlavor dataFlavor) {
        for (DataFlavor dataFlavor2 : htmlFlavors) {
            if (dataFlavor2.equals(dataFlavor)) {
                return true;
            }
        }
        return false;
    }

    protected boolean isHTMLSupported() {
        return this.htmlData != null;
    }

    protected String getHTMLData() {
        return this.htmlData;
    }

    protected boolean isPlainFlavor(DataFlavor dataFlavor) {
        for (DataFlavor dataFlavor2 : plainFlavors) {
            if (dataFlavor2.equals(dataFlavor)) {
                return true;
            }
        }
        return false;
    }

    protected boolean isPlainSupported() {
        return this.plainData != null;
    }

    protected String getPlainData() {
        return this.plainData;
    }

    protected boolean isStringFlavor(DataFlavor dataFlavor) {
        for (DataFlavor dataFlavor2 : stringFlavors) {
            if (dataFlavor2.equals(dataFlavor)) {
                return true;
            }
        }
        return false;
    }
}
