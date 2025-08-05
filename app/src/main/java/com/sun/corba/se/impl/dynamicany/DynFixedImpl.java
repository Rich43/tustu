package com.sun.corba.se.impl.dynamicany;

import com.sun.corba.se.spi.orb.ORB;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.omg.CORBA.Any;
import org.omg.CORBA.BAD_INV_ORDER;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.TypeCodePackage.BadKind;
import org.omg.DynamicAny.DynAnyPackage.InvalidValue;
import org.omg.DynamicAny.DynAnyPackage.TypeMismatch;
import org.omg.DynamicAny.DynFixed;
import org.slf4j.Marker;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:com/sun/corba/se/impl/dynamicany/DynFixedImpl.class */
public class DynFixedImpl extends DynAnyBasicImpl implements DynFixed {
    private DynFixedImpl() {
        this(null, (Any) null, false);
    }

    protected DynFixedImpl(ORB orb, Any any, boolean z2) {
        super(orb, any, z2);
    }

    protected DynFixedImpl(ORB orb, TypeCode typeCode) {
        super(orb, typeCode);
        this.index = -1;
    }

    @Override // org.omg.DynamicAny.DynFixedOperations
    public String get_value() {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        return this.any.extract_fixed().toString();
    }

    @Override // org.omg.DynamicAny.DynFixedOperations
    public boolean set_value(String str) throws BAD_INV_ORDER, TypeMismatch, InvalidValue {
        String strSubstring;
        String strSubstring2;
        int length;
        BigDecimal bigDecimal;
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        short sFixed_digits = 0;
        boolean z2 = true;
        try {
            sFixed_digits = this.any.type().fixed_digits();
            this.any.type().fixed_scale();
        } catch (BadKind e2) {
        }
        String strTrim = str.trim();
        if (strTrim.length() == 0) {
            throw new TypeMismatch();
        }
        String str2 = "";
        if (strTrim.charAt(0) == '-') {
            str2 = LanguageTag.SEP;
            strTrim = strTrim.substring(1);
        } else if (strTrim.charAt(0) == '+') {
            str2 = Marker.ANY_NON_NULL_MARKER;
            strTrim = strTrim.substring(1);
        }
        int iIndexOf = strTrim.indexOf(100);
        if (iIndexOf == -1) {
            iIndexOf = strTrim.indexOf(68);
        }
        if (iIndexOf != -1) {
            strTrim = strTrim.substring(0, iIndexOf);
        }
        if (strTrim.length() == 0) {
            throw new TypeMismatch();
        }
        int iIndexOf2 = strTrim.indexOf(46);
        if (iIndexOf2 == -1) {
            strSubstring = strTrim;
            strSubstring2 = null;
            length = strSubstring.length();
        } else if (iIndexOf2 == 0) {
            strSubstring = null;
            strSubstring2 = strTrim;
            length = strSubstring2.length();
        } else {
            strSubstring = strTrim.substring(0, iIndexOf2);
            strSubstring2 = strTrim.substring(iIndexOf2 + 1);
            length = strSubstring.length() + strSubstring2.length();
        }
        if (length > sFixed_digits) {
            z2 = false;
            if (strSubstring.length() < sFixed_digits) {
                strSubstring2 = strSubstring2.substring(0, sFixed_digits - strSubstring.length());
            } else if (strSubstring.length() == sFixed_digits) {
                strSubstring2 = null;
            } else {
                throw new InvalidValue();
            }
        }
        try {
            new BigInteger(strSubstring);
            if (strSubstring2 == null) {
                bigDecimal = new BigDecimal(str2 + strSubstring);
            } else {
                new BigInteger(strSubstring2);
                bigDecimal = new BigDecimal(str2 + strSubstring + "." + strSubstring2);
            }
            this.any.insert_fixed(bigDecimal, this.any.type());
            return z2;
        } catch (NumberFormatException e3) {
            throw new TypeMismatch();
        }
    }

    public String toString() {
        short sFixed_digits = 0;
        short sFixed_scale = 0;
        try {
            sFixed_digits = this.any.type().fixed_digits();
            sFixed_scale = this.any.type().fixed_scale();
        } catch (BadKind e2) {
        }
        return "DynFixed with value=" + get_value() + ", digits=" + ((int) sFixed_digits) + ", scale=" + ((int) sFixed_scale);
    }
}
