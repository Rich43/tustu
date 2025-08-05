package javax.security.auth.callback;

import java.io.Serializable;

/* loaded from: rt.jar:javax/security/auth/callback/ChoiceCallback.class */
public class ChoiceCallback implements Callback, Serializable {
    private static final long serialVersionUID = -3975664071579892167L;
    private String prompt;
    private String[] choices;
    private int defaultChoice;
    private boolean multipleSelectionsAllowed;
    private int[] selections;

    public ChoiceCallback(String str, String[] strArr, int i2, boolean z2) {
        if (str == null || str.length() == 0 || strArr == null || strArr.length == 0 || i2 < 0 || i2 >= strArr.length) {
            throw new IllegalArgumentException();
        }
        for (int i3 = 0; i3 < strArr.length; i3++) {
            if (strArr[i3] == null || strArr[i3].length() == 0) {
                throw new IllegalArgumentException();
            }
        }
        this.prompt = str;
        this.choices = strArr;
        this.defaultChoice = i2;
        this.multipleSelectionsAllowed = z2;
    }

    public String getPrompt() {
        return this.prompt;
    }

    public String[] getChoices() {
        return this.choices;
    }

    public int getDefaultChoice() {
        return this.defaultChoice;
    }

    public boolean allowMultipleSelections() {
        return this.multipleSelectionsAllowed;
    }

    public void setSelectedIndex(int i2) {
        this.selections = new int[1];
        this.selections[0] = i2;
    }

    public void setSelectedIndexes(int[] iArr) {
        if (!this.multipleSelectionsAllowed) {
            throw new UnsupportedOperationException();
        }
        this.selections = iArr;
    }

    public int[] getSelectedIndexes() {
        return this.selections;
    }
}
