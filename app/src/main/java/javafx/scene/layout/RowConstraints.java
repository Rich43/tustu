package javafx.scene.layout;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.geometry.VPos;

/* loaded from: jfxrt.jar:javafx/scene/layout/RowConstraints.class */
public class RowConstraints extends ConstraintsBase {
    private DoubleProperty minHeight;
    private DoubleProperty prefHeight;
    private DoubleProperty maxHeight;
    private DoubleProperty percentHeight;
    private ObjectProperty<Priority> vgrow;
    private ObjectProperty<VPos> valignment;
    private BooleanProperty fillHeight;

    public RowConstraints() {
    }

    public RowConstraints(double height) {
        this();
        setMinHeight(Double.NEGATIVE_INFINITY);
        setPrefHeight(height);
        setMaxHeight(Double.NEGATIVE_INFINITY);
    }

    public RowConstraints(double minHeight, double prefHeight, double maxHeight) {
        this();
        setMinHeight(minHeight);
        setPrefHeight(prefHeight);
        setMaxHeight(maxHeight);
    }

    public RowConstraints(double minHeight, double prefHeight, double maxHeight, Priority vgrow, VPos valignment, boolean fillHeight) {
        this(minHeight, prefHeight, maxHeight);
        setVgrow(vgrow);
        setValignment(valignment);
        setFillHeight(fillHeight);
    }

    public final void setMinHeight(double value) {
        minHeightProperty().set(value);
    }

    public final double getMinHeight() {
        if (this.minHeight == null) {
            return -1.0d;
        }
        return this.minHeight.get();
    }

    public final DoubleProperty minHeightProperty() {
        if (this.minHeight == null) {
            this.minHeight = new DoublePropertyBase(-1.0d) { // from class: javafx.scene.layout.RowConstraints.1
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    RowConstraints.this.requestLayout();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return RowConstraints.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "minHeight";
                }
            };
        }
        return this.minHeight;
    }

    public final void setPrefHeight(double value) {
        prefHeightProperty().set(value);
    }

    public final double getPrefHeight() {
        if (this.prefHeight == null) {
            return -1.0d;
        }
        return this.prefHeight.get();
    }

    public final DoubleProperty prefHeightProperty() {
        if (this.prefHeight == null) {
            this.prefHeight = new DoublePropertyBase(-1.0d) { // from class: javafx.scene.layout.RowConstraints.2
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    RowConstraints.this.requestLayout();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return RowConstraints.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "prefHeight";
                }
            };
        }
        return this.prefHeight;
    }

    public final void setMaxHeight(double value) {
        maxHeightProperty().set(value);
    }

    public final double getMaxHeight() {
        if (this.maxHeight == null) {
            return -1.0d;
        }
        return this.maxHeight.get();
    }

    public final DoubleProperty maxHeightProperty() {
        if (this.maxHeight == null) {
            this.maxHeight = new DoublePropertyBase(-1.0d) { // from class: javafx.scene.layout.RowConstraints.3
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    RowConstraints.this.requestLayout();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return RowConstraints.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "maxHeight";
                }
            };
        }
        return this.maxHeight;
    }

    public final void setPercentHeight(double value) {
        percentHeightProperty().set(value);
    }

    public final double getPercentHeight() {
        if (this.percentHeight == null) {
            return -1.0d;
        }
        return this.percentHeight.get();
    }

    public final DoubleProperty percentHeightProperty() {
        if (this.percentHeight == null) {
            this.percentHeight = new DoublePropertyBase(-1.0d) { // from class: javafx.scene.layout.RowConstraints.4
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    RowConstraints.this.requestLayout();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return RowConstraints.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "percentHeight";
                }
            };
        }
        return this.percentHeight;
    }

    public final void setVgrow(Priority value) {
        vgrowProperty().set(value);
    }

    public final Priority getVgrow() {
        if (this.vgrow == null) {
            return null;
        }
        return this.vgrow.get();
    }

    public final ObjectProperty<Priority> vgrowProperty() {
        if (this.vgrow == null) {
            this.vgrow = new ObjectPropertyBase<Priority>() { // from class: javafx.scene.layout.RowConstraints.5
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    RowConstraints.this.requestLayout();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return RowConstraints.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "vgrow";
                }
            };
        }
        return this.vgrow;
    }

    public final void setValignment(VPos value) {
        valignmentProperty().set(value);
    }

    public final VPos getValignment() {
        if (this.valignment == null) {
            return null;
        }
        return this.valignment.get();
    }

    public final ObjectProperty<VPos> valignmentProperty() {
        if (this.valignment == null) {
            this.valignment = new ObjectPropertyBase<VPos>() { // from class: javafx.scene.layout.RowConstraints.6
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    RowConstraints.this.requestLayout();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return RowConstraints.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "valignment";
                }
            };
        }
        return this.valignment;
    }

    public final void setFillHeight(boolean value) {
        fillHeightProperty().set(value);
    }

    public final boolean isFillHeight() {
        if (this.fillHeight == null) {
            return true;
        }
        return this.fillHeight.get();
    }

    public final BooleanProperty fillHeightProperty() {
        if (this.fillHeight == null) {
            this.fillHeight = new BooleanPropertyBase(true) { // from class: javafx.scene.layout.RowConstraints.7
                @Override // javafx.beans.property.BooleanPropertyBase
                protected void invalidated() {
                    RowConstraints.this.requestLayout();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return RowConstraints.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "fillHeight";
                }
            };
        }
        return this.fillHeight;
    }

    public String toString() {
        return "RowConstraints percentHeight=" + getPercentHeight() + " minHeight=" + getMinHeight() + " prefHeight=" + getPrefHeight() + " maxHeight=" + getMaxHeight() + " vgrow=" + ((Object) getVgrow()) + " fillHeight=" + isFillHeight() + " valignment=" + ((Object) getValignment());
    }
}
