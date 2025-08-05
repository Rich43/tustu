package javafx.scene.control;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.List;
import javafx.beans.NamedArg;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import javax.swing.JOptionPane;

/* loaded from: jfxrt.jar:javafx/scene/control/SpinnerValueFactory.class */
public abstract class SpinnerValueFactory<T> {
    private ObjectProperty<T> value = new SimpleObjectProperty(this, "value");
    private ObjectProperty<StringConverter<T>> converter = new SimpleObjectProperty(this, "converter");
    private BooleanProperty wrapAround;

    public abstract void decrement(int i2);

    public abstract void increment(int i2);

    public final T getValue() {
        return this.value.get();
    }

    public final void setValue(T newValue) {
        this.value.set(newValue);
    }

    public final ObjectProperty<T> valueProperty() {
        return this.value;
    }

    public final StringConverter<T> getConverter() {
        return this.converter.get();
    }

    public final void setConverter(StringConverter<T> newValue) {
        this.converter.set(newValue);
    }

    public final ObjectProperty<StringConverter<T>> converterProperty() {
        return this.converter;
    }

    public final void setWrapAround(boolean value) {
        wrapAroundProperty().set(value);
    }

    public final boolean isWrapAround() {
        if (this.wrapAround == null) {
            return false;
        }
        return this.wrapAround.get();
    }

    public final BooleanProperty wrapAroundProperty() {
        if (this.wrapAround == null) {
            this.wrapAround = new SimpleBooleanProperty(this, "wrapAround", false);
        }
        return this.wrapAround;
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/SpinnerValueFactory$ListSpinnerValueFactory.class */
    public static class ListSpinnerValueFactory<T> extends SpinnerValueFactory<T> {
        private int currentIndex = 0;
        private final ListChangeListener<T> itemsContentObserver = c2 -> {
            updateCurrentIndex();
        };
        private WeakListChangeListener<T> weakItemsContentObserver = new WeakListChangeListener<>(this.itemsContentObserver);
        private ObjectProperty<ObservableList<T>> items;

        public ListSpinnerValueFactory(@NamedArg("items") ObservableList<T> items) {
            setItems(items);
            setConverter(new StringConverter<T>() { // from class: javafx.scene.control.SpinnerValueFactory.ListSpinnerValueFactory.1
                @Override // javafx.util.StringConverter
                public String toString(T value) {
                    if (value == null) {
                        return "";
                    }
                    return value.toString();
                }

                /* JADX WARN: Multi-variable type inference failed */
                @Override // javafx.util.StringConverter
                public T fromString(String str) {
                    return str;
                }
            });
            valueProperty().addListener((o2, oldValue, newValue) -> {
                int newIndex;
                if (items.contains(newValue)) {
                    newIndex = items.indexOf(newValue);
                } else {
                    items.add(newValue);
                    newIndex = items.indexOf(newValue);
                }
                this.currentIndex = newIndex;
            });
            setValue(_getValue(this.currentIndex));
        }

        public final void setItems(ObservableList<T> value) {
            itemsProperty().set(value);
        }

        public final ObservableList<T> getItems() {
            if (this.items == null) {
                return null;
            }
            return this.items.get();
        }

        public final ObjectProperty<ObservableList<T>> itemsProperty() {
            if (this.items == null) {
                this.items = new SimpleObjectProperty<ObservableList<T>>(this, "items") { // from class: javafx.scene.control.SpinnerValueFactory.ListSpinnerValueFactory.2
                    WeakReference<ObservableList<T>> oldItemsRef;

                    @Override // javafx.beans.property.ObjectPropertyBase
                    protected void invalidated() {
                        ObservableList<T> oldItems = this.oldItemsRef == null ? null : this.oldItemsRef.get();
                        ObservableList<T> newItems = ListSpinnerValueFactory.this.getItems();
                        if (oldItems != null) {
                            oldItems.removeListener(ListSpinnerValueFactory.this.weakItemsContentObserver);
                        }
                        if (newItems != null) {
                            newItems.addListener(ListSpinnerValueFactory.this.weakItemsContentObserver);
                        }
                        ListSpinnerValueFactory.this.updateCurrentIndex();
                        this.oldItemsRef = new WeakReference<>(ListSpinnerValueFactory.this.getItems());
                    }
                };
            }
            return this.items;
        }

        @Override // javafx.scene.control.SpinnerValueFactory
        public void decrement(int steps) {
            int max = getItemsSize() - 1;
            int newIndex = this.currentIndex - steps;
            this.currentIndex = newIndex >= 0 ? newIndex : isWrapAround() ? Spinner.wrapValue(newIndex, 0, max + 1) : 0;
            setValue(_getValue(this.currentIndex));
        }

        @Override // javafx.scene.control.SpinnerValueFactory
        public void increment(int steps) {
            int max = getItemsSize() - 1;
            int newIndex = this.currentIndex + steps;
            this.currentIndex = newIndex <= max ? newIndex : isWrapAround() ? Spinner.wrapValue(newIndex, 0, max + 1) : max;
            setValue(_getValue(this.currentIndex));
        }

        private int getItemsSize() {
            List<T> items = getItems();
            if (items == null) {
                return 0;
            }
            return items.size();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateCurrentIndex() {
            int itemsSize = getItemsSize();
            if (this.currentIndex < 0 || this.currentIndex >= itemsSize) {
                this.currentIndex = 0;
            }
            setValue(_getValue(this.currentIndex));
        }

        private T _getValue(int index) {
            List<T> items = getItems();
            if (items != null && index >= 0 && index < items.size()) {
                return items.get(index);
            }
            return null;
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/SpinnerValueFactory$IntegerSpinnerValueFactory.class */
    public static class IntegerSpinnerValueFactory extends SpinnerValueFactory<Integer> {
        private IntegerProperty min;
        private IntegerProperty max;
        private IntegerProperty amountToStepBy;

        public IntegerSpinnerValueFactory(@NamedArg("min") int min, @NamedArg("max") int max) {
            this(min, max, min);
        }

        public IntegerSpinnerValueFactory(@NamedArg("min") int min, @NamedArg("max") int max, @NamedArg(JOptionPane.INITIAL_VALUE_PROPERTY) int initialValue) {
            this(min, max, initialValue, 1);
        }

        public IntegerSpinnerValueFactory(@NamedArg("min") int min, @NamedArg("max") int max, @NamedArg(JOptionPane.INITIAL_VALUE_PROPERTY) int initialValue, @NamedArg("amountToStepBy") int amountToStepBy) {
            this.min = new SimpleIntegerProperty(this, "min") { // from class: javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory.1
                @Override // javafx.beans.property.IntegerPropertyBase
                protected void invalidated() {
                    Integer currentValue = IntegerSpinnerValueFactory.this.getValue();
                    if (currentValue == null) {
                        return;
                    }
                    int newMin = get();
                    if (newMin > IntegerSpinnerValueFactory.this.getMax()) {
                        IntegerSpinnerValueFactory.this.setMin(IntegerSpinnerValueFactory.this.getMax());
                    } else if (currentValue.intValue() < newMin) {
                        IntegerSpinnerValueFactory.this.setValue(Integer.valueOf(newMin));
                    }
                }
            };
            this.max = new SimpleIntegerProperty(this, "max") { // from class: javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory.2
                @Override // javafx.beans.property.IntegerPropertyBase
                protected void invalidated() {
                    Integer currentValue = IntegerSpinnerValueFactory.this.getValue();
                    if (currentValue == null) {
                        return;
                    }
                    int newMax = get();
                    if (newMax < IntegerSpinnerValueFactory.this.getMin()) {
                        IntegerSpinnerValueFactory.this.setMax(IntegerSpinnerValueFactory.this.getMin());
                    } else if (currentValue.intValue() > newMax) {
                        IntegerSpinnerValueFactory.this.setValue(Integer.valueOf(newMax));
                    }
                }
            };
            this.amountToStepBy = new SimpleIntegerProperty(this, "amountToStepBy");
            setMin(min);
            setMax(max);
            setAmountToStepBy(amountToStepBy);
            setConverter(new IntegerStringConverter());
            valueProperty().addListener((o2, oldValue, newValue) -> {
                if (newValue.intValue() < getMin()) {
                    setValue(Integer.valueOf(getMin()));
                } else if (newValue.intValue() > getMax()) {
                    setValue(Integer.valueOf(getMax()));
                }
            });
            setValue(Integer.valueOf((initialValue < min || initialValue > max) ? min : initialValue));
        }

        public final void setMin(int value) {
            this.min.set(value);
        }

        public final int getMin() {
            return this.min.get();
        }

        public final IntegerProperty minProperty() {
            return this.min;
        }

        public final void setMax(int value) {
            this.max.set(value);
        }

        public final int getMax() {
            return this.max.get();
        }

        public final IntegerProperty maxProperty() {
            return this.max;
        }

        public final void setAmountToStepBy(int value) {
            this.amountToStepBy.set(value);
        }

        public final int getAmountToStepBy() {
            return this.amountToStepBy.get();
        }

        public final IntegerProperty amountToStepByProperty() {
            return this.amountToStepBy;
        }

        @Override // javafx.scene.control.SpinnerValueFactory
        public void decrement(int steps) {
            int min = getMin();
            int max = getMax();
            int newIndex = ((Integer) getValue()).intValue() - (steps * getAmountToStepBy());
            setValue(Integer.valueOf(newIndex >= min ? newIndex : isWrapAround() ? Spinner.wrapValue(newIndex, min, max) + 1 : min));
        }

        @Override // javafx.scene.control.SpinnerValueFactory
        public void increment(int steps) {
            int min = getMin();
            int max = getMax();
            int currentValue = ((Integer) getValue()).intValue();
            int newIndex = currentValue + (steps * getAmountToStepBy());
            setValue(Integer.valueOf(newIndex <= max ? newIndex : isWrapAround() ? Spinner.wrapValue(newIndex, min, max) - 1 : max));
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/SpinnerValueFactory$DoubleSpinnerValueFactory.class */
    public static class DoubleSpinnerValueFactory extends SpinnerValueFactory<Double> {
        private DoubleProperty min;
        private DoubleProperty max;
        private DoubleProperty amountToStepBy;

        public DoubleSpinnerValueFactory(@NamedArg("min") double min, @NamedArg("max") double max) {
            this(min, max, min);
        }

        public DoubleSpinnerValueFactory(@NamedArg("min") double min, @NamedArg("max") double max, @NamedArg(JOptionPane.INITIAL_VALUE_PROPERTY) double initialValue) {
            this(min, max, initialValue, 1.0d);
        }

        public DoubleSpinnerValueFactory(@NamedArg("min") double min, @NamedArg("max") double max, @NamedArg(JOptionPane.INITIAL_VALUE_PROPERTY) double initialValue, @NamedArg("amountToStepBy") double amountToStepBy) {
            this.min = new SimpleDoubleProperty(this, "min") { // from class: javafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory.2
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    Double currentValue = DoubleSpinnerValueFactory.this.getValue();
                    if (currentValue == null) {
                        return;
                    }
                    double newMin = get();
                    if (newMin > DoubleSpinnerValueFactory.this.getMax()) {
                        DoubleSpinnerValueFactory.this.setMin(DoubleSpinnerValueFactory.this.getMax());
                    } else if (currentValue.doubleValue() < newMin) {
                        DoubleSpinnerValueFactory.this.setValue(Double.valueOf(newMin));
                    }
                }
            };
            this.max = new SimpleDoubleProperty(this, "max") { // from class: javafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory.3
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    Double currentValue = DoubleSpinnerValueFactory.this.getValue();
                    if (currentValue == null) {
                        return;
                    }
                    double newMax = get();
                    if (newMax < DoubleSpinnerValueFactory.this.getMin()) {
                        DoubleSpinnerValueFactory.this.setMax(DoubleSpinnerValueFactory.this.getMin());
                    } else if (currentValue.doubleValue() > newMax) {
                        DoubleSpinnerValueFactory.this.setValue(Double.valueOf(newMax));
                    }
                }
            };
            this.amountToStepBy = new SimpleDoubleProperty(this, "amountToStepBy");
            setMin(min);
            setMax(max);
            setAmountToStepBy(amountToStepBy);
            setConverter(new StringConverter<Double>() { // from class: javafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory.1
                private final DecimalFormat df = new DecimalFormat("#.##");

                @Override // javafx.util.StringConverter
                public String toString(Double value) {
                    if (value == null) {
                        return "";
                    }
                    return this.df.format(value);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // javafx.util.StringConverter
                public Double fromString(String value) {
                    if (value == null) {
                        return null;
                    }
                    try {
                        String value2 = value.trim();
                        if (value2.length() < 1) {
                            return null;
                        }
                        return Double.valueOf(this.df.parse(value2).doubleValue());
                    } catch (ParseException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
            valueProperty().addListener((o2, oldValue, newValue) -> {
                if (newValue.doubleValue() < getMin()) {
                    setValue(Double.valueOf(getMin()));
                } else if (newValue.doubleValue() > getMax()) {
                    setValue(Double.valueOf(getMax()));
                }
            });
            setValue(Double.valueOf((initialValue < min || initialValue > max) ? min : initialValue));
        }

        public final void setMin(double value) {
            this.min.set(value);
        }

        public final double getMin() {
            return this.min.get();
        }

        public final DoubleProperty minProperty() {
            return this.min;
        }

        public final void setMax(double value) {
            this.max.set(value);
        }

        public final double getMax() {
            return this.max.get();
        }

        public final DoubleProperty maxProperty() {
            return this.max;
        }

        public final void setAmountToStepBy(double value) {
            this.amountToStepBy.set(value);
        }

        public final double getAmountToStepBy() {
            return this.amountToStepBy.get();
        }

        public final DoubleProperty amountToStepByProperty() {
            return this.amountToStepBy;
        }

        @Override // javafx.scene.control.SpinnerValueFactory
        public void decrement(int steps) {
            double dDoubleValue;
            BigDecimal currentValue = BigDecimal.valueOf(((Double) getValue()).doubleValue());
            BigDecimal minBigDecimal = BigDecimal.valueOf(getMin());
            BigDecimal maxBigDecimal = BigDecimal.valueOf(getMax());
            BigDecimal amountToStepByBigDecimal = BigDecimal.valueOf(getAmountToStepBy());
            BigDecimal newValue = currentValue.subtract(amountToStepByBigDecimal.multiply(BigDecimal.valueOf(steps)));
            if (newValue.compareTo(minBigDecimal) >= 0) {
                dDoubleValue = newValue.doubleValue();
            } else {
                dDoubleValue = isWrapAround() ? Spinner.wrapValue(newValue, minBigDecimal, maxBigDecimal).doubleValue() : getMin();
            }
            setValue(Double.valueOf(dDoubleValue));
        }

        @Override // javafx.scene.control.SpinnerValueFactory
        public void increment(int steps) {
            double dDoubleValue;
            BigDecimal currentValue = BigDecimal.valueOf(((Double) getValue()).doubleValue());
            BigDecimal minBigDecimal = BigDecimal.valueOf(getMin());
            BigDecimal maxBigDecimal = BigDecimal.valueOf(getMax());
            BigDecimal amountToStepByBigDecimal = BigDecimal.valueOf(getAmountToStepBy());
            BigDecimal newValue = currentValue.add(amountToStepByBigDecimal.multiply(BigDecimal.valueOf(steps)));
            if (newValue.compareTo(maxBigDecimal) <= 0) {
                dDoubleValue = newValue.doubleValue();
            } else {
                dDoubleValue = isWrapAround() ? Spinner.wrapValue(newValue, minBigDecimal, maxBigDecimal).doubleValue() : getMax();
            }
            setValue(Double.valueOf(dDoubleValue));
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/SpinnerValueFactory$LocalDateSpinnerValueFactory.class */
    static class LocalDateSpinnerValueFactory extends SpinnerValueFactory<LocalDate> {
        private ObjectProperty<LocalDate> min;
        private ObjectProperty<LocalDate> max;
        private ObjectProperty<TemporalUnit> temporalUnit;
        private LongProperty amountToStepBy;

        public LocalDateSpinnerValueFactory() {
            this(LocalDate.now());
        }

        public LocalDateSpinnerValueFactory(@NamedArg(JOptionPane.INITIAL_VALUE_PROPERTY) LocalDate initialValue) {
            this(LocalDate.MIN, LocalDate.MAX, initialValue);
        }

        public LocalDateSpinnerValueFactory(@NamedArg("min") LocalDate min, @NamedArg("min") LocalDate max, @NamedArg(JOptionPane.INITIAL_VALUE_PROPERTY) LocalDate initialValue) {
            this(min, max, initialValue, 1L, ChronoUnit.DAYS);
        }

        public LocalDateSpinnerValueFactory(@NamedArg("min") LocalDate min, @NamedArg("min") LocalDate max, @NamedArg(JOptionPane.INITIAL_VALUE_PROPERTY) LocalDate initialValue, @NamedArg("amountToStepBy") long amountToStepBy, @NamedArg("temporalUnit") TemporalUnit temporalUnit) {
            this.min = new SimpleObjectProperty<LocalDate>(this, "min") { // from class: javafx.scene.control.SpinnerValueFactory.LocalDateSpinnerValueFactory.2
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    LocalDate currentValue = LocalDateSpinnerValueFactory.this.getValue();
                    if (currentValue == null) {
                        return;
                    }
                    LocalDate newMin = get();
                    if (newMin.isAfter(LocalDateSpinnerValueFactory.this.getMax())) {
                        LocalDateSpinnerValueFactory.this.setMin(LocalDateSpinnerValueFactory.this.getMax());
                    } else if (currentValue.isBefore(newMin)) {
                        LocalDateSpinnerValueFactory.this.setValue(newMin);
                    }
                }
            };
            this.max = new SimpleObjectProperty<LocalDate>(this, "max") { // from class: javafx.scene.control.SpinnerValueFactory.LocalDateSpinnerValueFactory.3
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    LocalDate currentValue = LocalDateSpinnerValueFactory.this.getValue();
                    if (currentValue == null) {
                        return;
                    }
                    LocalDate newMax = get();
                    if (newMax.isBefore(LocalDateSpinnerValueFactory.this.getMin())) {
                        LocalDateSpinnerValueFactory.this.setMax(LocalDateSpinnerValueFactory.this.getMin());
                    } else if (currentValue.isAfter(newMax)) {
                        LocalDateSpinnerValueFactory.this.setValue(newMax);
                    }
                }
            };
            this.temporalUnit = new SimpleObjectProperty(this, "temporalUnit");
            this.amountToStepBy = new SimpleLongProperty(this, "amountToStepBy");
            setMin(min);
            setMax(max);
            setAmountToStepBy(amountToStepBy);
            setTemporalUnit(temporalUnit);
            setConverter(new StringConverter<LocalDate>() { // from class: javafx.scene.control.SpinnerValueFactory.LocalDateSpinnerValueFactory.1
                @Override // javafx.util.StringConverter
                public String toString(LocalDate object) {
                    if (object == null) {
                        return "";
                    }
                    return object.toString();
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // javafx.util.StringConverter
                public LocalDate fromString(String string) {
                    return LocalDate.parse(string);
                }
            });
            valueProperty().addListener((o2, oldValue, newValue) -> {
                if (getMin() != null && newValue.isBefore(getMin())) {
                    setValue(getMin());
                } else if (getMax() != null && newValue.isAfter(getMax())) {
                    setValue(getMax());
                }
            });
            setValue(initialValue != null ? initialValue : LocalDate.now());
        }

        public final void setMin(LocalDate value) {
            this.min.set(value);
        }

        public final LocalDate getMin() {
            return this.min.get();
        }

        public final ObjectProperty<LocalDate> minProperty() {
            return this.min;
        }

        public final void setMax(LocalDate value) {
            this.max.set(value);
        }

        public final LocalDate getMax() {
            return this.max.get();
        }

        public final ObjectProperty<LocalDate> maxProperty() {
            return this.max;
        }

        public final void setTemporalUnit(TemporalUnit value) {
            this.temporalUnit.set(value);
        }

        public final TemporalUnit getTemporalUnit() {
            return this.temporalUnit.get();
        }

        public final ObjectProperty<TemporalUnit> temporalUnitProperty() {
            return this.temporalUnit;
        }

        public final void setAmountToStepBy(long value) {
            this.amountToStepBy.set(value);
        }

        public final long getAmountToStepBy() {
            return this.amountToStepBy.get();
        }

        public final LongProperty amountToStepByProperty() {
            return this.amountToStepBy;
        }

        @Override // javafx.scene.control.SpinnerValueFactory
        public void decrement(int steps) {
            LocalDate currentValue = getValue();
            LocalDate min = getMin();
            LocalDate newValue = currentValue.minus(getAmountToStepBy() * steps, getTemporalUnit());
            if (min != null && isWrapAround() && newValue.isBefore(min)) {
                newValue = getMax();
            }
            setValue(newValue);
        }

        @Override // javafx.scene.control.SpinnerValueFactory
        public void increment(int steps) {
            LocalDate currentValue = getValue();
            LocalDate max = getMax();
            LocalDate newValue = currentValue.plus(getAmountToStepBy() * steps, getTemporalUnit());
            if (max != null && isWrapAround() && newValue.isAfter(max)) {
                newValue = getMin();
            }
            setValue(newValue);
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/SpinnerValueFactory$LocalTimeSpinnerValueFactory.class */
    static class LocalTimeSpinnerValueFactory extends SpinnerValueFactory<LocalTime> {
        private ObjectProperty<LocalTime> min;
        private ObjectProperty<LocalTime> max;
        private ObjectProperty<TemporalUnit> temporalUnit;
        private LongProperty amountToStepBy;

        public LocalTimeSpinnerValueFactory() {
            this(LocalTime.now());
        }

        public LocalTimeSpinnerValueFactory(@NamedArg(JOptionPane.INITIAL_VALUE_PROPERTY) LocalTime initialValue) {
            this(LocalTime.MIN, LocalTime.MAX, initialValue);
        }

        public LocalTimeSpinnerValueFactory(@NamedArg("min") LocalTime min, @NamedArg("min") LocalTime max, @NamedArg(JOptionPane.INITIAL_VALUE_PROPERTY) LocalTime initialValue) {
            this(min, max, initialValue, 1L, ChronoUnit.HOURS);
        }

        public LocalTimeSpinnerValueFactory(@NamedArg("min") LocalTime min, @NamedArg("min") LocalTime max, @NamedArg(JOptionPane.INITIAL_VALUE_PROPERTY) LocalTime initialValue, @NamedArg("amountToStepBy") long amountToStepBy, @NamedArg("temporalUnit") TemporalUnit temporalUnit) {
            this.min = new SimpleObjectProperty<LocalTime>(this, "min") { // from class: javafx.scene.control.SpinnerValueFactory.LocalTimeSpinnerValueFactory.2
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    LocalTime currentValue = LocalTimeSpinnerValueFactory.this.getValue();
                    if (currentValue == null) {
                        return;
                    }
                    LocalTime newMin = get();
                    if (newMin.isAfter(LocalTimeSpinnerValueFactory.this.getMax())) {
                        LocalTimeSpinnerValueFactory.this.setMin(LocalTimeSpinnerValueFactory.this.getMax());
                    } else if (currentValue.isBefore(newMin)) {
                        LocalTimeSpinnerValueFactory.this.setValue(newMin);
                    }
                }
            };
            this.max = new SimpleObjectProperty<LocalTime>(this, "max") { // from class: javafx.scene.control.SpinnerValueFactory.LocalTimeSpinnerValueFactory.3
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    LocalTime currentValue = LocalTimeSpinnerValueFactory.this.getValue();
                    if (currentValue == null) {
                        return;
                    }
                    LocalTime newMax = get();
                    if (newMax.isBefore(LocalTimeSpinnerValueFactory.this.getMin())) {
                        LocalTimeSpinnerValueFactory.this.setMax(LocalTimeSpinnerValueFactory.this.getMin());
                    } else if (currentValue.isAfter(newMax)) {
                        LocalTimeSpinnerValueFactory.this.setValue(newMax);
                    }
                }
            };
            this.temporalUnit = new SimpleObjectProperty(this, "temporalUnit");
            this.amountToStepBy = new SimpleLongProperty(this, "amountToStepBy");
            setMin(min);
            setMax(max);
            setAmountToStepBy(amountToStepBy);
            setTemporalUnit(temporalUnit);
            setConverter(new StringConverter<LocalTime>() { // from class: javafx.scene.control.SpinnerValueFactory.LocalTimeSpinnerValueFactory.1
                private DateTimeFormatter dtf = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);

                @Override // javafx.util.StringConverter
                public String toString(LocalTime localTime) {
                    if (localTime == null) {
                        return "";
                    }
                    return localTime.format(this.dtf);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // javafx.util.StringConverter
                public LocalTime fromString(String string) {
                    return LocalTime.parse(string);
                }
            });
            valueProperty().addListener((o2, oldValue, newValue) -> {
                if (getMin() != null && newValue.isBefore(getMin())) {
                    setValue(getMin());
                } else if (getMax() != null && newValue.isAfter(getMax())) {
                    setValue(getMax());
                }
            });
            setValue(initialValue != null ? initialValue : LocalTime.now());
        }

        public final void setMin(LocalTime value) {
            this.min.set(value);
        }

        public final LocalTime getMin() {
            return this.min.get();
        }

        public final ObjectProperty<LocalTime> minProperty() {
            return this.min;
        }

        public final void setMax(LocalTime value) {
            this.max.set(value);
        }

        public final LocalTime getMax() {
            return this.max.get();
        }

        public final ObjectProperty<LocalTime> maxProperty() {
            return this.max;
        }

        public final void setTemporalUnit(TemporalUnit value) {
            this.temporalUnit.set(value);
        }

        public final TemporalUnit getTemporalUnit() {
            return this.temporalUnit.get();
        }

        public final ObjectProperty<TemporalUnit> temporalUnitProperty() {
            return this.temporalUnit;
        }

        public final void setAmountToStepBy(long value) {
            this.amountToStepBy.set(value);
        }

        public final long getAmountToStepBy() {
            return this.amountToStepBy.get();
        }

        public final LongProperty amountToStepByProperty() {
            return this.amountToStepBy;
        }

        @Override // javafx.scene.control.SpinnerValueFactory
        public void decrement(int steps) {
            LocalTime currentValue = getValue();
            LocalTime min = getMin();
            Duration duration = Duration.of(getAmountToStepBy() * steps, getTemporalUnit());
            long durationInSeconds = duration.toMinutes() * 60;
            long currentValueInSeconds = currentValue.toSecondOfDay();
            if (!isWrapAround() && durationInSeconds > currentValueInSeconds) {
                setValue(min == null ? LocalTime.MIN : min);
            } else {
                setValue(currentValue.minus((TemporalAmount) duration));
            }
        }

        @Override // javafx.scene.control.SpinnerValueFactory
        public void increment(int steps) {
            LocalTime currentValue = getValue();
            LocalTime max = getMax();
            Duration duration = Duration.of(getAmountToStepBy() * steps, getTemporalUnit());
            long durationInSeconds = duration.toMinutes() * 60;
            long currentValueInSeconds = currentValue.toSecondOfDay();
            if (!isWrapAround() && durationInSeconds > LocalTime.MAX.toSecondOfDay() - currentValueInSeconds) {
                setValue(max == null ? LocalTime.MAX : max);
            } else {
                setValue(currentValue.plus((TemporalAmount) duration));
            }
        }
    }
}
