package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/SystemException.class */
public abstract class SystemException extends RuntimeException {
    public int minor;
    public CompletionStatus completed;

    protected SystemException(String str, int i2, CompletionStatus completionStatus) {
        super(str);
        this.minor = i2;
        this.completed = completionStatus;
    }

    @Override // java.lang.Throwable
    public String toString() {
        String str;
        String str2;
        String string = super.toString();
        int i2 = this.minor & (-4096);
        switch (i2) {
            case OMGVMCID.value /* 1330446336 */:
                str = string + "  vmcid: OMG";
                break;
            case 1398079488:
                str = string + "  vmcid: SUN";
                break;
            default:
                str = string + "  vmcid: 0x" + Integer.toHexString(i2);
                break;
        }
        String str3 = str + "  minor code: " + (this.minor & 4095);
        switch (this.completed.value()) {
            case 0:
                str2 = str3 + "  completed: Yes";
                break;
            case 1:
                str2 = str3 + "  completed: No";
                break;
            case 2:
            default:
                str2 = str3 + " completed: Maybe";
                break;
        }
        return str2;
    }
}
