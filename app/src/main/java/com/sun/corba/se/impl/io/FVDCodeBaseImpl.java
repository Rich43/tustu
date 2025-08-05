package com.sun.corba.se.impl.io;

import com.sun.corba.se.impl.logging.OMGSystemException;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.org.omg.CORBA.Repository;
import com.sun.org.omg.CORBA.ValueDefPackage.FullValueDescription;
import com.sun.org.omg.SendingContext._CodeBaseImplBase;
import java.util.Hashtable;
import java.util.Stack;
import javax.rmi.CORBA.Util;
import javax.rmi.CORBA.ValueHandler;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.ORB;

/* loaded from: rt.jar:com/sun/corba/se/impl/io/FVDCodeBaseImpl.class */
public class FVDCodeBaseImpl extends _CodeBaseImplBase {
    private static Hashtable fvds = new Hashtable();
    private transient ORB orb = null;
    private transient OMGSystemException wrapper = OMGSystemException.get(CORBALogDomains.RPC_ENCODING);
    private transient ValueHandlerImpl vhandler = null;

    void setValueHandler(ValueHandler valueHandler) {
        this.vhandler = (ValueHandlerImpl) valueHandler;
    }

    @Override // com.sun.org.omg.SendingContext.CodeBaseOperations
    public Repository get_ir() {
        return null;
    }

    @Override // com.sun.org.omg.SendingContext.CodeBaseOperations
    public String implementation(String str) {
        try {
            if (this.vhandler == null) {
                this.vhandler = ValueHandlerImpl.getInstance(false);
            }
            String codebase = Util.getCodebase(this.vhandler.getClassFromType(str));
            if (codebase == null) {
                return "";
            }
            return codebase;
        } catch (ClassNotFoundException e2) {
            throw this.wrapper.missingLocalValueImpl(CompletionStatus.COMPLETED_MAYBE, e2);
        }
    }

    @Override // com.sun.org.omg.SendingContext.CodeBaseOperations
    public String[] implementations(String[] strArr) {
        String[] strArr2 = new String[strArr.length];
        for (int i2 = 0; i2 < strArr.length; i2++) {
            strArr2[i2] = implementation(strArr[i2]);
        }
        return strArr2;
    }

    @Override // com.sun.org.omg.SendingContext.CodeBaseOperations
    public FullValueDescription meta(String str) {
        try {
            FullValueDescription fullValueDescriptionTranslate = (FullValueDescription) fvds.get(str);
            if (fullValueDescriptionTranslate == null) {
                if (this.vhandler == null) {
                    this.vhandler = ValueHandlerImpl.getInstance(false);
                }
                try {
                    fullValueDescriptionTranslate = ValueUtility.translate(_orb(), ObjectStreamClass.lookup(this.vhandler.getAnyClassFromType(str)), this.vhandler);
                } catch (Throwable th) {
                    if (this.orb == null) {
                        this.orb = ORB.init();
                    }
                    fullValueDescriptionTranslate = ValueUtility.translate(this.orb, ObjectStreamClass.lookup(this.vhandler.getAnyClassFromType(str)), this.vhandler);
                }
                if (fullValueDescriptionTranslate != null) {
                    fvds.put(str, fullValueDescriptionTranslate);
                } else {
                    throw this.wrapper.missingLocalValueImpl(CompletionStatus.COMPLETED_MAYBE);
                }
            }
            return fullValueDescriptionTranslate;
        } catch (Throwable th2) {
            throw this.wrapper.incompatibleValueImpl(CompletionStatus.COMPLETED_MAYBE, th2);
        }
    }

    @Override // com.sun.org.omg.SendingContext.CodeBaseOperations
    public FullValueDescription[] metas(String[] strArr) {
        FullValueDescription[] fullValueDescriptionArr = new FullValueDescription[strArr.length];
        for (int i2 = 0; i2 < strArr.length; i2++) {
            fullValueDescriptionArr[i2] = meta(strArr[i2]);
        }
        return fullValueDescriptionArr;
    }

    @Override // com.sun.org.omg.SendingContext.CodeBaseOperations
    public String[] bases(String str) {
        try {
            if (this.vhandler == null) {
                this.vhandler = ValueHandlerImpl.getInstance(false);
            }
            Stack stack = new Stack();
            for (Class<? super Object> superclass = ObjectStreamClass.lookup(this.vhandler.getClassFromType(str)).forClass().getSuperclass(); !superclass.equals(Object.class); superclass = superclass.getSuperclass()) {
                stack.push(this.vhandler.createForAnyType(superclass));
            }
            String[] strArr = new String[stack.size()];
            for (int length = strArr.length - 1; length >= 0; length++) {
                strArr[length] = (String) stack.pop();
            }
            return strArr;
        } catch (Throwable th) {
            throw this.wrapper.missingLocalValueImpl(CompletionStatus.COMPLETED_MAYBE, th);
        }
    }
}
