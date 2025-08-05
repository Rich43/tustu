package java.lang.invoke;

import java.lang.invoke.LambdaForm;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import javafx.fxml.FXMLLoader;

/* loaded from: rt.jar:java/lang/invoke/LambdaFormBuffer.class */
final class LambdaFormBuffer {
    private int arity;
    private int length;
    private LambdaForm.Name[] names;
    private LambdaForm.Name[] originalNames;
    private byte flags;
    private int firstChange;
    private LambdaForm.Name resultName;
    private String debugName;
    private ArrayList<LambdaForm.Name> dups;
    private static final int F_TRANS = 16;
    private static final int F_OWNED = 3;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !LambdaFormBuffer.class.desiredAssertionStatus();
    }

    LambdaFormBuffer(LambdaForm lambdaForm) {
        this.arity = lambdaForm.arity;
        setNames(lambdaForm.names);
        int i2 = lambdaForm.result;
        i2 = i2 == -2 ? this.length - 1 : i2;
        if (i2 >= 0 && lambdaForm.names[i2].type != LambdaForm.BasicType.V_TYPE) {
            this.resultName = lambdaForm.names[i2];
        }
        this.debugName = lambdaForm.debugName;
        if (!$assertionsDisabled && !lambdaForm.nameRefsAreLegal()) {
            throw new AssertionError();
        }
    }

    private LambdaForm lambdaForm() {
        if ($assertionsDisabled || !inTrans()) {
            return new LambdaForm(this.debugName, this.arity, nameArray(), resultIndex());
        }
        throw new AssertionError();
    }

    LambdaForm.Name name(int i2) {
        if ($assertionsDisabled || i2 < this.length) {
            return this.names[i2];
        }
        throw new AssertionError();
    }

    LambdaForm.Name[] nameArray() {
        return (LambdaForm.Name[]) Arrays.copyOf(this.names, this.length);
    }

    int resultIndex() {
        if (this.resultName == null) {
            return -1;
        }
        int iIndexOf = indexOf(this.resultName, this.names);
        if ($assertionsDisabled || iIndexOf >= 0) {
            return iIndexOf;
        }
        throw new AssertionError();
    }

    void setNames(LambdaForm.Name[] nameArr) {
        this.originalNames = nameArr;
        this.names = nameArr;
        this.length = nameArr.length;
        this.flags = (byte) 0;
    }

    private boolean verifyArity() {
        for (int i2 = 0; i2 < this.arity && i2 < this.firstChange; i2++) {
            if (!$assertionsDisabled && !this.names[i2].isParam()) {
                throw new AssertionError((Object) (FXMLLoader.CONTROLLER_METHOD_PREFIX + i2 + "=" + ((Object) this.names[i2])));
            }
        }
        for (int i3 = this.arity; i3 < this.length; i3++) {
            if (!$assertionsDisabled && this.names[i3].isParam()) {
                throw new AssertionError((Object) (FXMLLoader.CONTROLLER_METHOD_PREFIX + i3 + "=" + ((Object) this.names[i3])));
            }
        }
        for (int i4 = this.length; i4 < this.names.length; i4++) {
            if (!$assertionsDisabled && this.names[i4] != null) {
                throw new AssertionError((Object) (FXMLLoader.CONTROLLER_METHOD_PREFIX + i4 + "=" + ((Object) this.names[i4])));
            }
        }
        if (this.resultName != null) {
            int iIndexOf = indexOf(this.resultName, this.names);
            if (!$assertionsDisabled && iIndexOf < 0) {
                throw new AssertionError((Object) ("not found: " + this.resultName.exprString() + ((Object) Arrays.asList(this.names))));
            }
            if ($assertionsDisabled || this.names[iIndexOf] == this.resultName) {
                return true;
            }
            throw new AssertionError();
        }
        return true;
    }

    private boolean verifyFirstChange() {
        if (!$assertionsDisabled && !inTrans()) {
            throw new AssertionError();
        }
        for (int i2 = 0; i2 < this.length; i2++) {
            if (this.names[i2] != this.originalNames[i2]) {
                if ($assertionsDisabled || this.firstChange == i2) {
                    return true;
                }
                throw new AssertionError(Arrays.asList(Integer.valueOf(this.firstChange), Integer.valueOf(i2), this.originalNames[i2].exprString(), Arrays.asList(this.names)));
            }
        }
        if ($assertionsDisabled || this.firstChange == this.length) {
            return true;
        }
        throw new AssertionError(Arrays.asList(Integer.valueOf(this.firstChange), Arrays.asList(this.names)));
    }

    private static int indexOf(LambdaForm.NamedFunction namedFunction, LambdaForm.NamedFunction[] namedFunctionArr) {
        for (int i2 = 0; i2 < namedFunctionArr.length; i2++) {
            if (namedFunctionArr[i2] == namedFunction) {
                return i2;
            }
        }
        return -1;
    }

    private static int indexOf(LambdaForm.Name name, LambdaForm.Name[] nameArr) {
        for (int i2 = 0; i2 < nameArr.length; i2++) {
            if (nameArr[i2] == name) {
                return i2;
            }
        }
        return -1;
    }

    boolean inTrans() {
        return (this.flags & 16) != 0;
    }

    int ownedCount() {
        return this.flags & 3;
    }

    void growNames(int i2, int i3) {
        int i4 = this.length;
        int i5 = i4 + i3;
        int iOwnedCount = ownedCount();
        if (iOwnedCount == 0 || i5 > this.names.length) {
            this.names = (LambdaForm.Name[]) Arrays.copyOf(this.names, ((this.names.length + i3) * 5) / 4);
            if (iOwnedCount == 0) {
                this.flags = (byte) (this.flags + 1);
                iOwnedCount++;
                if (!$assertionsDisabled && ownedCount() != iOwnedCount) {
                    throw new AssertionError();
                }
            }
        }
        if (this.originalNames != null && this.originalNames.length < this.names.length) {
            this.originalNames = (LambdaForm.Name[]) Arrays.copyOf(this.originalNames, this.names.length);
            if (iOwnedCount == 1) {
                this.flags = (byte) (this.flags + 1);
                int i6 = iOwnedCount + 1;
                if (!$assertionsDisabled && ownedCount() != i6) {
                    throw new AssertionError();
                }
            }
        }
        if (i3 == 0) {
            return;
        }
        int i7 = i2 + i3;
        int i8 = i4 - i2;
        System.arraycopy(this.names, i2, this.names, i7, i8);
        Arrays.fill(this.names, i2, i7, (Object) null);
        if (this.originalNames != null) {
            System.arraycopy(this.originalNames, i2, this.originalNames, i7, i8);
            Arrays.fill(this.originalNames, i2, i7, (Object) null);
        }
        this.length = i5;
        if (this.firstChange >= i2) {
            this.firstChange += i3;
        }
    }

    int lastIndexOf(LambdaForm.Name name) {
        int i2 = -1;
        for (int i3 = 0; i3 < this.length; i3++) {
            if (this.names[i3] == name) {
                i2 = i3;
            }
        }
        return i2;
    }

    private void noteDuplicate(int i2, int i3) {
        LambdaForm.Name name = this.names[i2];
        if (!$assertionsDisabled && name != this.names[i3]) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && this.originalNames[i2] == null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && this.originalNames[i3] != null && this.originalNames[i3] != name) {
            throw new AssertionError();
        }
        if (this.dups == null) {
            this.dups = new ArrayList<>();
        }
        this.dups.add(name);
    }

    private void clearDuplicatesAndNulls() {
        if (this.dups != null) {
            if (!$assertionsDisabled && ownedCount() < 1) {
                throw new AssertionError();
            }
            Iterator<LambdaForm.Name> it = this.dups.iterator();
            while (it.hasNext()) {
                LambdaForm.Name next = it.next();
                int i2 = this.firstChange;
                while (true) {
                    if (i2 >= this.length) {
                        break;
                    }
                    if (this.names[i2] != next || this.originalNames[i2] == next) {
                        i2++;
                    } else {
                        this.names[i2] = null;
                        if (!$assertionsDisabled && !Arrays.asList(this.names).contains(next)) {
                            throw new AssertionError();
                        }
                    }
                }
            }
            this.dups.clear();
        }
        int i3 = this.length;
        int i4 = this.firstChange;
        while (i4 < this.length) {
            if (this.names[i4] == null) {
                int i5 = this.length - 1;
                this.length = i5;
                System.arraycopy(this.names, i4 + 1, this.names, i4, i5 - i4);
                i4--;
            }
            i4++;
        }
        if (this.length < i3) {
            Arrays.fill(this.names, this.length, i3, (Object) null);
        }
        if (!$assertionsDisabled && Arrays.asList(this.names).subList(0, this.length).contains(null)) {
            throw new AssertionError();
        }
    }

    void startEdit() {
        if (!$assertionsDisabled && !verifyArity()) {
            throw new AssertionError();
        }
        int iOwnedCount = ownedCount();
        if (!$assertionsDisabled && inTrans()) {
            throw new AssertionError();
        }
        this.flags = (byte) (this.flags | 16);
        LambdaForm.Name[] nameArr = this.names;
        LambdaForm.Name[] nameArr2 = iOwnedCount == 2 ? this.originalNames : null;
        if (!$assertionsDisabled && nameArr2 == nameArr) {
            throw new AssertionError();
        }
        if (nameArr2 != null && nameArr2.length >= this.length) {
            this.names = copyNamesInto(nameArr2);
        } else {
            this.names = (LambdaForm.Name[]) Arrays.copyOf(nameArr, Math.max(this.length + 2, nameArr.length));
            if (iOwnedCount < 2) {
                this.flags = (byte) (this.flags + 1);
            }
            if (!$assertionsDisabled && ownedCount() != iOwnedCount + 1) {
                throw new AssertionError();
            }
        }
        this.originalNames = nameArr;
        if (!$assertionsDisabled && this.originalNames == this.names) {
            throw new AssertionError();
        }
        this.firstChange = this.length;
        if (!$assertionsDisabled && !inTrans()) {
            throw new AssertionError();
        }
    }

    private void changeName(int i2, LambdaForm.Name name) {
        if (!$assertionsDisabled && !inTrans()) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && i2 >= this.length) {
            throw new AssertionError();
        }
        LambdaForm.Name name2 = this.names[i2];
        if (!$assertionsDisabled && name2 != this.originalNames[i2]) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && !verifyFirstChange()) {
            throw new AssertionError();
        }
        if (ownedCount() == 0) {
            growNames(0, 0);
        }
        this.names[i2] = name;
        if (this.firstChange > i2) {
            this.firstChange = i2;
        }
        if (this.resultName != null && this.resultName == name2) {
            this.resultName = name;
        }
    }

    void setResult(LambdaForm.Name name) {
        if (!$assertionsDisabled && name != null && lastIndexOf(name) < 0) {
            throw new AssertionError();
        }
        this.resultName = name;
    }

    LambdaForm endEdit() {
        LambdaForm.Name nameReplaceNames;
        if (!$assertionsDisabled && !verifyFirstChange()) {
            throw new AssertionError();
        }
        for (int iMax = Math.max(this.firstChange, this.arity); iMax < this.length; iMax++) {
            LambdaForm.Name name = this.names[iMax];
            if (name != null && (nameReplaceNames = name.replaceNames(this.originalNames, this.names, this.firstChange, iMax)) != name) {
                this.names[iMax] = nameReplaceNames;
                if (this.resultName == name) {
                    this.resultName = nameReplaceNames;
                }
            }
        }
        if (!$assertionsDisabled && !inTrans()) {
            throw new AssertionError();
        }
        this.flags = (byte) (this.flags & (-17));
        clearDuplicatesAndNulls();
        this.originalNames = null;
        if (this.firstChange < this.arity) {
            LambdaForm.Name[] nameArr = new LambdaForm.Name[this.arity - this.firstChange];
            int i2 = this.firstChange;
            int i3 = 0;
            for (int i4 = this.firstChange; i4 < this.arity; i4++) {
                LambdaForm.Name name2 = this.names[i4];
                if (name2.isParam()) {
                    int i5 = i2;
                    i2++;
                    this.names[i5] = name2;
                } else {
                    int i6 = i3;
                    i3++;
                    nameArr[i6] = name2;
                }
            }
            if (!$assertionsDisabled && i3 != this.arity - i2) {
                throw new AssertionError();
            }
            System.arraycopy(nameArr, 0, this.names, i2, i3);
            this.arity -= i3;
        }
        if ($assertionsDisabled || verifyArity()) {
            return lambdaForm();
        }
        throw new AssertionError();
    }

    private LambdaForm.Name[] copyNamesInto(LambdaForm.Name[] nameArr) {
        System.arraycopy(this.names, 0, nameArr, 0, this.length);
        Arrays.fill(nameArr, this.length, nameArr.length, (Object) null);
        return nameArr;
    }

    LambdaFormBuffer replaceFunctions(LambdaForm.NamedFunction[] namedFunctionArr, LambdaForm.NamedFunction[] namedFunctionArr2, Object... objArr) {
        if (!$assertionsDisabled && !inTrans()) {
            throw new AssertionError();
        }
        if (namedFunctionArr.length == 0) {
            return this;
        }
        for (int i2 = this.arity; i2 < this.length; i2++) {
            LambdaForm.Name name = this.names[i2];
            int iIndexOf = indexOf(name.function, namedFunctionArr);
            if (iIndexOf >= 0 && Arrays.equals(name.arguments, objArr)) {
                changeName(i2, new LambdaForm.Name(namedFunctionArr2[iIndexOf], name.arguments));
            }
        }
        return this;
    }

    private void replaceName(int i2, LambdaForm.Name name) {
        if (!$assertionsDisabled && !inTrans()) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && !verifyArity()) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && i2 >= this.arity) {
            throw new AssertionError();
        }
        LambdaForm.Name name2 = this.names[i2];
        if (!$assertionsDisabled && !name2.isParam()) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && name2.type != name.type) {
            throw new AssertionError();
        }
        changeName(i2, name);
    }

    LambdaFormBuffer renameParameter(int i2, LambdaForm.Name name) {
        if (!$assertionsDisabled && !name.isParam()) {
            throw new AssertionError();
        }
        replaceName(i2, name);
        return this;
    }

    LambdaFormBuffer replaceParameterByNewExpression(int i2, LambdaForm.Name name) {
        if (!$assertionsDisabled && name.isParam()) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && lastIndexOf(name) >= 0) {
            throw new AssertionError();
        }
        replaceName(i2, name);
        return this;
    }

    LambdaFormBuffer replaceParameterByCopy(int i2, int i3) {
        if (!$assertionsDisabled && i2 == i3) {
            throw new AssertionError();
        }
        replaceName(i2, this.names[i3]);
        noteDuplicate(i2, i3);
        return this;
    }

    private void insertName(int i2, LambdaForm.Name name, boolean z2) {
        if (!$assertionsDisabled && !inTrans()) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && !verifyArity()) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && (!z2 ? i2 >= this.arity : i2 <= this.arity)) {
            throw new AssertionError();
        }
        growNames(i2, 1);
        if (z2) {
            this.arity++;
        }
        changeName(i2, name);
    }

    LambdaFormBuffer insertExpression(int i2, LambdaForm.Name name) {
        if (!$assertionsDisabled && name.isParam()) {
            throw new AssertionError();
        }
        insertName(i2, name, false);
        return this;
    }

    LambdaFormBuffer insertParameter(int i2, LambdaForm.Name name) {
        if (!$assertionsDisabled && !name.isParam()) {
            throw new AssertionError();
        }
        insertName(i2, name, true);
        return this;
    }
}
