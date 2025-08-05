package com.sun.beans.editors;

import java.awt.Choice;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditor;
import javafx.fxml.FXMLLoader;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:com/sun/beans/editors/FontEditor.class */
public class FontEditor extends Panel implements PropertyEditor {
    private static final long serialVersionUID = 6732704486002715933L;
    private Font font;
    private Toolkit toolkit;
    private Label sample;
    private Choice familyChoser;
    private Choice styleChoser;
    private Choice sizeChoser;
    private String[] fonts;
    private String sampleText = "Abcde...";
    private String[] styleNames = {"plain", "bold", "italic"};
    private int[] styles = {0, 1, 2};
    private int[] pointSizes = {3, 5, 8, 10, 12, 14, 18, 24, 36, 48};
    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    public FontEditor() {
        setLayout(null);
        this.toolkit = Toolkit.getDefaultToolkit();
        this.fonts = this.toolkit.getFontList();
        this.familyChoser = new Choice();
        for (int i2 = 0; i2 < this.fonts.length; i2++) {
            this.familyChoser.addItem(this.fonts[i2]);
        }
        add(this.familyChoser);
        this.familyChoser.reshape(20, 5, 100, 30);
        this.styleChoser = new Choice();
        for (int i3 = 0; i3 < this.styleNames.length; i3++) {
            this.styleChoser.addItem(this.styleNames[i3]);
        }
        add(this.styleChoser);
        this.styleChoser.reshape(145, 5, 70, 30);
        this.sizeChoser = new Choice();
        for (int i4 = 0; i4 < this.pointSizes.length; i4++) {
            this.sizeChoser.addItem("" + this.pointSizes[i4]);
        }
        add(this.sizeChoser);
        this.sizeChoser.reshape(220, 5, 70, 30);
        resize(300, 40);
    }

    @Override // java.awt.Container, java.awt.Component
    public Dimension preferredSize() {
        return new Dimension(300, 40);
    }

    @Override // java.beans.PropertyEditor
    public void setValue(Object obj) {
        this.font = (Font) obj;
        if (this.font == null) {
            return;
        }
        changeFont(this.font);
        int i2 = 0;
        while (true) {
            if (i2 >= this.fonts.length) {
                break;
            }
            if (!this.fonts[i2].equals(this.font.getFamily())) {
                i2++;
            } else {
                this.familyChoser.select(i2);
                break;
            }
        }
        int i3 = 0;
        while (true) {
            if (i3 >= this.styleNames.length) {
                break;
            }
            if (this.font.getStyle() != this.styles[i3]) {
                i3++;
            } else {
                this.styleChoser.select(i3);
                break;
            }
        }
        for (int i4 = 0; i4 < this.pointSizes.length; i4++) {
            if (this.font.getSize() <= this.pointSizes[i4]) {
                this.sizeChoser.select(i4);
                return;
            }
        }
    }

    private void changeFont(Font font) {
        this.font = font;
        if (this.sample != null) {
            remove(this.sample);
        }
        this.sample = new Label(this.sampleText);
        this.sample.setFont(this.font);
        add(this.sample);
        Container parent = getParent();
        if (parent != null) {
            parent.invalidate();
            parent.layout();
        }
        invalidate();
        layout();
        repaint();
        this.support.firePropertyChange("", (Object) null, (Object) null);
    }

    @Override // java.beans.PropertyEditor
    public Object getValue() {
        return this.font;
    }

    @Override // java.beans.PropertyEditor
    public String getJavaInitializationString() {
        if (this.font == null) {
            return FXMLLoader.NULL_KEYWORD;
        }
        return "new java.awt.Font(\"" + this.font.getName() + "\", " + this.font.getStyle() + ", " + this.font.getSize() + ")";
    }

    @Override // java.awt.Component
    public boolean action(Event event, Object obj) {
        String selectedItem = this.familyChoser.getSelectedItem();
        int i2 = this.styles[this.styleChoser.getSelectedIndex()];
        int i3 = this.pointSizes[this.sizeChoser.getSelectedIndex()];
        try {
            changeFont(new Font(selectedItem, i2, i3));
            return false;
        } catch (Exception e2) {
            System.err.println("Couldn't create font " + selectedItem + LanguageTag.SEP + this.styleNames[i2] + LanguageTag.SEP + i3);
            return false;
        }
    }

    @Override // java.beans.PropertyEditor
    public boolean isPaintable() {
        return true;
    }

    @Override // java.beans.PropertyEditor
    public void paintValue(Graphics graphics, Rectangle rectangle) {
        Font font = graphics.getFont();
        graphics.setFont(this.font);
        graphics.drawString(this.sampleText, 0, rectangle.height - ((rectangle.height - graphics.getFontMetrics().getAscent()) / 2));
        graphics.setFont(font);
    }

    @Override // java.beans.PropertyEditor
    public String getAsText() {
        if (this.font == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(this.font.getName());
        sb.append(' ');
        boolean zIsBold = this.font.isBold();
        if (zIsBold) {
            sb.append("BOLD");
        }
        boolean zIsItalic = this.font.isItalic();
        if (zIsItalic) {
            sb.append("ITALIC");
        }
        if (zIsBold || zIsItalic) {
            sb.append(' ');
        }
        sb.append(this.font.getSize());
        return sb.toString();
    }

    @Override // java.beans.PropertyEditor
    public void setAsText(String str) throws IllegalArgumentException {
        setValue(str == null ? null : Font.decode(str));
    }

    @Override // java.beans.PropertyEditor
    public String[] getTags() {
        return null;
    }

    @Override // java.beans.PropertyEditor
    public Component getCustomEditor() {
        return this;
    }

    @Override // java.beans.PropertyEditor
    public boolean supportsCustomEditor() {
        return true;
    }

    @Override // java.awt.Container, java.awt.Component, java.beans.PropertyEditor
    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.support.addPropertyChangeListener(propertyChangeListener);
    }

    @Override // java.awt.Component, java.beans.PropertyEditor
    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.support.removePropertyChangeListener(propertyChangeListener);
    }
}
