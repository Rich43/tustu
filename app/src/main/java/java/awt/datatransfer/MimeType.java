package java.awt.datatransfer;

import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Locale;

/* loaded from: rt.jar:java/awt/datatransfer/MimeType.class */
class MimeType implements Externalizable, Cloneable {
    static final long serialVersionUID = -6568722458793895906L;
    private String primaryType;
    private String subType;
    private MimeTypeParameterList parameters;
    private static final String TSPECIALS = "()<>@,;:\\\"/[]?=";

    public MimeType() {
    }

    public MimeType(String str) throws MimeTypeParseException {
        parse(str);
    }

    public MimeType(String str, String str2) throws MimeTypeParseException {
        this(str, str2, new MimeTypeParameterList());
    }

    public MimeType(String str, String str2, MimeTypeParameterList mimeTypeParameterList) throws MimeTypeParseException {
        if (isValidToken(str)) {
            this.primaryType = str.toLowerCase(Locale.ENGLISH);
            if (isValidToken(str2)) {
                this.subType = str2.toLowerCase(Locale.ENGLISH);
                this.parameters = (MimeTypeParameterList) mimeTypeParameterList.clone();
                return;
            }
            throw new MimeTypeParseException("Sub type is invalid.");
        }
        throw new MimeTypeParseException("Primary type is invalid.");
    }

    public int hashCode() {
        return 0 + this.primaryType.hashCode() + this.subType.hashCode() + this.parameters.hashCode();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof MimeType)) {
            return false;
        }
        MimeType mimeType = (MimeType) obj;
        return this.primaryType.equals(mimeType.primaryType) && this.subType.equals(mimeType.subType) && this.parameters.equals(mimeType.parameters);
    }

    private void parse(String str) throws MimeTypeParseException {
        int iIndexOf = str.indexOf(47);
        int iIndexOf2 = str.indexOf(59);
        if (iIndexOf < 0 && iIndexOf2 < 0) {
            throw new MimeTypeParseException("Unable to find a sub type.");
        }
        if (iIndexOf < 0 && iIndexOf2 >= 0) {
            throw new MimeTypeParseException("Unable to find a sub type.");
        }
        if (iIndexOf >= 0 && iIndexOf2 < 0) {
            this.primaryType = str.substring(0, iIndexOf).trim().toLowerCase(Locale.ENGLISH);
            this.subType = str.substring(iIndexOf + 1).trim().toLowerCase(Locale.ENGLISH);
            this.parameters = new MimeTypeParameterList();
        } else if (iIndexOf < iIndexOf2) {
            this.primaryType = str.substring(0, iIndexOf).trim().toLowerCase(Locale.ENGLISH);
            this.subType = str.substring(iIndexOf + 1, iIndexOf2).trim().toLowerCase(Locale.ENGLISH);
            this.parameters = new MimeTypeParameterList(str.substring(iIndexOf2));
        } else {
            throw new MimeTypeParseException("Unable to find a sub type.");
        }
        if (!isValidToken(this.primaryType)) {
            throw new MimeTypeParseException("Primary type is invalid.");
        }
        if (!isValidToken(this.subType)) {
            throw new MimeTypeParseException("Sub type is invalid.");
        }
    }

    public String getPrimaryType() {
        return this.primaryType;
    }

    public String getSubType() {
        return this.subType;
    }

    public MimeTypeParameterList getParameters() {
        return (MimeTypeParameterList) this.parameters.clone();
    }

    public String getParameter(String str) {
        return this.parameters.get(str);
    }

    public void setParameter(String str, String str2) {
        this.parameters.set(str, str2);
    }

    public void removeParameter(String str) {
        this.parameters.remove(str);
    }

    public String toString() {
        return getBaseType() + this.parameters.toString();
    }

    public String getBaseType() {
        return this.primaryType + "/" + this.subType;
    }

    public boolean match(MimeType mimeType) {
        return mimeType != null && this.primaryType.equals(mimeType.getPrimaryType()) && (this.subType.equals("*") || mimeType.getSubType().equals("*") || this.subType.equals(mimeType.getSubType()));
    }

    public boolean match(String str) throws MimeTypeParseException {
        if (str == null) {
            return false;
        }
        return match(new MimeType(str));
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        String string = toString();
        if (string.length() <= 65535) {
            objectOutput.writeUTF(string);
            return;
        }
        objectOutput.writeByte(0);
        objectOutput.writeByte(0);
        objectOutput.writeInt(string.length());
        objectOutput.write(string.getBytes());
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        String utf = objectInput.readUTF();
        if (utf == null || utf.length() == 0) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int i2 = objectInput.readInt();
            while (true) {
                int i3 = i2;
                i2--;
                if (i3 <= 0) {
                    break;
                } else {
                    byteArrayOutputStream.write(objectInput.readByte());
                }
            }
            utf = byteArrayOutputStream.toString();
        }
        try {
            parse(utf);
        } catch (MimeTypeParseException e2) {
            throw new IOException(e2.toString());
        }
    }

    public Object clone() {
        MimeType mimeType = null;
        try {
            mimeType = (MimeType) super.clone();
        } catch (CloneNotSupportedException e2) {
        }
        mimeType.parameters = (MimeTypeParameterList) this.parameters.clone();
        return mimeType;
    }

    private static boolean isTokenChar(char c2) {
        return c2 > ' ' && c2 < 127 && TSPECIALS.indexOf(c2) < 0;
    }

    private boolean isValidToken(String str) {
        int length = str.length();
        if (length > 0) {
            for (int i2 = 0; i2 < length; i2++) {
                if (!isTokenChar(str.charAt(i2))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
