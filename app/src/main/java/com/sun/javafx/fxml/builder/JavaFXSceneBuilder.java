package com.sun.javafx.fxml.builder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.beans.DefaultProperty;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Builder;

@DefaultProperty("root")
/* loaded from: jfxrt.jar:com/sun/javafx/fxml/builder/JavaFXSceneBuilder.class */
public class JavaFXSceneBuilder implements Builder<Scene> {
    private Parent root = null;
    private double width = -1.0d;
    private double height = -1.0d;
    private Paint fill = Color.WHITE;
    private ArrayList<String> stylesheets = new ArrayList<>();

    public Parent getRoot() {
        return this.root;
    }

    public void setRoot(Parent root) {
        this.root = root;
    }

    public double getWidth() {
        return this.width;
    }

    public void setWidth(double width) {
        if (width < -1.0d) {
            throw new IllegalArgumentException();
        }
        this.width = width;
    }

    public double getHeight() {
        return this.height;
    }

    public void setHeight(double height) {
        if (height < -1.0d) {
            throw new IllegalArgumentException();
        }
        this.height = height;
    }

    public Paint getFill() {
        return this.fill;
    }

    public void setFill(Paint fill) {
        if (fill == null) {
            throw new NullPointerException();
        }
        this.fill = fill;
    }

    public List<String> getStylesheets() {
        return this.stylesheets;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public Scene build2() {
        Scene scene = new Scene(this.root, this.width, this.height, this.fill);
        Iterator<String> it = this.stylesheets.iterator();
        while (it.hasNext()) {
            String stylesheet = it.next();
            scene.getStylesheets().add(stylesheet);
        }
        return scene;
    }
}
