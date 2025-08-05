package javafx.scene.control;

import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.skin.PaginationSkin;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.DefaultProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableIntegerProperty;
import javafx.css.StyleableProperty;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.util.Callback;

@DefaultProperty("pages")
/* loaded from: jfxrt.jar:javafx/scene/control/Pagination.class */
public class Pagination extends Control {
    private static final int DEFAULT_MAX_PAGE_INDICATOR_COUNT = 10;
    public static final String STYLE_CLASS_BULLET = "bullet";
    public static final int INDETERMINATE = Integer.MAX_VALUE;
    private int oldMaxPageIndicatorCount;
    private IntegerProperty maxPageIndicatorCount;
    private int oldPageCount;
    private IntegerProperty pageCount;
    private final IntegerProperty currentPageIndex;
    private ObjectProperty<Callback<Integer, Node>> pageFactory;
    private static final String DEFAULT_STYLE_CLASS = "pagination";

    public Pagination(int pageCount, int pageIndex) {
        this.oldMaxPageIndicatorCount = 10;
        this.oldPageCount = Integer.MAX_VALUE;
        this.pageCount = new SimpleIntegerProperty(this, "pageCount", Integer.MAX_VALUE) { // from class: javafx.scene.control.Pagination.2
            @Override // javafx.beans.property.IntegerPropertyBase
            protected void invalidated() {
                if (!Pagination.this.pageCount.isBound()) {
                    if (Pagination.this.getPageCount() < 1) {
                        Pagination.this.setPageCount(Pagination.this.oldPageCount);
                    }
                    Pagination.this.oldPageCount = Pagination.this.getPageCount();
                }
            }
        };
        this.currentPageIndex = new SimpleIntegerProperty(this, "currentPageIndex", 0) { // from class: javafx.scene.control.Pagination.3
            @Override // javafx.beans.property.IntegerPropertyBase
            protected void invalidated() {
                if (!Pagination.this.currentPageIndex.isBound()) {
                    if (Pagination.this.getCurrentPageIndex() < 0) {
                        Pagination.this.setCurrentPageIndex(0);
                    } else if (Pagination.this.getCurrentPageIndex() > Pagination.this.getPageCount() - 1) {
                        Pagination.this.setCurrentPageIndex(Pagination.this.getPageCount() - 1);
                    }
                }
            }

            @Override // javafx.beans.property.IntegerPropertyBase, javafx.beans.property.Property
            public void bind(ObservableValue<? extends Number> rawObservable) {
                throw new UnsupportedOperationException("currentPageIndex supports only bidirectional binding");
            }
        };
        this.pageFactory = new SimpleObjectProperty(this, "pageFactory");
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        setAccessibleRole(AccessibleRole.PAGINATION);
        setPageCount(pageCount);
        setCurrentPageIndex(pageIndex);
    }

    public Pagination(int pageCount) {
        this(pageCount, 0);
    }

    public Pagination() {
        this(Integer.MAX_VALUE, 0);
    }

    public final void setMaxPageIndicatorCount(int value) {
        maxPageIndicatorCountProperty().set(value);
    }

    public final int getMaxPageIndicatorCount() {
        if (this.maxPageIndicatorCount == null) {
            return 10;
        }
        return this.maxPageIndicatorCount.get();
    }

    public final IntegerProperty maxPageIndicatorCountProperty() {
        if (this.maxPageIndicatorCount == null) {
            this.maxPageIndicatorCount = new StyleableIntegerProperty(10) { // from class: javafx.scene.control.Pagination.1
                @Override // javafx.beans.property.IntegerPropertyBase
                protected void invalidated() {
                    if (!Pagination.this.maxPageIndicatorCount.isBound()) {
                        if (Pagination.this.getMaxPageIndicatorCount() < 1 || Pagination.this.getMaxPageIndicatorCount() > Pagination.this.getPageCount()) {
                            Pagination.this.setMaxPageIndicatorCount(Pagination.this.oldMaxPageIndicatorCount);
                        }
                        Pagination.this.oldMaxPageIndicatorCount = Pagination.this.getMaxPageIndicatorCount();
                    }
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.MAX_PAGE_INDICATOR_COUNT;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Pagination.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "maxPageIndicatorCount";
                }
            };
        }
        return this.maxPageIndicatorCount;
    }

    public final void setPageCount(int value) {
        this.pageCount.set(value);
    }

    public final int getPageCount() {
        return this.pageCount.get();
    }

    public final IntegerProperty pageCountProperty() {
        return this.pageCount;
    }

    public final void setCurrentPageIndex(int value) {
        this.currentPageIndex.set(value);
    }

    public final int getCurrentPageIndex() {
        return this.currentPageIndex.get();
    }

    public final IntegerProperty currentPageIndexProperty() {
        return this.currentPageIndex;
    }

    public final void setPageFactory(Callback<Integer, Node> value) {
        this.pageFactory.set(value);
    }

    public final Callback<Integer, Node> getPageFactory() {
        return this.pageFactory.get();
    }

    public final ObjectProperty<Callback<Integer, Node>> pageFactoryProperty() {
        return this.pageFactory;
    }

    @Override // javafx.scene.control.Control
    protected Skin<?> createDefaultSkin() {
        return new PaginationSkin(this);
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/Pagination$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<Pagination, Number> MAX_PAGE_INDICATOR_COUNT = new CssMetaData<Pagination, Number>("-fx-max-page-indicator-count", SizeConverter.getInstance(), 10) { // from class: javafx.scene.control.Pagination.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Pagination n2) {
                return n2.maxPageIndicatorCount == null || !n2.maxPageIndicatorCount.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(Pagination n2) {
                return (StyleableProperty) n2.maxPageIndicatorCountProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Control.getClassCssMetaData());
            styleables.add(MAX_PAGE_INDICATOR_COUNT);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override // javafx.scene.control.Control
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }
}
