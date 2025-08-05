package javax.security.auth.callback;

import java.io.Serializable;

/* loaded from: rt.jar:javax/security/auth/callback/ConfirmationCallback.class */
public class ConfirmationCallback implements Callback, Serializable {
    private static final long serialVersionUID = -9095656433782481624L;
    public static final int UNSPECIFIED_OPTION = -1;
    public static final int YES_NO_OPTION = 0;
    public static final int YES_NO_CANCEL_OPTION = 1;
    public static final int OK_CANCEL_OPTION = 2;
    public static final int YES = 0;
    public static final int NO = 1;
    public static final int CANCEL = 2;
    public static final int OK = 3;
    public static final int INFORMATION = 0;
    public static final int WARNING = 1;
    public static final int ERROR = 2;
    private String prompt;
    private int messageType;
    private int optionType;
    private int defaultOption;
    private String[] options;
    private int selection;

    public ConfirmationCallback(int i2, int i3, int i4) {
        this.optionType = -1;
        if (i2 < 0 || i2 > 2 || i3 < 0 || i3 > 2) {
            throw new IllegalArgumentException();
        }
        switch (i3) {
            case 0:
                if (i4 != 0 && i4 != 1) {
                    throw new IllegalArgumentException();
                }
                break;
            case 1:
                if (i4 != 0 && i4 != 1 && i4 != 2) {
                    throw new IllegalArgumentException();
                }
                break;
            case 2:
                if (i4 != 3 && i4 != 2) {
                    throw new IllegalArgumentException();
                }
                break;
        }
        this.messageType = i2;
        this.optionType = i3;
        this.defaultOption = i4;
    }

    public ConfirmationCallback(int i2, String[] strArr, int i3) {
        this.optionType = -1;
        if (i2 < 0 || i2 > 2 || strArr == null || strArr.length == 0 || i3 < 0 || i3 >= strArr.length) {
            throw new IllegalArgumentException();
        }
        for (int i4 = 0; i4 < strArr.length; i4++) {
            if (strArr[i4] == null || strArr[i4].length() == 0) {
                throw new IllegalArgumentException();
            }
        }
        this.messageType = i2;
        this.options = strArr;
        this.defaultOption = i3;
    }

    public ConfirmationCallback(String str, int i2, int i3, int i4) {
        this.optionType = -1;
        if (str == null || str.length() == 0 || i2 < 0 || i2 > 2 || i3 < 0 || i3 > 2) {
            throw new IllegalArgumentException();
        }
        switch (i3) {
            case 0:
                if (i4 != 0 && i4 != 1) {
                    throw new IllegalArgumentException();
                }
                break;
            case 1:
                if (i4 != 0 && i4 != 1 && i4 != 2) {
                    throw new IllegalArgumentException();
                }
                break;
            case 2:
                if (i4 != 3 && i4 != 2) {
                    throw new IllegalArgumentException();
                }
                break;
        }
        this.prompt = str;
        this.messageType = i2;
        this.optionType = i3;
        this.defaultOption = i4;
    }

    public ConfirmationCallback(String str, int i2, String[] strArr, int i3) {
        this.optionType = -1;
        if (str == null || str.length() == 0 || i2 < 0 || i2 > 2 || strArr == null || strArr.length == 0 || i3 < 0 || i3 >= strArr.length) {
            throw new IllegalArgumentException();
        }
        for (int i4 = 0; i4 < strArr.length; i4++) {
            if (strArr[i4] == null || strArr[i4].length() == 0) {
                throw new IllegalArgumentException();
            }
        }
        this.prompt = str;
        this.messageType = i2;
        this.options = strArr;
        this.defaultOption = i3;
    }

    public String getPrompt() {
        return this.prompt;
    }

    public int getMessageType() {
        return this.messageType;
    }

    public int getOptionType() {
        return this.optionType;
    }

    public String[] getOptions() {
        return this.options;
    }

    public int getDefaultOption() {
        return this.defaultOption;
    }

    public void setSelectedIndex(int i2) {
        this.selection = i2;
    }

    public int getSelectedIndex() {
        return this.selection;
    }
}
