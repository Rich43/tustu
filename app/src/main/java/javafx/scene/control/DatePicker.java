package javafx.scene.control;

import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.scene.control.skin.ComboBoxPopupControl;
import com.sun.javafx.scene.control.skin.DatePickerSkin;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.LocalDateStringConverter;

/* loaded from: jfxrt.jar:javafx/scene/control/DatePicker.class */
public class DatePicker extends ComboBoxBase<LocalDate> {
    private LocalDate lastValidDate;
    private Chronology lastValidChronology;
    private ObjectProperty<Callback<DatePicker, DateCell>> dayCellFactory;
    private ObjectProperty<Chronology> chronology;
    private BooleanProperty showWeekNumbers;
    private ObjectProperty<StringConverter<LocalDate>> converter;
    private StringConverter<LocalDate> defaultConverter;
    private ReadOnlyObjectWrapper<TextField> editor;
    private static final String DEFAULT_STYLE_CLASS = "date-picker";

    public DatePicker() {
        this(null);
        valueProperty().addListener(observable -> {
            LocalDate date = getValue();
            Chronology chrono = getChronology();
            if (validateDate(chrono, date)) {
                this.lastValidDate = date;
            } else {
                System.err.println("Restoring value to " + (this.lastValidDate == null ? FXMLLoader.NULL_KEYWORD : getConverter().toString(this.lastValidDate)));
                setValue(this.lastValidDate);
            }
        });
        chronologyProperty().addListener(observable2 -> {
            LocalDate date = getValue();
            Chronology chrono = getChronology();
            if (validateDate(chrono, date)) {
                this.lastValidChronology = chrono;
                this.defaultConverter = new LocalDateStringConverter(FormatStyle.SHORT, null, chrono);
            } else {
                System.err.println("Restoring value to " + ((Object) this.lastValidChronology));
                setChronology(this.lastValidChronology);
            }
        });
    }

    private boolean validateDate(Chronology chrono, LocalDate date) {
        if (date != null) {
            try {
                chrono.date(date);
                return true;
            } catch (DateTimeException ex) {
                System.err.println(ex);
                return false;
            }
        }
        return true;
    }

    public DatePicker(LocalDate localDate) {
        this.lastValidDate = null;
        this.lastValidChronology = IsoChronology.INSTANCE;
        this.chronology = new SimpleObjectProperty(this, "chronology", null);
        this.converter = new SimpleObjectProperty(this, "converter", null);
        this.defaultConverter = new LocalDateStringConverter(FormatStyle.SHORT, null, getChronology());
        setValue(localDate);
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        setAccessibleRole(AccessibleRole.DATE_PICKER);
        setEditable(true);
    }

    public final void setDayCellFactory(Callback<DatePicker, DateCell> value) {
        dayCellFactoryProperty().set(value);
    }

    public final Callback<DatePicker, DateCell> getDayCellFactory() {
        if (this.dayCellFactory != null) {
            return this.dayCellFactory.get();
        }
        return null;
    }

    public final ObjectProperty<Callback<DatePicker, DateCell>> dayCellFactoryProperty() {
        if (this.dayCellFactory == null) {
            this.dayCellFactory = new SimpleObjectProperty(this, "dayCellFactory");
        }
        return this.dayCellFactory;
    }

    public final ObjectProperty<Chronology> chronologyProperty() {
        return this.chronology;
    }

    public final Chronology getChronology() {
        Chronology chrono = this.chronology.get();
        if (chrono == null) {
            try {
                chrono = Chronology.ofLocale(Locale.getDefault(Locale.Category.FORMAT));
            } catch (Exception ex) {
                System.err.println(ex);
            }
            if (chrono == null) {
                chrono = IsoChronology.INSTANCE;
            }
        }
        return chrono;
    }

    public final void setChronology(Chronology value) {
        this.chronology.setValue(value);
    }

    public final BooleanProperty showWeekNumbersProperty() {
        if (this.showWeekNumbers == null) {
            String country = Locale.getDefault(Locale.Category.FORMAT).getCountry();
            boolean localizedDefault = !country.isEmpty() && ControlResources.getNonTranslatableString("DatePicker.showWeekNumbers").contains(country);
            this.showWeekNumbers = new StyleableBooleanProperty(localizedDefault) { // from class: javafx.scene.control.DatePicker.1
                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
                    return StyleableProperties.SHOW_WEEK_NUMBERS;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return DatePicker.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "showWeekNumbers";
                }
            };
        }
        return this.showWeekNumbers;
    }

    public final void setShowWeekNumbers(boolean value) {
        showWeekNumbersProperty().setValue(Boolean.valueOf(value));
    }

    public final boolean isShowWeekNumbers() {
        return showWeekNumbersProperty().getValue2().booleanValue();
    }

    public final ObjectProperty<StringConverter<LocalDate>> converterProperty() {
        return this.converter;
    }

    public final void setConverter(StringConverter<LocalDate> value) {
        converterProperty().set(value);
    }

    public final StringConverter<LocalDate> getConverter() {
        StringConverter<LocalDate> converter = converterProperty().get();
        if (converter != null) {
            return converter;
        }
        return this.defaultConverter;
    }

    public final TextField getEditor() {
        return editorProperty().get();
    }

    public final ReadOnlyObjectProperty<TextField> editorProperty() {
        if (this.editor == null) {
            this.editor = new ReadOnlyObjectWrapper<>(this, "editor");
            this.editor.set(new ComboBoxPopupControl.FakeFocusTextField());
        }
        return this.editor.getReadOnlyProperty();
    }

    @Override // javafx.scene.control.Control
    protected Skin<?> createDefaultSkin() {
        return new DatePickerSkin(this);
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/DatePicker$StyleableProperties.class */
    private static class StyleableProperties {
        private static final String country = Locale.getDefault(Locale.Category.FORMAT).getCountry();
        private static final CssMetaData<DatePicker, Boolean> SHOW_WEEK_NUMBERS;
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            SHOW_WEEK_NUMBERS = new CssMetaData<DatePicker, Boolean>("-fx-show-week-numbers", BooleanConverter.getInstance(), Boolean.valueOf(!country.isEmpty() && ControlResources.getNonTranslatableString("DatePicker.showWeekNumbers").contains(country))) { // from class: javafx.scene.control.DatePicker.StyleableProperties.1
                @Override // javafx.css.CssMetaData
                public boolean isSettable(DatePicker n2) {
                    return n2.showWeekNumbers == null || !n2.showWeekNumbers.isBound();
                }

                @Override // javafx.css.CssMetaData
                public StyleableProperty<Boolean> getStyleableProperty(DatePicker n2) {
                    return (StyleableProperty) n2.showWeekNumbersProperty();
                }
            };
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Control.getClassCssMetaData());
            Collections.addAll(styleables, SHOW_WEEK_NUMBERS);
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

    @Override // javafx.scene.control.ComboBoxBase, javafx.scene.control.Control, javafx.scene.Parent, javafx.scene.Node
    public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case DATE:
                return getValue();
            case TEXT:
                String accText = getAccessibleText();
                if (accText != null && !accText.isEmpty()) {
                    return accText;
                }
                LocalDate date = getValue();
                StringConverter<LocalDate> c2 = getConverter();
                if (date != null && c2 != null) {
                    return c2.toString(date);
                }
                return "";
            default:
                return super.queryAccessibleAttribute(attribute, parameters);
        }
    }
}
