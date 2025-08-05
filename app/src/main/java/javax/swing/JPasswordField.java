package javax.swing;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleText;
import javax.accessibility.AccessibleTextSequence;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Segment;

/* loaded from: rt.jar:javax/swing/JPasswordField.class */
public class JPasswordField extends JTextField {
    private static final String uiClassID = "PasswordFieldUI";
    private char echoChar;
    private boolean echoCharSet;

    public JPasswordField() {
        this(null, null, 0);
    }

    public JPasswordField(String str) {
        this(null, str, 0);
    }

    public JPasswordField(int i2) {
        this(null, null, i2);
    }

    public JPasswordField(String str, int i2) {
        this(null, str, i2);
    }

    public JPasswordField(Document document, String str, int i2) {
        super(document, str, i2);
        this.echoCharSet = false;
        enableInputMethods(false);
    }

    @Override // javax.swing.JTextField, javax.swing.JComponent
    public String getUIClassID() {
        return uiClassID;
    }

    @Override // javax.swing.text.JTextComponent, javax.swing.JComponent
    public void updateUI() {
        if (!this.echoCharSet) {
            this.echoChar = '*';
        }
        super.updateUI();
    }

    public char getEchoChar() {
        return this.echoChar;
    }

    public void setEchoChar(char c2) {
        this.echoChar = c2;
        this.echoCharSet = true;
        repaint();
        revalidate();
    }

    public boolean echoCharIsSet() {
        return this.echoChar != 0;
    }

    @Override // javax.swing.text.JTextComponent
    public void cut() {
        if (getClientProperty("JPasswordField.cutCopyAllowed") != Boolean.TRUE) {
            UIManager.getLookAndFeel().provideErrorFeedback(this);
        } else {
            super.cut();
        }
    }

    @Override // javax.swing.text.JTextComponent
    public void copy() {
        if (getClientProperty("JPasswordField.cutCopyAllowed") != Boolean.TRUE) {
            UIManager.getLookAndFeel().provideErrorFeedback(this);
        } else {
            super.copy();
        }
    }

    @Override // javax.swing.text.JTextComponent
    @Deprecated
    public String getText() {
        return super.getText();
    }

    @Override // javax.swing.text.JTextComponent
    @Deprecated
    public String getText(int i2, int i3) throws BadLocationException {
        return super.getText(i2, i3);
    }

    public char[] getPassword() {
        Document document = getDocument();
        Segment segment = new Segment();
        try {
            document.getText(0, document.getLength(), segment);
            char[] cArr = new char[segment.count];
            System.arraycopy(segment.array, segment.offset, cArr, 0, segment.count);
            return cArr;
        } catch (BadLocationException e2) {
            return null;
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        if (getUIClassID().equals(uiClassID)) {
            byte writeObjCounter = (byte) (JComponent.getWriteObjCounter(this) - 1);
            JComponent.setWriteObjCounter(this, writeObjCounter);
            if (writeObjCounter == 0 && this.ui != null) {
                this.ui.installUI(this);
            }
        }
    }

    @Override // javax.swing.JTextField, javax.swing.text.JTextComponent, javax.swing.JComponent, java.awt.Container, java.awt.Component
    protected String paramString() {
        return super.paramString() + ",echoChar=" + this.echoChar;
    }

    boolean customSetUIProperty(String str, Object obj) {
        if (str == "echoChar") {
            if (!this.echoCharSet) {
                setEchoChar(((Character) obj).charValue());
                this.echoCharSet = false;
                return true;
            }
            return true;
        }
        return false;
    }

    @Override // javax.swing.JTextField, javax.swing.text.JTextComponent, java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleJPasswordField();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/JPasswordField$AccessibleJPasswordField.class */
    protected class AccessibleJPasswordField extends JTextField.AccessibleJTextField {
        protected AccessibleJPasswordField() {
            super();
        }

        @Override // javax.swing.text.JTextComponent.AccessibleJTextComponent, javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.PASSWORD_TEXT;
        }

        @Override // javax.swing.text.JTextComponent.AccessibleJTextComponent, javax.accessibility.AccessibleContext
        public AccessibleText getAccessibleText() {
            return this;
        }

        private String getEchoString(String str) {
            if (str == null) {
                return null;
            }
            char[] cArr = new char[str.length()];
            Arrays.fill(cArr, JPasswordField.this.getEchoChar());
            return new String(cArr);
        }

        @Override // javax.swing.text.JTextComponent.AccessibleJTextComponent, javax.accessibility.AccessibleText
        public String getAtIndex(int i2, int i3) {
            String str;
            if (i2 == 1) {
                str = super.getAtIndex(i2, i3);
            } else {
                char[] password = JPasswordField.this.getPassword();
                if (password == null || i3 < 0 || i3 >= password.length) {
                    return null;
                }
                str = new String(password);
            }
            return getEchoString(str);
        }

        @Override // javax.swing.text.JTextComponent.AccessibleJTextComponent, javax.accessibility.AccessibleText
        public String getAfterIndex(int i2, int i3) {
            if (i2 == 1) {
                return getEchoString(super.getAfterIndex(i2, i3));
            }
            return null;
        }

        @Override // javax.swing.text.JTextComponent.AccessibleJTextComponent, javax.accessibility.AccessibleText
        public String getBeforeIndex(int i2, int i3) {
            if (i2 == 1) {
                return getEchoString(super.getBeforeIndex(i2, i3));
            }
            return null;
        }

        @Override // javax.swing.text.JTextComponent.AccessibleJTextComponent, javax.accessibility.AccessibleEditableText, javax.accessibility.AccessibleExtendedText
        public String getTextRange(int i2, int i3) {
            return getEchoString(super.getTextRange(i2, i3));
        }

        @Override // javax.swing.text.JTextComponent.AccessibleJTextComponent, javax.accessibility.AccessibleExtendedText
        public AccessibleTextSequence getTextSequenceAt(int i2, int i3) {
            if (i2 == 1) {
                AccessibleTextSequence textSequenceAt = super.getTextSequenceAt(i2, i3);
                if (textSequenceAt == null) {
                    return null;
                }
                return new AccessibleTextSequence(textSequenceAt.startIndex, textSequenceAt.endIndex, getEchoString(textSequenceAt.text));
            }
            char[] password = JPasswordField.this.getPassword();
            if (password == null || i3 < 0 || i3 >= password.length) {
                return null;
            }
            return new AccessibleTextSequence(0, password.length - 1, getEchoString(new String(password)));
        }

        @Override // javax.swing.text.JTextComponent.AccessibleJTextComponent, javax.accessibility.AccessibleExtendedText
        public AccessibleTextSequence getTextSequenceAfter(int i2, int i3) {
            AccessibleTextSequence textSequenceAfter;
            if (i2 != 1 || (textSequenceAfter = super.getTextSequenceAfter(i2, i3)) == null) {
                return null;
            }
            return new AccessibleTextSequence(textSequenceAfter.startIndex, textSequenceAfter.endIndex, getEchoString(textSequenceAfter.text));
        }

        @Override // javax.swing.text.JTextComponent.AccessibleJTextComponent, javax.accessibility.AccessibleExtendedText
        public AccessibleTextSequence getTextSequenceBefore(int i2, int i3) {
            AccessibleTextSequence textSequenceBefore;
            if (i2 != 1 || (textSequenceBefore = super.getTextSequenceBefore(i2, i3)) == null) {
                return null;
            }
            return new AccessibleTextSequence(textSequenceBefore.startIndex, textSequenceBefore.endIndex, getEchoString(textSequenceBefore.text));
        }
    }
}
