package com.sun.jndi.toolkit.dir;

import java.util.Enumeration;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Properties;
import javax.naming.CompoundName;
import javax.naming.InvalidNameException;
import javax.naming.Name;

/* compiled from: HierMemDirCtx.java */
/* loaded from: rt.jar:com/sun/jndi/toolkit/dir/HierarchicalName.class */
final class HierarchicalName extends CompoundName {
    private int hashValue;
    private static final long serialVersionUID = -6717336834584573168L;

    HierarchicalName() {
        super(new Enumeration<String>() { // from class: com.sun.jndi.toolkit.dir.HierarchicalName.1
            @Override // java.util.Enumeration
            public boolean hasMoreElements() {
                return false;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Enumeration
            /* renamed from: nextElement */
            public String nextElement2() {
                throw new NoSuchElementException();
            }
        }, HierarchicalNameParser.mySyntax);
        this.hashValue = -1;
    }

    HierarchicalName(Enumeration<String> enumeration, Properties properties) {
        super(enumeration, properties);
        this.hashValue = -1;
    }

    HierarchicalName(String str, Properties properties) throws InvalidNameException {
        super(str, properties);
        this.hashValue = -1;
    }

    @Override // javax.naming.CompoundName
    public int hashCode() {
        if (this.hashValue == -1) {
            String upperCase = toString().toUpperCase(Locale.ENGLISH);
            int length = upperCase.length();
            int i2 = 0;
            char[] cArr = new char[length];
            upperCase.getChars(0, length, cArr, 0);
            for (int i3 = length; i3 > 0; i3--) {
                int i4 = i2;
                i2++;
                this.hashValue = (this.hashValue * 37) + cArr[i4];
            }
        }
        return this.hashValue;
    }

    @Override // javax.naming.CompoundName, javax.naming.Name
    public Name getPrefix(int i2) {
        return new HierarchicalName(super.getPrefix(i2).getAll(), this.mySyntax);
    }

    @Override // javax.naming.CompoundName, javax.naming.Name
    public Name getSuffix(int i2) {
        return new HierarchicalName(super.getSuffix(i2).getAll(), this.mySyntax);
    }

    @Override // javax.naming.CompoundName, javax.naming.Name
    public Object clone() {
        return new HierarchicalName(getAll(), this.mySyntax);
    }
}
