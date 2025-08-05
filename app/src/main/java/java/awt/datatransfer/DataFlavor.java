package java.awt.datatransfer;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.io.ByteArrayInputStream;
import java.io.CharArrayReader;
import java.io.Externalizable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.OptionalDataException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import javafx.fxml.FXMLLoader;
import sun.awt.datatransfer.DataTransferer;
import sun.reflect.misc.ReflectUtil;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:java/awt/datatransfer/DataFlavor.class */
public class DataFlavor implements Externalizable, Cloneable {
    private static final long serialVersionUID = 8367026044764648243L;
    public static final String javaSerializedObjectMimeType = "application/x-java-serialized-object";
    public static final String javaJVMLocalObjectMimeType = "application/x-java-jvm-local-objectref";
    public static final String javaRemoteObjectMimeType = "application/x-java-remote-object";
    private static Comparator<DataFlavor> textFlavorComparator;
    transient int atom;
    MimeType mimeType;
    private String humanPresentableName;
    private Class<?> representationClass;
    private static final Class<InputStream> ioInputStreamClass = InputStream.class;
    public static final DataFlavor stringFlavor = createConstant((Class<?>) String.class, "Unicode String");
    public static final DataFlavor imageFlavor = createConstant("image/x-java-image; class=java.awt.Image", "Image");

    @Deprecated
    public static final DataFlavor plainTextFlavor = createConstant("text/plain; charset=unicode; class=java.io.InputStream", "Plain Text");
    public static final DataFlavor javaFileListFlavor = createConstant("application/x-java-file-list;class=java.util.List", (String) null);
    public static DataFlavor selectionHtmlFlavor = initHtmlDataFlavor("selection");
    public static DataFlavor fragmentHtmlFlavor = initHtmlDataFlavor("fragment");
    public static DataFlavor allHtmlFlavor = initHtmlDataFlavor("all");

    protected static final Class<?> tryToLoadClass(String str, ClassLoader classLoader) throws ClassNotFoundException {
        ReflectUtil.checkPackageAccess(str);
        try {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkPermission(SecurityConstants.GET_CLASSLOADER_PERMISSION);
            }
            try {
                return Class.forName(str, true, ClassLoader.getSystemClassLoader());
            } catch (ClassNotFoundException e2) {
                ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
                if (contextClassLoader != null) {
                    try {
                        return Class.forName(str, true, contextClassLoader);
                    } catch (ClassNotFoundException e3) {
                        return Class.forName(str, true, classLoader);
                    }
                }
                return Class.forName(str, true, classLoader);
            }
        } catch (SecurityException e4) {
        }
    }

    private static DataFlavor createConstant(Class<?> cls, String str) {
        try {
            return new DataFlavor(cls, str);
        } catch (Exception e2) {
            return null;
        }
    }

    private static DataFlavor createConstant(String str, String str2) {
        try {
            return new DataFlavor(str, str2);
        } catch (Exception e2) {
            return null;
        }
    }

    private static DataFlavor initHtmlDataFlavor(String str) {
        try {
            return new DataFlavor("text/html; class=java.lang.String;document=" + str + ";charset=Unicode");
        } catch (Exception e2) {
            return null;
        }
    }

    public DataFlavor() {
    }

    private DataFlavor(String str, String str2, MimeTypeParameterList mimeTypeParameterList, Class<?> cls, String str3) {
        if (str == null) {
            throw new NullPointerException("primaryType");
        }
        if (str2 == null) {
            throw new NullPointerException("subType");
        }
        if (cls == null) {
            throw new NullPointerException("representationClass");
        }
        mimeTypeParameterList = mimeTypeParameterList == null ? new MimeTypeParameterList() : mimeTypeParameterList;
        mimeTypeParameterList.set(Constants.ATTRNAME_CLASS, cls.getName());
        if (str3 == null) {
            str3 = mimeTypeParameterList.get("humanPresentableName");
            if (str3 == null) {
                str3 = str + "/" + str2;
            }
        }
        try {
            this.mimeType = new MimeType(str, str2, mimeTypeParameterList);
            this.representationClass = cls;
            this.humanPresentableName = str3;
            this.mimeType.removeParameter("humanPresentableName");
        } catch (MimeTypeParseException e2) {
            throw new IllegalArgumentException("MimeType Parse Exception: " + e2.getMessage());
        }
    }

    public DataFlavor(Class<?> cls, String str) {
        this("application", "x-java-serialized-object", null, cls, str);
        if (cls == null) {
            throw new NullPointerException("representationClass");
        }
    }

    public DataFlavor(String str, String str2) {
        if (str == null) {
            throw new NullPointerException("mimeType");
        }
        try {
            initialize(str, str2, getClass().getClassLoader());
        } catch (MimeTypeParseException e2) {
            throw new IllegalArgumentException("failed to parse:" + str);
        } catch (ClassNotFoundException e3) {
            throw new IllegalArgumentException("can't find specified class: " + e3.getMessage());
        }
    }

    public DataFlavor(String str, String str2, ClassLoader classLoader) throws ClassNotFoundException {
        if (str == null) {
            throw new NullPointerException("mimeType");
        }
        try {
            initialize(str, str2, classLoader);
        } catch (MimeTypeParseException e2) {
            throw new IllegalArgumentException("failed to parse:" + str);
        }
    }

    public DataFlavor(String str) throws ClassNotFoundException {
        if (str == null) {
            throw new NullPointerException("mimeType");
        }
        try {
            initialize(str, null, getClass().getClassLoader());
        } catch (MimeTypeParseException e2) {
            throw new IllegalArgumentException("failed to parse:" + str);
        }
    }

    private void initialize(String str, String str2, ClassLoader classLoader) throws ClassNotFoundException, MimeTypeParseException {
        if (str == null) {
            throw new NullPointerException("mimeType");
        }
        this.mimeType = new MimeType(str);
        String parameter = getParameter(Constants.ATTRNAME_CLASS);
        if (parameter == null) {
            if (javaSerializedObjectMimeType.equals(this.mimeType.getBaseType())) {
                throw new IllegalArgumentException("no representation class specified for:" + str);
            }
            this.representationClass = InputStream.class;
        } else {
            this.representationClass = tryToLoadClass(parameter, classLoader);
        }
        this.mimeType.setParameter(Constants.ATTRNAME_CLASS, this.representationClass.getName());
        if (str2 == null) {
            str2 = this.mimeType.getParameter("humanPresentableName");
            if (str2 == null) {
                str2 = this.mimeType.getPrimaryType() + "/" + this.mimeType.getSubType();
            }
        }
        this.humanPresentableName = str2;
        this.mimeType.removeParameter("humanPresentableName");
    }

    public String toString() {
        return getClass().getName() + "[" + paramString() + "]";
    }

    private String paramString() {
        String str;
        String str2;
        String str3 = "mimetype=";
        if (this.mimeType == null) {
            str = str3 + FXMLLoader.NULL_KEYWORD;
        } else {
            str = str3 + this.mimeType.getBaseType();
        }
        String str4 = str + ";representationclass=";
        if (this.representationClass == null) {
            str2 = str4 + FXMLLoader.NULL_KEYWORD;
        } else {
            str2 = str4 + this.representationClass.getName();
        }
        if (DataTransferer.isFlavorCharsetTextType(this) && (isRepresentationClassInputStream() || isRepresentationClassByteBuffer() || byte[].class.equals(this.representationClass))) {
            str2 = str2 + ";charset=" + DataTransferer.getTextCharset(this);
        }
        return str2;
    }

    public static final DataFlavor getTextPlainUnicodeFlavor() {
        String defaultUnicodeEncoding = null;
        DataTransferer dataTransferer = DataTransferer.getInstance();
        if (dataTransferer != null) {
            defaultUnicodeEncoding = dataTransferer.getDefaultUnicodeEncoding();
        }
        return new DataFlavor("text/plain;charset=" + defaultUnicodeEncoding + ";class=java.io.InputStream", "Plain Text");
    }

    public static final DataFlavor selectBestTextFlavor(DataFlavor[] dataFlavorArr) {
        if (dataFlavorArr == null || dataFlavorArr.length == 0) {
            return null;
        }
        if (textFlavorComparator == null) {
            textFlavorComparator = new TextFlavorComparator();
        }
        DataFlavor dataFlavor = (DataFlavor) Collections.max(Arrays.asList(dataFlavorArr), textFlavorComparator);
        if (!dataFlavor.isFlavorTextType()) {
            return null;
        }
        return dataFlavor;
    }

    /* loaded from: rt.jar:java/awt/datatransfer/DataFlavor$TextFlavorComparator.class */
    static class TextFlavorComparator extends DataTransferer.DataFlavorComparator {
        TextFlavorComparator() {
        }

        @Override // sun.awt.datatransfer.DataTransferer.DataFlavorComparator, java.util.Comparator
        public int compare(Object obj, Object obj2) {
            DataFlavor dataFlavor = (DataFlavor) obj2;
            if (((DataFlavor) obj).isFlavorTextType()) {
                if (dataFlavor.isFlavorTextType()) {
                    return super.compare(obj, obj2);
                }
                return 1;
            }
            if (dataFlavor.isFlavorTextType()) {
                return -1;
            }
            return 0;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v36, types: [java.io.InputStream] */
    public Reader getReaderForText(Transferable transferable) throws UnsupportedFlavorException, IOException {
        Object transferData = transferable.getTransferData(this);
        if (transferData == null) {
            throw new IllegalArgumentException("getTransferData() returned null");
        }
        if (transferData instanceof Reader) {
            return (Reader) transferData;
        }
        if (transferData instanceof String) {
            return new StringReader((String) transferData);
        }
        if (transferData instanceof CharBuffer) {
            CharBuffer charBuffer = (CharBuffer) transferData;
            int iRemaining = charBuffer.remaining();
            char[] cArr = new char[iRemaining];
            charBuffer.get(cArr, 0, iRemaining);
            return new CharArrayReader(cArr);
        }
        if (transferData instanceof char[]) {
            return new CharArrayReader((char[]) transferData);
        }
        ByteArrayInputStream byteArrayInputStream = null;
        if (transferData instanceof InputStream) {
            byteArrayInputStream = (InputStream) transferData;
        } else if (transferData instanceof ByteBuffer) {
            ByteBuffer byteBuffer = (ByteBuffer) transferData;
            int iRemaining2 = byteBuffer.remaining();
            byte[] bArr = new byte[iRemaining2];
            byteBuffer.get(bArr, 0, iRemaining2);
            byteArrayInputStream = new ByteArrayInputStream(bArr);
        } else if (transferData instanceof byte[]) {
            byteArrayInputStream = new ByteArrayInputStream((byte[]) transferData);
        }
        if (byteArrayInputStream == null) {
            throw new IllegalArgumentException("transfer data is not Reader, String, CharBuffer, char array, InputStream, ByteBuffer, or byte array");
        }
        String parameter = getParameter("charset");
        return parameter == null ? new InputStreamReader(byteArrayInputStream) : new InputStreamReader(byteArrayInputStream, parameter);
    }

    public String getMimeType() {
        if (this.mimeType != null) {
            return this.mimeType.toString();
        }
        return null;
    }

    public Class<?> getRepresentationClass() {
        return this.representationClass;
    }

    public String getHumanPresentableName() {
        return this.humanPresentableName;
    }

    public String getPrimaryType() {
        if (this.mimeType != null) {
            return this.mimeType.getPrimaryType();
        }
        return null;
    }

    public String getSubType() {
        if (this.mimeType != null) {
            return this.mimeType.getSubType();
        }
        return null;
    }

    public String getParameter(String str) {
        if (str.equals("humanPresentableName")) {
            return this.humanPresentableName;
        }
        if (this.mimeType != null) {
            return this.mimeType.getParameter(str);
        }
        return null;
    }

    public void setHumanPresentableName(String str) {
        this.humanPresentableName = str;
    }

    public boolean equals(Object obj) {
        return (obj instanceof DataFlavor) && equals((DataFlavor) obj);
    }

    public boolean equals(DataFlavor dataFlavor) {
        if (dataFlavor == null) {
            return false;
        }
        if (this == dataFlavor) {
            return true;
        }
        if (!Objects.equals(getRepresentationClass(), dataFlavor.getRepresentationClass())) {
            return false;
        }
        if (this.mimeType == null) {
            if (dataFlavor.mimeType != null) {
                return false;
            }
            return true;
        }
        if (!this.mimeType.match(dataFlavor.mimeType)) {
            return false;
        }
        if ("text".equals(getPrimaryType())) {
            if (DataTransferer.doesSubtypeSupportCharset(this) && this.representationClass != null && !isStandardTextRepresentationClass() && !Objects.equals(DataTransferer.canonicalName(getParameter("charset")), DataTransferer.canonicalName(dataFlavor.getParameter("charset")))) {
                return false;
            }
            if ("html".equals(getSubType()) && !Objects.equals(getParameter(com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.DOCUMENT_PNAME), dataFlavor.getParameter(com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.DOCUMENT_PNAME))) {
                return false;
            }
            return true;
        }
        return true;
    }

    @Deprecated
    public boolean equals(String str) {
        if (str == null || this.mimeType == null) {
            return false;
        }
        return isMimeTypeEqual(str);
    }

    public int hashCode() {
        String parameter;
        String strCanonicalName;
        int iHashCode = 0;
        if (this.representationClass != null) {
            iHashCode = 0 + this.representationClass.hashCode();
        }
        if (this.mimeType != null) {
            String primaryType = this.mimeType.getPrimaryType();
            if (primaryType != null) {
                iHashCode += primaryType.hashCode();
            }
            if ("text".equals(primaryType)) {
                if (DataTransferer.doesSubtypeSupportCharset(this) && this.representationClass != null && !isStandardTextRepresentationClass() && (strCanonicalName = DataTransferer.canonicalName(getParameter("charset"))) != null) {
                    iHashCode += strCanonicalName.hashCode();
                }
                if ("html".equals(getSubType()) && (parameter = getParameter(com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.DOCUMENT_PNAME)) != null) {
                    iHashCode += parameter.hashCode();
                }
            }
        }
        return iHashCode;
    }

    public boolean match(DataFlavor dataFlavor) {
        return equals(dataFlavor);
    }

    public boolean isMimeTypeEqual(String str) {
        if (str == null) {
            throw new NullPointerException("mimeType");
        }
        if (this.mimeType == null) {
            return false;
        }
        try {
            return this.mimeType.match(new MimeType(str));
        } catch (MimeTypeParseException e2) {
            return false;
        }
    }

    public final boolean isMimeTypeEqual(DataFlavor dataFlavor) {
        return isMimeTypeEqual(dataFlavor.mimeType);
    }

    private boolean isMimeTypeEqual(MimeType mimeType) {
        if (this.mimeType == null) {
            return mimeType == null;
        }
        return this.mimeType.match(mimeType);
    }

    private boolean isStandardTextRepresentationClass() {
        return isRepresentationClassReader() || String.class.equals(this.representationClass) || isRepresentationClassCharBuffer() || char[].class.equals(this.representationClass);
    }

    public boolean isMimeTypeSerializedObject() {
        return isMimeTypeEqual(javaSerializedObjectMimeType);
    }

    public final Class<?> getDefaultRepresentationClass() {
        return ioInputStreamClass;
    }

    public final String getDefaultRepresentationClassAsString() {
        return getDefaultRepresentationClass().getName();
    }

    public boolean isRepresentationClassInputStream() {
        return ioInputStreamClass.isAssignableFrom(this.representationClass);
    }

    public boolean isRepresentationClassReader() {
        return Reader.class.isAssignableFrom(this.representationClass);
    }

    public boolean isRepresentationClassCharBuffer() {
        return CharBuffer.class.isAssignableFrom(this.representationClass);
    }

    public boolean isRepresentationClassByteBuffer() {
        return ByteBuffer.class.isAssignableFrom(this.representationClass);
    }

    public boolean isRepresentationClassSerializable() {
        return Serializable.class.isAssignableFrom(this.representationClass);
    }

    public boolean isRepresentationClassRemote() {
        return DataTransferer.isRemote(this.representationClass);
    }

    public boolean isFlavorSerializedObjectType() {
        return isRepresentationClassSerializable() && isMimeTypeEqual(javaSerializedObjectMimeType);
    }

    public boolean isFlavorRemoteObjectType() {
        return isRepresentationClassRemote() && isRepresentationClassSerializable() && isMimeTypeEqual(javaRemoteObjectMimeType);
    }

    public boolean isFlavorJavaFileListType() {
        return this.mimeType != null && this.representationClass != null && List.class.isAssignableFrom(this.representationClass) && this.mimeType.match(javaFileListFlavor.mimeType);
    }

    public boolean isFlavorTextType() {
        return DataTransferer.isFlavorCharsetTextType(this) || DataTransferer.isFlavorNoncharsetTextType(this);
    }

    @Override // java.io.Externalizable
    public synchronized void writeExternal(ObjectOutput objectOutput) throws IOException {
        if (this.mimeType != null) {
            this.mimeType.setParameter("humanPresentableName", this.humanPresentableName);
            objectOutput.writeObject(this.mimeType);
            this.mimeType.removeParameter("humanPresentableName");
        } else {
            objectOutput.writeObject(null);
        }
        objectOutput.writeObject(this.representationClass);
    }

    @Override // java.io.Externalizable
    public synchronized void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        String parameter = null;
        this.mimeType = (MimeType) objectInput.readObject();
        if (this.mimeType != null) {
            this.humanPresentableName = this.mimeType.getParameter("humanPresentableName");
            this.mimeType.removeParameter("humanPresentableName");
            parameter = this.mimeType.getParameter(Constants.ATTRNAME_CLASS);
            if (parameter == null) {
                throw new IOException("no class parameter specified in: " + ((Object) this.mimeType));
            }
        }
        try {
            this.representationClass = (Class) objectInput.readObject();
        } catch (OptionalDataException e2) {
            if (!e2.eof || e2.length != 0) {
                throw e2;
            }
            if (parameter != null) {
                this.representationClass = tryToLoadClass(parameter, getClass().getClassLoader());
            }
        }
    }

    public Object clone() throws CloneNotSupportedException {
        Object objClone = super.clone();
        if (this.mimeType != null) {
            ((DataFlavor) objClone).mimeType = (MimeType) this.mimeType.clone();
        }
        return objClone;
    }

    @Deprecated
    protected String normalizeMimeTypeParameter(String str, String str2) {
        return str2;
    }

    @Deprecated
    protected String normalizeMimeType(String str) {
        return str;
    }
}
