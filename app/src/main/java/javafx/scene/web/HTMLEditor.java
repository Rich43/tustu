package javafx.scene.web;

import com.sun.javafx.scene.web.skin.HTMLEditorSkin;
import javafx.css.StyleableProperty;
import javafx.print.PrinterJob;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

/* loaded from: jfxrt.jar:javafx/scene/web/HTMLEditor.class */
public class HTMLEditor extends Control {
    public HTMLEditor() {
        ((StyleableProperty) super.skinClassNameProperty()).applyStyle(null, "com.sun.javafx.scene.web.skin.HTMLEditorSkin");
        getStyleClass().add("html-editor");
    }

    @Override // javafx.scene.control.Control
    protected Skin<?> createDefaultSkin() {
        return new HTMLEditorSkin(this);
    }

    public String getHtmlText() {
        return ((HTMLEditorSkin) getSkin()).getHTMLText();
    }

    public void setHtmlText(String htmlText) {
        ((HTMLEditorSkin) getSkin()).setHTMLText(htmlText);
    }

    public void print(PrinterJob job) {
        ((HTMLEditorSkin) getSkin()).print(job);
    }
}
