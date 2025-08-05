package com.sun.beans.editors;

import java.awt.Canvas;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.TextField;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditor;
import javafx.fxml.FXMLLoader;

/* loaded from: rt.jar:com/sun/beans/editors/ColorEditor.class */
public class ColorEditor extends Panel implements PropertyEditor {
    private static final long serialVersionUID = 1781257185164716054L;
    private Canvas sample;
    private int ourWidth;
    private Color color;
    private TextField text;
    private Choice choser;
    private String[] colorNames = {" ", "white", "lightGray", "gray", "darkGray", "black", "red", "pink", "orange", "yellow", "green", "magenta", "cyan", "blue"};
    private Color[] colors = {null, Color.white, Color.lightGray, Color.gray, Color.darkGray, Color.black, Color.red, Color.pink, Color.orange, Color.yellow, Color.green, Color.magenta, Color.cyan, Color.blue};
    private int sampleHeight = 20;
    private int sampleWidth = 40;
    private int hPad = 5;
    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    public ColorEditor() {
        setLayout(null);
        this.ourWidth = this.hPad;
        Panel panel = new Panel();
        panel.setLayout(null);
        panel.setBackground(Color.black);
        this.sample = new Canvas();
        panel.add(this.sample);
        this.sample.reshape(2, 2, this.sampleWidth, this.sampleHeight);
        add(panel);
        panel.reshape(this.ourWidth, 2, this.sampleWidth + 4, this.sampleHeight + 4);
        this.ourWidth += this.sampleWidth + 4 + this.hPad;
        this.text = new TextField("", 14);
        add(this.text);
        this.text.reshape(this.ourWidth, 0, 100, 30);
        this.ourWidth += 100 + this.hPad;
        this.choser = new Choice();
        for (int i2 = 0; i2 < this.colorNames.length; i2++) {
            this.choser.addItem(this.colorNames[i2]);
        }
        add(this.choser);
        this.choser.reshape(this.ourWidth, 0, 100, 30);
        this.ourWidth += 100 + this.hPad;
        resize(this.ourWidth, 40);
    }

    @Override // java.beans.PropertyEditor
    public void setValue(Object obj) {
        changeColor((Color) obj);
    }

    @Override // java.awt.Container, java.awt.Component
    public Dimension preferredSize() {
        return new Dimension(this.ourWidth, 40);
    }

    @Override // java.awt.Component
    public boolean keyUp(Event event, int i2) {
        if (event.target == this.text) {
            try {
                setAsText(this.text.getText());
                return false;
            } catch (IllegalArgumentException e2) {
                return false;
            }
        }
        return false;
    }

    @Override // java.beans.PropertyEditor
    public void setAsText(String str) throws IllegalArgumentException {
        if (str == null) {
            changeColor(null);
            return;
        }
        int iIndexOf = str.indexOf(44);
        int iIndexOf2 = str.indexOf(44, iIndexOf + 1);
        if (iIndexOf < 0 || iIndexOf2 < 0) {
            throw new IllegalArgumentException(str);
        }
        try {
            changeColor(new Color(Integer.parseInt(str.substring(0, iIndexOf)), Integer.parseInt(str.substring(iIndexOf + 1, iIndexOf2)), Integer.parseInt(str.substring(iIndexOf2 + 1))));
        } catch (Exception e2) {
            throw new IllegalArgumentException(str);
        }
    }

    @Override // java.awt.Component
    public boolean action(Event event, Object obj) {
        if (event.target == this.choser) {
            changeColor(this.colors[this.choser.getSelectedIndex()]);
            return false;
        }
        return false;
    }

    @Override // java.beans.PropertyEditor
    public String getJavaInitializationString() {
        return this.color != null ? "new java.awt.Color(" + this.color.getRGB() + ",true)" : FXMLLoader.NULL_KEYWORD;
    }

    private void changeColor(Color color) {
        if (color == null) {
            this.color = null;
            this.text.setText("");
            return;
        }
        this.color = color;
        this.text.setText("" + color.getRed() + "," + color.getGreen() + "," + color.getBlue());
        int i2 = 0;
        for (int i3 = 0; i3 < this.colorNames.length; i3++) {
            if (this.color.equals(this.colors[i3])) {
                i2 = i3;
            }
        }
        this.choser.select(i2);
        this.sample.setBackground(this.color);
        this.sample.repaint();
        this.support.firePropertyChange("", (Object) null, (Object) null);
    }

    @Override // java.beans.PropertyEditor
    public Object getValue() {
        return this.color;
    }

    @Override // java.beans.PropertyEditor
    public boolean isPaintable() {
        return true;
    }

    @Override // java.beans.PropertyEditor
    public void paintValue(Graphics graphics, Rectangle rectangle) {
        Color color = graphics.getColor();
        graphics.setColor(Color.black);
        graphics.drawRect(rectangle.f12372x, rectangle.f12373y, rectangle.width - 3, rectangle.height - 3);
        graphics.setColor(this.color);
        graphics.fillRect(rectangle.f12372x + 1, rectangle.f12373y + 1, rectangle.width - 4, rectangle.height - 4);
        graphics.setColor(color);
    }

    @Override // java.beans.PropertyEditor
    public String getAsText() {
        if (this.color != null) {
            return this.color.getRed() + "," + this.color.getGreen() + "," + this.color.getBlue();
        }
        return null;
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
