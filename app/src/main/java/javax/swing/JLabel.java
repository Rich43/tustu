package javax.swing;

import java.awt.Component;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.beans.Transient;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.BreakIterator;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleExtendedComponent;
import javax.accessibility.AccessibleIcon;
import javax.accessibility.AccessibleKeyBinding;
import javax.accessibility.AccessibleRelation;
import javax.accessibility.AccessibleRelationSet;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleText;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.LabelUI;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Position;
import javax.swing.text.StyledDocument;
import javax.swing.text.View;

/* loaded from: rt.jar:javax/swing/JLabel.class */
public class JLabel extends JComponent implements SwingConstants, Accessible {
    private static final String uiClassID = "LabelUI";
    private int mnemonic;
    private int mnemonicIndex;
    private String text;
    private Icon defaultIcon;
    private Icon disabledIcon;
    private boolean disabledIconSet;
    private int verticalAlignment;
    private int horizontalAlignment;
    private int verticalTextPosition;
    private int horizontalTextPosition;
    private int iconTextGap;
    protected Component labelFor;
    static final String LABELED_BY_PROPERTY = "labeledBy";

    public JLabel(String str, Icon icon, int i2) throws IllegalArgumentException {
        this.mnemonic = 0;
        this.mnemonicIndex = -1;
        this.text = "";
        this.defaultIcon = null;
        this.disabledIcon = null;
        this.disabledIconSet = false;
        this.verticalAlignment = 0;
        this.horizontalAlignment = 10;
        this.verticalTextPosition = 0;
        this.horizontalTextPosition = 11;
        this.iconTextGap = 4;
        this.labelFor = null;
        setText(str);
        setIcon(icon);
        setHorizontalAlignment(i2);
        updateUI();
        setAlignmentX(0.0f);
    }

    public JLabel(String str, int i2) {
        this(str, null, i2);
    }

    public JLabel(String str) {
        this(str, null, 10);
    }

    public JLabel(Icon icon, int i2) {
        this(null, icon, i2);
    }

    public JLabel(Icon icon) {
        this(null, icon, 0);
    }

    public JLabel() {
        this("", null, 10);
    }

    public LabelUI getUI() {
        return (LabelUI) this.ui;
    }

    public void setUI(LabelUI labelUI) {
        super.setUI((ComponentUI) labelUI);
        if (!this.disabledIconSet && this.disabledIcon != null) {
            setDisabledIcon(null);
        }
    }

    @Override // javax.swing.JComponent
    public void updateUI() {
        setUI((LabelUI) UIManager.getUI(this));
    }

    @Override // javax.swing.JComponent
    public String getUIClassID() {
        return uiClassID;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String str) throws IllegalArgumentException {
        String accessibleName = null;
        if (this.accessibleContext != null) {
            accessibleName = this.accessibleContext.getAccessibleName();
        }
        String str2 = this.text;
        this.text = str;
        firePropertyChange("text", str2, str);
        setDisplayedMnemonicIndex(SwingUtilities.findDisplayedMnemonicIndex(str, getDisplayedMnemonic()));
        if (this.accessibleContext != null && this.accessibleContext.getAccessibleName() != accessibleName) {
            this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_VISIBLE_DATA_PROPERTY, accessibleName, this.accessibleContext.getAccessibleName());
        }
        if (str == null || str2 == null || !str.equals(str2)) {
            revalidate();
            repaint();
        }
    }

    public Icon getIcon() {
        return this.defaultIcon;
    }

    public void setIcon(Icon icon) {
        Icon icon2 = this.defaultIcon;
        this.defaultIcon = icon;
        if (this.defaultIcon != icon2 && !this.disabledIconSet) {
            this.disabledIcon = null;
        }
        firePropertyChange("icon", icon2, this.defaultIcon);
        if (this.accessibleContext != null && icon2 != this.defaultIcon) {
            this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_VISIBLE_DATA_PROPERTY, icon2, this.defaultIcon);
        }
        if (this.defaultIcon != icon2) {
            if (this.defaultIcon == null || icon2 == null || this.defaultIcon.getIconWidth() != icon2.getIconWidth() || this.defaultIcon.getIconHeight() != icon2.getIconHeight()) {
                revalidate();
            }
            repaint();
        }
    }

    @Transient
    public Icon getDisabledIcon() {
        if (!this.disabledIconSet && this.disabledIcon == null && this.defaultIcon != null) {
            this.disabledIcon = UIManager.getLookAndFeel().getDisabledIcon(this, this.defaultIcon);
            if (this.disabledIcon != null) {
                firePropertyChange(AbstractButton.DISABLED_ICON_CHANGED_PROPERTY, (Object) null, this.disabledIcon);
            }
        }
        return this.disabledIcon;
    }

    public void setDisabledIcon(Icon icon) {
        Icon icon2 = this.disabledIcon;
        this.disabledIcon = icon;
        this.disabledIconSet = icon != null;
        firePropertyChange(AbstractButton.DISABLED_ICON_CHANGED_PROPERTY, icon2, icon);
        if (icon != icon2) {
            if (icon == null || icon2 == null || icon.getIconWidth() != icon2.getIconWidth() || icon.getIconHeight() != icon2.getIconHeight()) {
                revalidate();
            }
            if (!isEnabled()) {
                repaint();
            }
        }
    }

    public void setDisplayedMnemonic(int i2) throws IllegalArgumentException {
        int i3 = this.mnemonic;
        this.mnemonic = i2;
        firePropertyChange("displayedMnemonic", i3, this.mnemonic);
        setDisplayedMnemonicIndex(SwingUtilities.findDisplayedMnemonicIndex(getText(), this.mnemonic));
        if (i2 != i3) {
            revalidate();
            repaint();
        }
    }

    public void setDisplayedMnemonic(char c2) throws IllegalArgumentException {
        int extendedKeyCodeForChar = KeyEvent.getExtendedKeyCodeForChar(c2);
        if (extendedKeyCodeForChar != 0) {
            setDisplayedMnemonic(extendedKeyCodeForChar);
        }
    }

    public int getDisplayedMnemonic() {
        return this.mnemonic;
    }

    public void setDisplayedMnemonicIndex(int i2) throws IllegalArgumentException {
        int i3 = this.mnemonicIndex;
        if (i2 == -1) {
            this.mnemonicIndex = -1;
        } else {
            String text = getText();
            int length = text == null ? 0 : text.length();
            if (i2 < -1 || i2 >= length) {
                throw new IllegalArgumentException("index == " + i2);
            }
        }
        this.mnemonicIndex = i2;
        firePropertyChange("displayedMnemonicIndex", i3, i2);
        if (i2 != i3) {
            revalidate();
            repaint();
        }
    }

    public int getDisplayedMnemonicIndex() {
        return this.mnemonicIndex;
    }

    protected int checkHorizontalKey(int i2, String str) {
        if (i2 == 2 || i2 == 0 || i2 == 4 || i2 == 10 || i2 == 11) {
            return i2;
        }
        throw new IllegalArgumentException(str);
    }

    protected int checkVerticalKey(int i2, String str) {
        if (i2 == 1 || i2 == 0 || i2 == 3) {
            return i2;
        }
        throw new IllegalArgumentException(str);
    }

    public int getIconTextGap() {
        return this.iconTextGap;
    }

    public void setIconTextGap(int i2) {
        int i3 = this.iconTextGap;
        this.iconTextGap = i2;
        firePropertyChange("iconTextGap", i3, i2);
        if (i2 != i3) {
            revalidate();
            repaint();
        }
    }

    public int getVerticalAlignment() {
        return this.verticalAlignment;
    }

    public void setVerticalAlignment(int i2) {
        if (i2 == this.verticalAlignment) {
            return;
        }
        int i3 = this.verticalAlignment;
        this.verticalAlignment = checkVerticalKey(i2, AbstractButton.VERTICAL_ALIGNMENT_CHANGED_PROPERTY);
        firePropertyChange(AbstractButton.VERTICAL_ALIGNMENT_CHANGED_PROPERTY, i3, this.verticalAlignment);
        repaint();
    }

    public int getHorizontalAlignment() {
        return this.horizontalAlignment;
    }

    public void setHorizontalAlignment(int i2) {
        if (i2 == this.horizontalAlignment) {
            return;
        }
        int i3 = this.horizontalAlignment;
        this.horizontalAlignment = checkHorizontalKey(i2, AbstractButton.HORIZONTAL_ALIGNMENT_CHANGED_PROPERTY);
        firePropertyChange(AbstractButton.HORIZONTAL_ALIGNMENT_CHANGED_PROPERTY, i3, this.horizontalAlignment);
        repaint();
    }

    public int getVerticalTextPosition() {
        return this.verticalTextPosition;
    }

    public void setVerticalTextPosition(int i2) {
        if (i2 == this.verticalTextPosition) {
            return;
        }
        int i3 = this.verticalTextPosition;
        this.verticalTextPosition = checkVerticalKey(i2, AbstractButton.VERTICAL_TEXT_POSITION_CHANGED_PROPERTY);
        firePropertyChange(AbstractButton.VERTICAL_TEXT_POSITION_CHANGED_PROPERTY, i3, this.verticalTextPosition);
        revalidate();
        repaint();
    }

    public int getHorizontalTextPosition() {
        return this.horizontalTextPosition;
    }

    public void setHorizontalTextPosition(int i2) {
        int i3 = this.horizontalTextPosition;
        this.horizontalTextPosition = checkHorizontalKey(i2, AbstractButton.HORIZONTAL_TEXT_POSITION_CHANGED_PROPERTY);
        firePropertyChange(AbstractButton.HORIZONTAL_TEXT_POSITION_CHANGED_PROPERTY, i3, this.horizontalTextPosition);
        revalidate();
        repaint();
    }

    @Override // java.awt.Component, java.awt.image.ImageObserver
    public boolean imageUpdate(Image image, int i2, int i3, int i4, int i5, int i6) {
        if (isShowing()) {
            if (!SwingUtilities.doesIconReferenceImage(getIcon(), image) && !SwingUtilities.doesIconReferenceImage(this.disabledIcon, image)) {
                return false;
            }
            return super.imageUpdate(image, i2, i3, i4, i5, i6);
        }
        return false;
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

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    protected String paramString() {
        String str;
        String str2;
        String str3;
        String str4;
        String str5 = this.text != null ? this.text : "";
        String string = (this.defaultIcon == null || this.defaultIcon == this) ? "" : this.defaultIcon.toString();
        String string2 = (this.disabledIcon == null || this.disabledIcon == this) ? "" : this.disabledIcon.toString();
        String string3 = this.labelFor != null ? this.labelFor.toString() : "";
        if (this.verticalAlignment == 1) {
            str = "TOP";
        } else if (this.verticalAlignment == 0) {
            str = "CENTER";
        } else if (this.verticalAlignment == 3) {
            str = "BOTTOM";
        } else {
            str = "";
        }
        if (this.horizontalAlignment == 2) {
            str2 = "LEFT";
        } else if (this.horizontalAlignment == 0) {
            str2 = "CENTER";
        } else if (this.horizontalAlignment == 4) {
            str2 = "RIGHT";
        } else if (this.horizontalAlignment == 10) {
            str2 = "LEADING";
        } else if (this.horizontalAlignment == 11) {
            str2 = "TRAILING";
        } else {
            str2 = "";
        }
        if (this.verticalTextPosition == 1) {
            str3 = "TOP";
        } else if (this.verticalTextPosition == 0) {
            str3 = "CENTER";
        } else if (this.verticalTextPosition == 3) {
            str3 = "BOTTOM";
        } else {
            str3 = "";
        }
        if (this.horizontalTextPosition == 2) {
            str4 = "LEFT";
        } else if (this.horizontalTextPosition == 0) {
            str4 = "CENTER";
        } else if (this.horizontalTextPosition == 4) {
            str4 = "RIGHT";
        } else if (this.horizontalTextPosition == 10) {
            str4 = "LEADING";
        } else if (this.horizontalTextPosition == 11) {
            str4 = "TRAILING";
        } else {
            str4 = "";
        }
        return super.paramString() + ",defaultIcon=" + string + ",disabledIcon=" + string2 + ",horizontalAlignment=" + str2 + ",horizontalTextPosition=" + str4 + ",iconTextGap=" + this.iconTextGap + ",labelFor=" + string3 + ",text=" + str5 + ",verticalAlignment=" + str + ",verticalTextPosition=" + str3;
    }

    public Component getLabelFor() {
        return this.labelFor;
    }

    public void setLabelFor(Component component) {
        Component component2 = this.labelFor;
        this.labelFor = component;
        firePropertyChange("labelFor", component2, component);
        if (component2 instanceof JComponent) {
            ((JComponent) component2).putClientProperty(LABELED_BY_PROPERTY, null);
        }
        if (component instanceof JComponent) {
            ((JComponent) component).putClientProperty(LABELED_BY_PROPERTY, this);
        }
    }

    @Override // java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleJLabel();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/JLabel$AccessibleJLabel.class */
    protected class AccessibleJLabel extends JComponent.AccessibleJComponent implements AccessibleText, AccessibleExtendedComponent {
        protected AccessibleJLabel() {
            super();
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public String getAccessibleName() {
            String accessibleName = this.accessibleName;
            if (accessibleName == null) {
                accessibleName = (String) JLabel.this.getClientProperty(AccessibleContext.ACCESSIBLE_NAME_PROPERTY);
            }
            if (accessibleName == null) {
                accessibleName = JLabel.this.getText();
            }
            if (accessibleName == null) {
                accessibleName = super.getAccessibleName();
            }
            return accessibleName;
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.LABEL;
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleIcon[] getAccessibleIcon() {
            Object accessibleContext;
            Icon icon = JLabel.this.getIcon();
            if ((icon instanceof Accessible) && (accessibleContext = ((Accessible) icon).getAccessibleContext()) != null && (accessibleContext instanceof AccessibleIcon)) {
                return new AccessibleIcon[]{(AccessibleIcon) accessibleContext};
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleRelationSet getAccessibleRelationSet() {
            Component labelFor;
            AccessibleRelationSet accessibleRelationSet = super.getAccessibleRelationSet();
            if (!accessibleRelationSet.contains(AccessibleRelation.LABEL_FOR) && (labelFor = JLabel.this.getLabelFor()) != null) {
                AccessibleRelation accessibleRelation = new AccessibleRelation(AccessibleRelation.LABEL_FOR);
                accessibleRelation.setTarget(labelFor);
                accessibleRelationSet.add(accessibleRelation);
            }
            return accessibleRelationSet;
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleText getAccessibleText() {
            if (((View) JLabel.this.getClientProperty("html")) != null) {
                return this;
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleText
        public int getIndexAtPoint(Point point) {
            Rectangle textRectangle;
            View view = (View) JLabel.this.getClientProperty("html");
            if (view == null || (textRectangle = getTextRectangle()) == null) {
                return -1;
            }
            return view.viewToModel(point.f12370x, point.f12371y, new Rectangle2D.Float(textRectangle.f12372x, textRectangle.f12373y, textRectangle.width, textRectangle.height), new Position.Bias[1]);
        }

        @Override // javax.accessibility.AccessibleText
        public Rectangle getCharacterBounds(int i2) {
            Rectangle textRectangle;
            View view = (View) JLabel.this.getClientProperty("html");
            if (view == null || (textRectangle = getTextRectangle()) == null) {
                return null;
            }
            try {
                return view.modelToView(i2, new Rectangle2D.Float(textRectangle.f12372x, textRectangle.f12373y, textRectangle.width, textRectangle.height), Position.Bias.Forward).getBounds();
            } catch (BadLocationException e2) {
                return null;
            }
        }

        @Override // javax.accessibility.AccessibleText
        public int getCharCount() {
            View view = (View) JLabel.this.getClientProperty("html");
            if (view != null) {
                Document document = view.getDocument();
                if (document instanceof StyledDocument) {
                    return ((StyledDocument) document).getLength();
                }
            }
            return JLabel.this.accessibleContext.getAccessibleName().length();
        }

        @Override // javax.accessibility.AccessibleText
        public int getCaretPosition() {
            return -1;
        }

        @Override // javax.accessibility.AccessibleText
        public String getAtIndex(int i2, int i3) {
            if (i3 < 0 || i3 >= getCharCount()) {
                return null;
            }
            switch (i2) {
                case 1:
                    try {
                        return getText(i3, 1);
                    } catch (BadLocationException e2) {
                        return null;
                    }
                case 2:
                    try {
                        String text = getText(0, getCharCount());
                        BreakIterator wordInstance = BreakIterator.getWordInstance(getLocale());
                        wordInstance.setText(text);
                        return text.substring(wordInstance.previous(), wordInstance.following(i3));
                    } catch (BadLocationException e3) {
                        return null;
                    }
                case 3:
                    try {
                        String text2 = getText(0, getCharCount());
                        BreakIterator sentenceInstance = BreakIterator.getSentenceInstance(getLocale());
                        sentenceInstance.setText(text2);
                        return text2.substring(sentenceInstance.previous(), sentenceInstance.following(i3));
                    } catch (BadLocationException e4) {
                        return null;
                    }
                default:
                    return null;
            }
        }

        @Override // javax.accessibility.AccessibleText
        public String getAfterIndex(int i2, int i3) {
            int iFollowing;
            int iFollowing2;
            if (i3 < 0 || i3 >= getCharCount()) {
                return null;
            }
            switch (i2) {
                case 1:
                    if (i3 + 1 >= getCharCount()) {
                        return null;
                    }
                    try {
                        return getText(i3 + 1, 1);
                    } catch (BadLocationException e2) {
                        return null;
                    }
                case 2:
                    try {
                        String text = getText(0, getCharCount());
                        BreakIterator wordInstance = BreakIterator.getWordInstance(getLocale());
                        wordInstance.setText(text);
                        int iFollowing3 = wordInstance.following(i3);
                        if (iFollowing3 == -1 || iFollowing3 >= text.length() || (iFollowing = wordInstance.following(iFollowing3)) == -1 || iFollowing >= text.length()) {
                            return null;
                        }
                        return text.substring(iFollowing3, iFollowing);
                    } catch (BadLocationException e3) {
                        return null;
                    }
                case 3:
                    try {
                        String text2 = getText(0, getCharCount());
                        BreakIterator sentenceInstance = BreakIterator.getSentenceInstance(getLocale());
                        sentenceInstance.setText(text2);
                        int iFollowing4 = sentenceInstance.following(i3);
                        if (iFollowing4 == -1 || iFollowing4 > text2.length() || (iFollowing2 = sentenceInstance.following(iFollowing4)) == -1 || iFollowing2 > text2.length()) {
                            return null;
                        }
                        return text2.substring(iFollowing4, iFollowing2);
                    } catch (BadLocationException e4) {
                        return null;
                    }
                default:
                    return null;
            }
        }

        @Override // javax.accessibility.AccessibleText
        public String getBeforeIndex(int i2, int i3) {
            if (i3 < 0 || i3 > getCharCount() - 1) {
                return null;
            }
            switch (i2) {
                case 1:
                    if (i3 == 0) {
                        return null;
                    }
                    try {
                        return getText(i3 - 1, 1);
                    } catch (BadLocationException e2) {
                        return null;
                    }
                case 2:
                    try {
                        String text = getText(0, getCharCount());
                        BreakIterator wordInstance = BreakIterator.getWordInstance(getLocale());
                        wordInstance.setText(text);
                        wordInstance.following(i3);
                        int iPrevious = wordInstance.previous();
                        int iPrevious2 = wordInstance.previous();
                        if (iPrevious2 == -1) {
                            return null;
                        }
                        return text.substring(iPrevious2, iPrevious);
                    } catch (BadLocationException e3) {
                        return null;
                    }
                case 3:
                    try {
                        String text2 = getText(0, getCharCount());
                        BreakIterator sentenceInstance = BreakIterator.getSentenceInstance(getLocale());
                        sentenceInstance.setText(text2);
                        sentenceInstance.following(i3);
                        int iPrevious3 = sentenceInstance.previous();
                        int iPrevious4 = sentenceInstance.previous();
                        if (iPrevious4 == -1) {
                            return null;
                        }
                        return text2.substring(iPrevious4, iPrevious3);
                    } catch (BadLocationException e4) {
                        return null;
                    }
                default:
                    return null;
            }
        }

        @Override // javax.accessibility.AccessibleText
        public AttributeSet getCharacterAttribute(int i2) {
            Element characterElement;
            View view = (View) JLabel.this.getClientProperty("html");
            if (view != null) {
                Document document = view.getDocument();
                if ((document instanceof StyledDocument) && (characterElement = ((StyledDocument) document).getCharacterElement(i2)) != null) {
                    return characterElement.getAttributes();
                }
                return null;
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleText
        public int getSelectionStart() {
            return -1;
        }

        @Override // javax.accessibility.AccessibleText
        public int getSelectionEnd() {
            return -1;
        }

        @Override // javax.accessibility.AccessibleText
        public String getSelectedText() {
            return null;
        }

        private String getText(int i2, int i3) throws BadLocationException {
            View view = (View) JLabel.this.getClientProperty("html");
            if (view != null) {
                Document document = view.getDocument();
                if (document instanceof StyledDocument) {
                    return ((StyledDocument) document).getText(i2, i3);
                }
                return null;
            }
            return null;
        }

        private Rectangle getTextRectangle() {
            String text = JLabel.this.getText();
            Icon icon = JLabel.this.isEnabled() ? JLabel.this.getIcon() : JLabel.this.getDisabledIcon();
            if (icon == null && text == null) {
                return null;
            }
            Rectangle rectangle = new Rectangle();
            Rectangle rectangle2 = new Rectangle();
            Rectangle rectangle3 = new Rectangle();
            Insets insets = JLabel.this.getInsets(new Insets(0, 0, 0, 0));
            rectangle3.f12372x = insets.left;
            rectangle3.f12373y = insets.top;
            rectangle3.width = JLabel.this.getWidth() - (insets.left + insets.right);
            rectangle3.height = JLabel.this.getHeight() - (insets.top + insets.bottom);
            SwingUtilities.layoutCompoundLabel(JLabel.this, getFontMetrics(getFont()), text, icon, JLabel.this.getVerticalAlignment(), JLabel.this.getHorizontalAlignment(), JLabel.this.getVerticalTextPosition(), JLabel.this.getHorizontalTextPosition(), rectangle3, rectangle, rectangle2, JLabel.this.getIconTextGap());
            return rectangle2;
        }

        @Override // javax.swing.JComponent.AccessibleJComponent
        AccessibleExtendedComponent getAccessibleExtendedComponent() {
            return this;
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, javax.accessibility.AccessibleExtendedComponent
        public String getToolTipText() {
            return JLabel.this.getToolTipText();
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, javax.accessibility.AccessibleExtendedComponent
        public String getTitledBorderText() {
            return super.getTitledBorderText();
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, javax.accessibility.AccessibleExtendedComponent
        public AccessibleKeyBinding getAccessibleKeyBinding() {
            int displayedMnemonic = JLabel.this.getDisplayedMnemonic();
            if (displayedMnemonic == 0) {
                return null;
            }
            return new LabelKeyBinding(displayedMnemonic);
        }

        /* loaded from: rt.jar:javax/swing/JLabel$AccessibleJLabel$LabelKeyBinding.class */
        class LabelKeyBinding implements AccessibleKeyBinding {
            int mnemonic;

            LabelKeyBinding(int i2) {
                this.mnemonic = i2;
            }

            @Override // javax.accessibility.AccessibleKeyBinding
            public int getAccessibleKeyBindingCount() {
                return 1;
            }

            @Override // javax.accessibility.AccessibleKeyBinding
            public Object getAccessibleKeyBinding(int i2) {
                if (i2 != 0) {
                    throw new IllegalArgumentException();
                }
                return KeyStroke.getKeyStroke(this.mnemonic, 0);
            }
        }
    }
}
