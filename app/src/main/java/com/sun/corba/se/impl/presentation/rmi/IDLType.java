package com.sun.corba.se.impl.presentation.rmi;

/* loaded from: rt.jar:com/sun/corba/se/impl/presentation/rmi/IDLType.class */
public class IDLType {
    private Class cl_;
    private String[] modules_;
    private String memberName_;

    public IDLType(Class cls, String[] strArr, String str) {
        this.cl_ = cls;
        this.modules_ = strArr;
        this.memberName_ = str;
    }

    public IDLType(Class cls, String str) {
        this(cls, new String[0], str);
    }

    public Class getJavaClass() {
        return this.cl_;
    }

    public String[] getModules() {
        return this.modules_;
    }

    public String makeConcatenatedName(char c2, boolean z2) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 0; i2 < this.modules_.length; i2++) {
            String strMangleIDLKeywordClash = this.modules_[i2];
            if (i2 > 0) {
                stringBuffer.append(c2);
            }
            if (z2 && IDLNameTranslatorImpl.isIDLKeyword(strMangleIDLKeywordClash)) {
                strMangleIDLKeywordClash = IDLNameTranslatorImpl.mangleIDLKeywordClash(strMangleIDLKeywordClash);
            }
            stringBuffer.append(strMangleIDLKeywordClash);
        }
        return stringBuffer.toString();
    }

    public String getModuleName() {
        return makeConcatenatedName('_', false);
    }

    public String getExceptionName() {
        String strMakeConcatenatedName = makeConcatenatedName('/', true);
        String strSubstring = this.memberName_;
        if (strSubstring.endsWith("Exception")) {
            strSubstring = strSubstring.substring(0, strSubstring.length() - "Exception".length());
        }
        String str = strSubstring + "Ex";
        if (strMakeConcatenatedName.length() == 0) {
            return "IDL:" + str + ":1.0";
        }
        return "IDL:" + strMakeConcatenatedName + '/' + str + ":1.0";
    }

    public String getMemberName() {
        return this.memberName_;
    }

    public boolean hasModule() {
        return this.modules_.length > 0;
    }
}
