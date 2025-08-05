package java.awt;

import java.awt.Component;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.awt.im.InputMethodRequests;
import java.awt.peer.TextComponentPeer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.BreakIterator;
import java.util.EventListener;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleState;
import javax.accessibility.AccessibleStateSet;
import javax.accessibility.AccessibleText;
import javax.swing.text.AttributeSet;
import sun.awt.InputMethodSupport;
import sun.security.util.SecurityConstants;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:java/awt/TextComponent.class */
public class TextComponent extends Component implements Accessible {
    String text;
    int selectionStart;
    int selectionEnd;
    protected transient TextListener textListener;
    private static final long serialVersionUID = -2214773872412987419L;
    boolean editable = true;
    boolean backgroundSetByClientCode = false;
    private int textComponentSerializedDataVersion = 1;
    private boolean checkForEnableIM = true;

    TextComponent(String str) throws HeadlessException {
        GraphicsEnvironment.checkHeadless();
        this.text = str != null ? str : "";
        setCursor(Cursor.getPredefinedCursor(2));
    }

    private void enableInputMethodsIfNecessary() {
        if (this.checkForEnableIM) {
            this.checkForEnableIM = false;
            try {
                Object defaultToolkit = Toolkit.getDefaultToolkit();
                boolean zEnableInputMethodsForTextComponent = false;
                if (defaultToolkit instanceof InputMethodSupport) {
                    zEnableInputMethodsForTextComponent = ((InputMethodSupport) defaultToolkit).enableInputMethodsForTextComponent();
                }
                enableInputMethods(zEnableInputMethodsForTextComponent);
            } catch (Exception e2) {
            }
        }
    }

    @Override // java.awt.Component
    public void enableInputMethods(boolean z2) {
        this.checkForEnableIM = false;
        super.enableInputMethods(z2);
    }

    @Override // java.awt.Component
    boolean areInputMethodsEnabled() {
        if (this.checkForEnableIM) {
            enableInputMethodsIfNecessary();
        }
        return (this.eventMask & 4096) != 0;
    }

    @Override // java.awt.Component
    public InputMethodRequests getInputMethodRequests() {
        TextComponentPeer textComponentPeer = (TextComponentPeer) this.peer;
        if (textComponentPeer != null) {
            return textComponentPeer.getInputMethodRequests();
        }
        return null;
    }

    @Override // java.awt.Component
    public void addNotify() {
        super.addNotify();
        enableInputMethodsIfNecessary();
    }

    @Override // java.awt.Component
    public void removeNotify() {
        synchronized (getTreeLock()) {
            TextComponentPeer textComponentPeer = (TextComponentPeer) this.peer;
            if (textComponentPeer != null) {
                this.text = textComponentPeer.getText();
                this.selectionStart = textComponentPeer.getSelectionStart();
                this.selectionEnd = textComponentPeer.getSelectionEnd();
            }
            super.removeNotify();
        }
    }

    public synchronized void setText(String str) {
        boolean z2 = (this.text == null || this.text.isEmpty()) && (str == null || str.isEmpty());
        this.text = str != null ? str : "";
        TextComponentPeer textComponentPeer = (TextComponentPeer) this.peer;
        if (textComponentPeer != null && !z2) {
            textComponentPeer.setText(this.text);
        }
    }

    public synchronized String getText() {
        TextComponentPeer textComponentPeer = (TextComponentPeer) this.peer;
        if (textComponentPeer != null) {
            this.text = textComponentPeer.getText();
        }
        return this.text;
    }

    public synchronized String getSelectedText() {
        return getText().substring(getSelectionStart(), getSelectionEnd());
    }

    public boolean isEditable() {
        return this.editable;
    }

    public synchronized void setEditable(boolean z2) {
        if (this.editable == z2) {
            return;
        }
        this.editable = z2;
        TextComponentPeer textComponentPeer = (TextComponentPeer) this.peer;
        if (textComponentPeer != null) {
            textComponentPeer.setEditable(z2);
        }
    }

    @Override // java.awt.Component
    public Color getBackground() {
        if (!this.editable && !this.backgroundSetByClientCode) {
            return SystemColor.control;
        }
        return super.getBackground();
    }

    @Override // java.awt.Component
    public void setBackground(Color color) {
        this.backgroundSetByClientCode = true;
        super.setBackground(color);
    }

    public synchronized int getSelectionStart() {
        TextComponentPeer textComponentPeer = (TextComponentPeer) this.peer;
        if (textComponentPeer != null) {
            this.selectionStart = textComponentPeer.getSelectionStart();
        }
        return this.selectionStart;
    }

    public synchronized void setSelectionStart(int i2) {
        select(i2, getSelectionEnd());
    }

    public synchronized int getSelectionEnd() {
        TextComponentPeer textComponentPeer = (TextComponentPeer) this.peer;
        if (textComponentPeer != null) {
            this.selectionEnd = textComponentPeer.getSelectionEnd();
        }
        return this.selectionEnd;
    }

    public synchronized void setSelectionEnd(int i2) {
        select(getSelectionStart(), i2);
    }

    public synchronized void select(int i2, int i3) {
        String text = getText();
        if (i2 < 0) {
            i2 = 0;
        }
        if (i2 > text.length()) {
            i2 = text.length();
        }
        if (i3 > text.length()) {
            i3 = text.length();
        }
        if (i3 < i2) {
            i3 = i2;
        }
        this.selectionStart = i2;
        this.selectionEnd = i3;
        TextComponentPeer textComponentPeer = (TextComponentPeer) this.peer;
        if (textComponentPeer != null) {
            textComponentPeer.select(i2, i3);
        }
    }

    public synchronized void selectAll() {
        this.selectionStart = 0;
        this.selectionEnd = getText().length();
        TextComponentPeer textComponentPeer = (TextComponentPeer) this.peer;
        if (textComponentPeer != null) {
            textComponentPeer.select(this.selectionStart, this.selectionEnd);
        }
    }

    public synchronized void setCaretPosition(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException("position less than zero.");
        }
        int length = getText().length();
        if (i2 > length) {
            i2 = length;
        }
        TextComponentPeer textComponentPeer = (TextComponentPeer) this.peer;
        if (textComponentPeer != null) {
            textComponentPeer.setCaretPosition(i2);
        } else {
            select(i2, i2);
        }
    }

    public synchronized int getCaretPosition() {
        int caretPosition;
        TextComponentPeer textComponentPeer = (TextComponentPeer) this.peer;
        if (textComponentPeer != null) {
            caretPosition = textComponentPeer.getCaretPosition();
        } else {
            caretPosition = this.selectionStart;
        }
        int length = getText().length();
        if (caretPosition > length) {
            caretPosition = length;
        }
        return caretPosition;
    }

    public synchronized void addTextListener(TextListener textListener) {
        if (textListener == null) {
            return;
        }
        this.textListener = AWTEventMulticaster.add(this.textListener, textListener);
        this.newEventsOnly = true;
    }

    public synchronized void removeTextListener(TextListener textListener) {
        if (textListener == null) {
            return;
        }
        this.textListener = AWTEventMulticaster.remove(this.textListener, textListener);
    }

    public synchronized TextListener[] getTextListeners() {
        return (TextListener[]) getListeners(TextListener.class);
    }

    @Override // java.awt.Component
    public <T extends EventListener> T[] getListeners(Class<T> cls) {
        if (cls != TextListener.class) {
            return (T[]) super.getListeners(cls);
        }
        return (T[]) AWTEventMulticaster.getListeners(this.textListener, cls);
    }

    @Override // java.awt.Component
    boolean eventEnabled(AWTEvent aWTEvent) {
        if (aWTEvent.id != 900) {
            return super.eventEnabled(aWTEvent);
        }
        if ((this.eventMask & 1024) != 0 || this.textListener != null) {
            return true;
        }
        return false;
    }

    @Override // java.awt.Component
    protected void processEvent(AWTEvent aWTEvent) {
        if (aWTEvent instanceof TextEvent) {
            processTextEvent((TextEvent) aWTEvent);
        } else {
            super.processEvent(aWTEvent);
        }
    }

    protected void processTextEvent(TextEvent textEvent) {
        TextListener textListener = this.textListener;
        if (textListener != null) {
            switch (textEvent.getID()) {
                case 900:
                    textListener.textValueChanged(textEvent);
                    break;
            }
        }
    }

    @Override // java.awt.Component
    protected String paramString() {
        String str = super.paramString() + ",text=" + getText();
        if (this.editable) {
            str = str + ",editable";
        }
        return str + ",selection=" + getSelectionStart() + LanguageTag.SEP + getSelectionEnd();
    }

    private boolean canAccessClipboard() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager == null) {
            return true;
        }
        try {
            securityManager.checkPermission(SecurityConstants.AWT.ACCESS_CLIPBOARD_PERMISSION);
            return true;
        } catch (SecurityException e2) {
            return false;
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        TextComponentPeer textComponentPeer = (TextComponentPeer) this.peer;
        if (textComponentPeer != null) {
            this.text = textComponentPeer.getText();
            this.selectionStart = textComponentPeer.getSelectionStart();
            this.selectionEnd = textComponentPeer.getSelectionEnd();
        }
        objectOutputStream.defaultWriteObject();
        AWTEventMulticaster.save(objectOutputStream, "textL", this.textListener);
        objectOutputStream.writeObject(null);
    }

    private void readObject(ObjectInputStream objectInputStream) throws HeadlessException, IOException, ClassNotFoundException {
        GraphicsEnvironment.checkHeadless();
        objectInputStream.defaultReadObject();
        this.text = this.text != null ? this.text : "";
        select(this.selectionStart, this.selectionEnd);
        while (true) {
            Object object = objectInputStream.readObject();
            if (null != object) {
                if ("textL" == ((String) object).intern()) {
                    addTextListener((TextListener) objectInputStream.readObject());
                } else {
                    objectInputStream.readObject();
                }
            } else {
                enableInputMethodsIfNecessary();
                return;
            }
        }
    }

    @Override // java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleAWTTextComponent();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:java/awt/TextComponent$AccessibleAWTTextComponent.class */
    protected class AccessibleAWTTextComponent extends Component.AccessibleAWTComponent implements AccessibleText, TextListener {
        private static final long serialVersionUID = 3631432373506317811L;
        private static final boolean NEXT = true;
        private static final boolean PREVIOUS = false;

        public AccessibleAWTTextComponent() {
            super();
            TextComponent.this.addTextListener(this);
        }

        @Override // java.awt.event.TextListener
        public void textValueChanged(TextEvent textEvent) {
            firePropertyChange(AccessibleContext.ACCESSIBLE_TEXT_PROPERTY, null, Integer.valueOf(TextComponent.this.getCaretPosition()));
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleStateSet getAccessibleStateSet() {
            AccessibleStateSet accessibleStateSet = super.getAccessibleStateSet();
            if (TextComponent.this.isEditable()) {
                accessibleStateSet.add(AccessibleState.EDITABLE);
            }
            return accessibleStateSet;
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.TEXT;
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleText getAccessibleText() {
            return this;
        }

        @Override // javax.accessibility.AccessibleText
        public int getIndexAtPoint(Point point) {
            return -1;
        }

        @Override // javax.accessibility.AccessibleText
        public Rectangle getCharacterBounds(int i2) {
            return null;
        }

        @Override // javax.accessibility.AccessibleText
        public int getCharCount() {
            return TextComponent.this.getText().length();
        }

        @Override // javax.accessibility.AccessibleText
        public int getCaretPosition() {
            return TextComponent.this.getCaretPosition();
        }

        @Override // javax.accessibility.AccessibleText
        public AttributeSet getCharacterAttribute(int i2) {
            return null;
        }

        @Override // javax.accessibility.AccessibleText
        public int getSelectionStart() {
            return TextComponent.this.getSelectionStart();
        }

        @Override // javax.accessibility.AccessibleText
        public int getSelectionEnd() {
            return TextComponent.this.getSelectionEnd();
        }

        @Override // javax.accessibility.AccessibleText
        public String getSelectedText() {
            String selectedText = TextComponent.this.getSelectedText();
            if (selectedText == null || selectedText.equals("")) {
                return null;
            }
            return selectedText;
        }

        @Override // javax.accessibility.AccessibleText
        public String getAtIndex(int i2, int i3) {
            if (i3 < 0 || i3 >= TextComponent.this.getText().length()) {
                return null;
            }
            switch (i2) {
                case 1:
                    return TextComponent.this.getText().substring(i3, i3 + 1);
                case 2:
                    String text = TextComponent.this.getText();
                    BreakIterator wordInstance = BreakIterator.getWordInstance();
                    wordInstance.setText(text);
                    return text.substring(wordInstance.previous(), wordInstance.following(i3));
                case 3:
                    String text2 = TextComponent.this.getText();
                    BreakIterator sentenceInstance = BreakIterator.getSentenceInstance();
                    sentenceInstance.setText(text2);
                    return text2.substring(sentenceInstance.previous(), sentenceInstance.following(i3));
                default:
                    return null;
            }
        }

        private int findWordLimit(int i2, BreakIterator breakIterator, boolean z2, String str) {
            int iFollowing = z2 ? breakIterator.following(i2) : breakIterator.preceding(i2);
            int next = z2 ? breakIterator.next() : breakIterator.previous();
            while (true) {
                int i3 = next;
                if (i3 != -1) {
                    for (int iMin = Math.min(iFollowing, i3); iMin < Math.max(iFollowing, i3); iMin++) {
                        if (Character.isLetter(str.charAt(iMin))) {
                            return iFollowing;
                        }
                    }
                    iFollowing = i3;
                    next = z2 ? breakIterator.next() : breakIterator.previous();
                } else {
                    return -1;
                }
            }
        }

        @Override // javax.accessibility.AccessibleText
        public String getAfterIndex(int i2, int i3) {
            int iFollowing;
            int iFollowing2;
            if (i3 < 0 || i3 >= TextComponent.this.getText().length()) {
                return null;
            }
            switch (i2) {
                case 1:
                    if (i3 + 1 >= TextComponent.this.getText().length()) {
                        return null;
                    }
                    return TextComponent.this.getText().substring(i3 + 1, i3 + 2);
                case 2:
                    String text = TextComponent.this.getText();
                    BreakIterator wordInstance = BreakIterator.getWordInstance();
                    wordInstance.setText(text);
                    int iFindWordLimit = findWordLimit(i3, wordInstance, true, text);
                    if (iFindWordLimit == -1 || iFindWordLimit >= text.length() || (iFollowing2 = wordInstance.following(iFindWordLimit)) == -1 || iFollowing2 >= text.length()) {
                        return null;
                    }
                    return text.substring(iFindWordLimit, iFollowing2);
                case 3:
                    String text2 = TextComponent.this.getText();
                    BreakIterator sentenceInstance = BreakIterator.getSentenceInstance();
                    sentenceInstance.setText(text2);
                    int iFollowing3 = sentenceInstance.following(i3);
                    if (iFollowing3 == -1 || iFollowing3 >= text2.length() || (iFollowing = sentenceInstance.following(iFollowing3)) == -1 || iFollowing >= text2.length()) {
                        return null;
                    }
                    return text2.substring(iFollowing3, iFollowing);
                default:
                    return null;
            }
        }

        @Override // javax.accessibility.AccessibleText
        public String getBeforeIndex(int i2, int i3) {
            int iPreceding;
            if (i3 < 0 || i3 > TextComponent.this.getText().length() - 1) {
                return null;
            }
            switch (i2) {
                case 1:
                    if (i3 == 0) {
                        return null;
                    }
                    return TextComponent.this.getText().substring(i3 - 1, i3);
                case 2:
                    String text = TextComponent.this.getText();
                    BreakIterator wordInstance = BreakIterator.getWordInstance();
                    wordInstance.setText(text);
                    int iFindWordLimit = findWordLimit(i3, wordInstance, false, text);
                    if (iFindWordLimit == -1 || (iPreceding = wordInstance.preceding(iFindWordLimit)) == -1) {
                        return null;
                    }
                    return text.substring(iPreceding, iFindWordLimit);
                case 3:
                    String text2 = TextComponent.this.getText();
                    BreakIterator sentenceInstance = BreakIterator.getSentenceInstance();
                    sentenceInstance.setText(text2);
                    sentenceInstance.following(i3);
                    int iPrevious = sentenceInstance.previous();
                    int iPrevious2 = sentenceInstance.previous();
                    if (iPrevious2 == -1) {
                        return null;
                    }
                    return text2.substring(iPrevious2, iPrevious);
                default:
                    return null;
            }
        }
    }
}
