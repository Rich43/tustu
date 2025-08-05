package javafx.scene.layout;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.geometry.HPos;

/* loaded from: jfxrt.jar:javafx/scene/layout/ColumnConstraints.class */
public class ColumnConstraints extends ConstraintsBase {
    private DoubleProperty minWidth;
    private DoubleProperty prefWidth;
    private DoubleProperty maxWidth;
    private DoubleProperty percentWidth;
    private ObjectProperty<Priority> hgrow;
    private ObjectProperty<HPos> halignment;
    private BooleanProperty fillWidth;

    public ColumnConstraints() {
    }

    public ColumnConstraints(double width) {
        this();
        setMinWidth(Double.NEGATIVE_INFINITY);
        setPrefWidth(width);
        setMaxWidth(Double.NEGATIVE_INFINITY);
    }

    public ColumnConstraints(double minWidth, double prefWidth, double maxWidth) {
        this();
        setMinWidth(minWidth);
        setPrefWidth(prefWidth);
        setMaxWidth(maxWidth);
    }

    public ColumnConstraints(double minWidth, double prefWidth, double maxWidth, Priority hgrow, HPos halignment, boolean fillWidth) {
        this(minWidth, prefWidth, maxWidth);
        setHgrow(hgrow);
        setHalignment(halignment);
        setFillWidth(fillWidth);
    }

    public final void setMinWidth(double value) {
        minWidthProperty().set(value);
    }

    public final double getMinWidth() {
        if (this.minWidth == null) {
            return -1.0d;
        }
        return this.minWidth.get();
    }

    public final DoubleProperty minWidthProperty() {
        if (this.minWidth == null) {
            this.minWidth = new DoublePropertyBase(-1.0d) { // from class: javafx.scene.layout.ColumnConstraints.1
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    ColumnConstraints.this.requestLayout();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ColumnConstraints.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "minWidth";
                }
            };
        }
        return this.minWidth;
    }

    public final void setPrefWidth(double value) {
        prefWidthProperty().set(value);
    }

    public final double getPrefWidth() {
        if (this.prefWidth == null) {
            return -1.0d;
        }
        return this.prefWidth.get();
    }

    public final DoubleProperty prefWidthProperty() {
        if (this.prefWidth == null) {
            this.prefWidth = new DoublePropertyBase(-1.0d) { // from class: javafx.scene.layout.ColumnConstraints.2
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    ColumnConstraints.this.requestLayout();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ColumnConstraints.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "prefWidth";
                }
            };
        }
        return this.prefWidth;
    }

    public final void setMaxWidth(double value) {
        maxWidthProperty().set(value);
    }

    public final double getMaxWidth() {
        if (this.maxWidth == null) {
            return -1.0d;
        }
        return this.maxWidth.get();
    }

    public final DoubleProperty maxWidthProperty() {
        if (this.maxWidth == null) {
            this.maxWidth = new DoublePropertyBase(-1.0d) { // from class: javafx.scene.layout.ColumnConstraints.3
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    ColumnConstraints.this.requestLayout();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ColumnConstraints.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "maxWidth";
                }
            };
        }
        return this.maxWidth;
    }

    public final void setPercentWidth(double value) {
        percentWidthProperty().set(value);
    }

    public final double getPercentWidth() {
        if (this.percentWidth == null) {
            return -1.0d;
        }
        return this.percentWidth.get();
    }

    public final DoubleProperty percentWidthProperty() {
        if (this.percentWidth == null) {
            this.percentWidth = new DoublePropertyBase(-1.0d) { // from class: javafx.scene.layout.ColumnConstraints.4
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    ColumnConstraints.this.requestLayout();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ColumnConstraints.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "percentWidth";
                }
            };
        }
        return this.percentWidth;
    }

    public final void setHgrow(Priority value) {
        hgrowProperty().set(value);
    }

    public final Priority getHgrow() {
        if (this.hgrow == null) {
            return null;
        }
        return this.hgrow.get();
    }

    public final ObjectProperty<Priority> hgrowProperty() {
        if (this.hgrow == null) {
            this.hgrow = new ObjectPropertyBase<Priority>() { // from class: javafx.scene.layout.ColumnConstraints.5
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    ColumnConstraints.this.requestLayout();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ColumnConstraints.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "hgrow";
                }
            };
        }
        return this.hgrow;
    }

    public final void setHalignment(HPos value) {
        halignmentProperty().set(value);
    }

    public final HPos getHalignment() {
        if (this.halignment == null) {
            return null;
        }
        return this.halignment.get();
    }

    public final ObjectProperty<HPos> halignmentProperty() {
        if (this.halignment == null) {
            this.halignment = new ObjectPropertyBase<HPos>() { // from class: javafx.scene.layout.ColumnConstraints.6
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    ColumnConstraints.this.requestLayout();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ColumnConstraints.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "halignment";
                }
            };
        }
        return this.halignment;
    }

    public final void setFillWidth(boolean value) {
        fillWidthProperty().set(value);
    }

    public final boolean isFillWidth() {
        if (this.fillWidth == null) {
            return true;
        }
        return this.fillWidth.get();
    }

    public final BooleanProperty fillWidthProperty() {
        if (this.fillWidth == null) {
            this.fillWidth = new BooleanPropertyBase(true) { // from class: javafx.scene.layout.ColumnConstraints.7
                @Override // javafx.beans.property.BooleanPropertyBase
                protected void invalidated() {
                    ColumnConstraints.this.requestLayout();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ColumnConstraints.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "fillWidth";
                }
            };
        }
        return this.fillWidth;
    }

    public String toString() {
        return "ColumnConstraints percentWidth=" + getPercentWidth() + " minWidth=" + getMinWidth() + " prefWidth=" + getPrefWidth() + " maxWidth=" + getMaxWidth() + " hgrow=" + ((Object) getHgrow()) + " fillWidth=" + isFillWidth() + " halignment=" + ((Object) getHalignment());
    }
}
