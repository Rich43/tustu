package javax.management;

/* loaded from: rt.jar:javax/management/MatchQueryExp.class */
class MatchQueryExp extends QueryEval implements QueryExp {
    private static final long serialVersionUID = -7156603696948215014L;
    private AttributeValueExp exp;
    private String pattern;

    public MatchQueryExp() {
    }

    public MatchQueryExp(AttributeValueExp attributeValueExp, StringValueExp stringValueExp) {
        this.exp = attributeValueExp;
        this.pattern = stringValueExp.getValue();
    }

    public AttributeValueExp getAttribute() {
        return this.exp;
    }

    public String getPattern() {
        return this.pattern;
    }

    @Override // javax.management.QueryExp
    public boolean apply(ObjectName objectName) throws InvalidApplicationException, BadBinaryOpValueExpException, BadAttributeValueExpException, BadStringOperationException {
        ValueExp valueExpApply = this.exp.apply(objectName);
        if (!(valueExpApply instanceof StringValueExp)) {
            return false;
        }
        return wildmatch(((StringValueExp) valueExpApply).getValue(), this.pattern);
    }

    public String toString() {
        return ((Object) this.exp) + " like " + ((Object) new StringValueExp(this.pattern));
    }

    private static boolean wildmatch(String str, String str2) {
        int i2 = 0;
        int i3 = 0;
        int length = str.length();
        int length2 = str2.length();
        while (i3 < length2) {
            int i4 = i3;
            i3++;
            char cCharAt = str2.charAt(i4);
            if (cCharAt == '?') {
                i2++;
                if (i2 > length) {
                    return false;
                }
            } else if (cCharAt == '[') {
                if (i2 >= length) {
                    return false;
                }
                boolean z2 = true;
                boolean z3 = false;
                if (str2.charAt(i3) == '!') {
                    z2 = false;
                    i3++;
                }
                while (true) {
                    char cCharAt2 = str2.charAt(i3);
                    if (cCharAt2 == ']') {
                        break;
                    }
                    i3++;
                    if (i3 >= length2) {
                        break;
                    }
                    if (str2.charAt(i3) == '-' && i3 + 1 < length2 && str2.charAt(i3 + 1) != ']') {
                        if (str.charAt(i2) >= str2.charAt(i3 - 1) && str.charAt(i2) <= str2.charAt(i3 + 1)) {
                            z3 = true;
                        }
                        i3++;
                    } else if (cCharAt2 == str.charAt(i2)) {
                        z3 = true;
                    }
                }
                if (i3 >= length2 || z2 != z3) {
                    return false;
                }
                i3++;
                i2++;
            } else {
                if (cCharAt == '*') {
                    if (i3 >= length2) {
                        return true;
                    }
                    while (!wildmatch(str.substring(i2), str2.substring(i3))) {
                        i2++;
                        if (i2 >= length) {
                            return false;
                        }
                    }
                    return true;
                }
                if (cCharAt == '\\') {
                    if (i3 < length2 && i2 < length) {
                        i3++;
                        int i5 = i2;
                        i2++;
                        if (str2.charAt(i3) != str.charAt(i5)) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else {
                    if (i2 >= length) {
                        return false;
                    }
                    int i6 = i2;
                    i2++;
                    if (cCharAt != str.charAt(i6)) {
                        return false;
                    }
                }
            }
        }
        return i2 == length;
    }
}
