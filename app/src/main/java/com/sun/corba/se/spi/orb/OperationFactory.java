package com.sun.corba.se.spi.orb;

import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.StringTokenizer;
import sun.corba.SharedSecrets;

/* loaded from: rt.jar:com/sun/corba/se/spi/orb/OperationFactory.class */
public abstract class OperationFactory {
    private static Operation suffixActionImpl = new SuffixAction();
    private static Operation valueActionImpl = new ValueAction();
    private static Operation identityActionImpl = new IdentityAction();
    private static Operation booleanActionImpl = new BooleanAction();
    private static Operation integerActionImpl = new IntegerAction();
    private static Operation stringActionImpl = new StringAction();
    private static Operation classActionImpl = new ClassAction();
    private static Operation setFlagActionImpl = new SetFlagAction();
    private static Operation URLActionImpl = new URLAction();
    private static Operation convertIntegerToShortImpl = new ConvertIntegerToShort();

    private OperationFactory() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getString(Object obj) {
        if (obj instanceof String) {
            return (String) obj;
        }
        throw new Error("String expected");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Object[] getObjectArray(Object obj) {
        if (obj instanceof Object[]) {
            return (Object[]) obj;
        }
        throw new Error("Object[] expected");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static StringPair getStringPair(Object obj) {
        if (obj instanceof StringPair) {
            return (StringPair) obj;
        }
        throw new Error("StringPair expected");
    }

    /* loaded from: rt.jar:com/sun/corba/se/spi/orb/OperationFactory$OperationBase.class */
    private static abstract class OperationBase implements Operation {
        private OperationBase() {
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof OperationBase)) {
                return false;
            }
            return toString().equals(((OperationBase) obj).toString());
        }

        public int hashCode() {
            return toString().hashCode();
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/spi/orb/OperationFactory$MaskErrorAction.class */
    private static class MaskErrorAction extends OperationBase {
        private Operation op;

        public MaskErrorAction(Operation operation) {
            super();
            this.op = operation;
        }

        @Override // com.sun.corba.se.spi.orb.Operation
        public Object operate(Object obj) {
            try {
                return this.op.operate(obj);
            } catch (Exception e2) {
                return null;
            }
        }

        public String toString() {
            return "maskErrorAction(" + ((Object) this.op) + ")";
        }
    }

    public static Operation maskErrorAction(Operation operation) {
        return new MaskErrorAction(operation);
    }

    /* loaded from: rt.jar:com/sun/corba/se/spi/orb/OperationFactory$IndexAction.class */
    private static class IndexAction extends OperationBase {
        private int index;

        public IndexAction(int i2) {
            super();
            this.index = i2;
        }

        @Override // com.sun.corba.se.spi.orb.Operation
        public Object operate(Object obj) {
            return OperationFactory.getObjectArray(obj)[this.index];
        }

        public String toString() {
            return "indexAction(" + this.index + ")";
        }
    }

    public static Operation indexAction(int i2) {
        return new IndexAction(i2);
    }

    /* loaded from: rt.jar:com/sun/corba/se/spi/orb/OperationFactory$SuffixAction.class */
    private static class SuffixAction extends OperationBase {
        private SuffixAction() {
            super();
        }

        @Override // com.sun.corba.se.spi.orb.Operation
        public Object operate(Object obj) {
            return OperationFactory.getStringPair(obj).getFirst();
        }

        public String toString() {
            return "suffixAction";
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/spi/orb/OperationFactory$ValueAction.class */
    private static class ValueAction extends OperationBase {
        private ValueAction() {
            super();
        }

        @Override // com.sun.corba.se.spi.orb.Operation
        public Object operate(Object obj) {
            return OperationFactory.getStringPair(obj).getSecond();
        }

        public String toString() {
            return "valueAction";
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/spi/orb/OperationFactory$IdentityAction.class */
    private static class IdentityAction extends OperationBase {
        private IdentityAction() {
            super();
        }

        @Override // com.sun.corba.se.spi.orb.Operation
        public Object operate(Object obj) {
            return obj;
        }

        public String toString() {
            return "identityAction";
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/spi/orb/OperationFactory$BooleanAction.class */
    private static class BooleanAction extends OperationBase {
        private BooleanAction() {
            super();
        }

        @Override // com.sun.corba.se.spi.orb.Operation
        public Object operate(Object obj) {
            return new Boolean(OperationFactory.getString(obj));
        }

        public String toString() {
            return "booleanAction";
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/spi/orb/OperationFactory$IntegerAction.class */
    private static class IntegerAction extends OperationBase {
        private IntegerAction() {
            super();
        }

        @Override // com.sun.corba.se.spi.orb.Operation
        public Object operate(Object obj) {
            return new Integer(OperationFactory.getString(obj));
        }

        public String toString() {
            return "integerAction";
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/spi/orb/OperationFactory$StringAction.class */
    private static class StringAction extends OperationBase {
        private StringAction() {
            super();
        }

        @Override // com.sun.corba.se.spi.orb.Operation
        public Object operate(Object obj) {
            return obj;
        }

        public String toString() {
            return "stringAction";
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/spi/orb/OperationFactory$ClassAction.class */
    private static class ClassAction extends OperationBase {
        private ClassAction() {
            super();
        }

        @Override // com.sun.corba.se.spi.orb.Operation
        public Object operate(Object obj) {
            String string = OperationFactory.getString(obj);
            try {
                return SharedSecrets.getJavaCorbaAccess().loadClass(string);
            } catch (Exception e2) {
                throw ORBUtilSystemException.get(CORBALogDomains.ORB_LIFECYCLE).couldNotLoadClass(e2, string);
            }
        }

        public String toString() {
            return "classAction";
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/spi/orb/OperationFactory$SetFlagAction.class */
    private static class SetFlagAction extends OperationBase {
        private SetFlagAction() {
            super();
        }

        @Override // com.sun.corba.se.spi.orb.Operation
        public Object operate(Object obj) {
            return Boolean.TRUE;
        }

        public String toString() {
            return "setFlagAction";
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/spi/orb/OperationFactory$URLAction.class */
    private static class URLAction extends OperationBase {
        private URLAction() {
            super();
        }

        @Override // com.sun.corba.se.spi.orb.Operation
        public Object operate(Object obj) {
            String str = (String) obj;
            try {
                return new URL(str);
            } catch (MalformedURLException e2) {
                throw ORBUtilSystemException.get(CORBALogDomains.ORB_LIFECYCLE).badUrl(e2, str);
            }
        }

        public String toString() {
            return "URLAction";
        }
    }

    public static Operation identityAction() {
        return identityActionImpl;
    }

    public static Operation suffixAction() {
        return suffixActionImpl;
    }

    public static Operation valueAction() {
        return valueActionImpl;
    }

    public static Operation booleanAction() {
        return booleanActionImpl;
    }

    public static Operation integerAction() {
        return integerActionImpl;
    }

    public static Operation stringAction() {
        return stringActionImpl;
    }

    public static Operation classAction() {
        return classActionImpl;
    }

    public static Operation setFlagAction() {
        return setFlagActionImpl;
    }

    public static Operation URLAction() {
        return URLActionImpl;
    }

    /* loaded from: rt.jar:com/sun/corba/se/spi/orb/OperationFactory$IntegerRangeAction.class */
    private static class IntegerRangeAction extends OperationBase {
        private int min;
        private int max;

        IntegerRangeAction(int i2, int i3) {
            super();
            this.min = i2;
            this.max = i3;
        }

        @Override // com.sun.corba.se.spi.orb.Operation
        public Object operate(Object obj) throws NumberFormatException {
            int i2 = Integer.parseInt(OperationFactory.getString(obj));
            if (i2 >= this.min && i2 <= this.max) {
                return new Integer(i2);
            }
            throw new IllegalArgumentException("Property value " + i2 + " is not in the range " + this.min + " to " + this.max);
        }

        public String toString() {
            return "integerRangeAction(" + this.min + "," + this.max + ")";
        }
    }

    public static Operation integerRangeAction(int i2, int i3) {
        return new IntegerRangeAction(i2, i3);
    }

    /* loaded from: rt.jar:com/sun/corba/se/spi/orb/OperationFactory$ListAction.class */
    private static class ListAction extends OperationBase {
        private String sep;
        private Operation act;

        ListAction(String str, Operation operation) {
            super();
            this.sep = str;
            this.act = operation;
        }

        @Override // com.sun.corba.se.spi.orb.Operation
        public Object operate(Object obj) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, NegativeArraySizeException {
            StringTokenizer stringTokenizer = new StringTokenizer(OperationFactory.getString(obj), this.sep);
            int iCountTokens = stringTokenizer.countTokens();
            Object objNewInstance = null;
            int i2 = 0;
            while (stringTokenizer.hasMoreTokens()) {
                Object objOperate = this.act.operate(stringTokenizer.nextToken());
                if (objNewInstance == null) {
                    objNewInstance = Array.newInstance(objOperate.getClass(), iCountTokens);
                }
                int i3 = i2;
                i2++;
                Array.set(objNewInstance, i3, objOperate);
            }
            return objNewInstance;
        }

        public String toString() {
            return "listAction(separator=\"" + this.sep + "\",action=" + ((Object) this.act) + ")";
        }
    }

    public static Operation listAction(String str, Operation operation) {
        return new ListAction(str, operation);
    }

    /* loaded from: rt.jar:com/sun/corba/se/spi/orb/OperationFactory$SequenceAction.class */
    private static class SequenceAction extends OperationBase {
        private String sep;
        private Operation[] actions;

        SequenceAction(String str, Operation[] operationArr) {
            super();
            this.sep = str;
            this.actions = operationArr;
        }

        @Override // com.sun.corba.se.spi.orb.Operation
        public Object operate(Object obj) {
            StringTokenizer stringTokenizer = new StringTokenizer(OperationFactory.getString(obj), this.sep);
            int iCountTokens = stringTokenizer.countTokens();
            if (iCountTokens != this.actions.length) {
                throw new Error("Number of tokens and number of actions do not match");
            }
            int i2 = 0;
            Object[] objArr = new Object[iCountTokens];
            while (stringTokenizer.hasMoreTokens()) {
                int i3 = i2;
                i2++;
                objArr[i3] = this.actions[i2].operate(stringTokenizer.nextToken());
            }
            return objArr;
        }

        public String toString() {
            return "sequenceAction(separator=\"" + this.sep + "\",actions=" + Arrays.toString(this.actions) + ")";
        }
    }

    public static Operation sequenceAction(String str, Operation[] operationArr) {
        return new SequenceAction(str, operationArr);
    }

    /* loaded from: rt.jar:com/sun/corba/se/spi/orb/OperationFactory$ComposeAction.class */
    private static class ComposeAction extends OperationBase {
        private Operation op1;
        private Operation op2;

        ComposeAction(Operation operation, Operation operation2) {
            super();
            this.op1 = operation;
            this.op2 = operation2;
        }

        @Override // com.sun.corba.se.spi.orb.Operation
        public Object operate(Object obj) {
            return this.op2.operate(this.op1.operate(obj));
        }

        public String toString() {
            return "composition(" + ((Object) this.op1) + "," + ((Object) this.op2) + ")";
        }
    }

    public static Operation compose(Operation operation, Operation operation2) {
        return new ComposeAction(operation, operation2);
    }

    /* loaded from: rt.jar:com/sun/corba/se/spi/orb/OperationFactory$MapAction.class */
    private static class MapAction extends OperationBase {
        Operation op;

        MapAction(Operation operation) {
            super();
            this.op = operation;
        }

        @Override // com.sun.corba.se.spi.orb.Operation
        public Object operate(Object obj) {
            Object[] objArr = (Object[]) obj;
            Object[] objArr2 = new Object[objArr.length];
            for (int i2 = 0; i2 < objArr.length; i2++) {
                objArr2[i2] = this.op.operate(objArr[i2]);
            }
            return objArr2;
        }

        public String toString() {
            return "mapAction(" + ((Object) this.op) + ")";
        }
    }

    public static Operation mapAction(Operation operation) {
        return new MapAction(operation);
    }

    /* loaded from: rt.jar:com/sun/corba/se/spi/orb/OperationFactory$MapSequenceAction.class */
    private static class MapSequenceAction extends OperationBase {
        private Operation[] op;

        public MapSequenceAction(Operation[] operationArr) {
            super();
            this.op = operationArr;
        }

        @Override // com.sun.corba.se.spi.orb.Operation
        public Object operate(Object obj) {
            Object[] objArr = (Object[]) obj;
            Object[] objArr2 = new Object[objArr.length];
            for (int i2 = 0; i2 < objArr.length; i2++) {
                objArr2[i2] = this.op[i2].operate(objArr[i2]);
            }
            return objArr2;
        }

        public String toString() {
            return "mapSequenceAction(" + Arrays.toString(this.op) + ")";
        }
    }

    public static Operation mapSequenceAction(Operation[] operationArr) {
        return new MapSequenceAction(operationArr);
    }

    /* loaded from: rt.jar:com/sun/corba/se/spi/orb/OperationFactory$ConvertIntegerToShort.class */
    private static class ConvertIntegerToShort extends OperationBase {
        private ConvertIntegerToShort() {
            super();
        }

        @Override // com.sun.corba.se.spi.orb.Operation
        public Object operate(Object obj) {
            return new Short(((Integer) obj).shortValue());
        }

        public String toString() {
            return "ConvertIntegerToShort";
        }
    }

    public static Operation convertIntegerToShort() {
        return convertIntegerToShortImpl;
    }
}
