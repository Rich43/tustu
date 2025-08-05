package com.sun.javafx.binding;

import java.lang.ref.WeakReference;
import java.text.Format;
import java.text.ParseException;
import javafx.beans.Observable;
import javafx.beans.WeakListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.util.StringConverter;

/* loaded from: jfxrt.jar:com/sun/javafx/binding/BidirectionalBinding.class */
public abstract class BidirectionalBinding<T> implements ChangeListener<T>, WeakListener {
    private final int cachedHashCode;

    protected abstract Object getProperty1();

    protected abstract Object getProperty2();

    private static void checkParameters(Object property1, Object property2) {
        if (property1 == null || property2 == null) {
            throw new NullPointerException("Both properties must be specified.");
        }
        if (property1 == property2) {
            throw new IllegalArgumentException("Cannot bind property to itself");
        }
    }

    public static <T> BidirectionalBinding bind(Property<T> property, Property<T> property2) {
        checkParameters(property, property2);
        BidirectionalBinding binding = ((property instanceof DoubleProperty) && (property2 instanceof DoubleProperty)) ? new BidirectionalDoubleBinding((DoubleProperty) property, (DoubleProperty) property2) : ((property instanceof FloatProperty) && (property2 instanceof FloatProperty)) ? new BidirectionalFloatBinding((FloatProperty) property, (FloatProperty) property2) : ((property instanceof IntegerProperty) && (property2 instanceof IntegerProperty)) ? new BidirectionalIntegerBinding((IntegerProperty) property, (IntegerProperty) property2) : ((property instanceof LongProperty) && (property2 instanceof LongProperty)) ? new BidirectionalLongBinding((LongProperty) property, (LongProperty) property2) : ((property instanceof BooleanProperty) && (property2 instanceof BooleanProperty)) ? new BidirectionalBooleanBinding((BooleanProperty) property, (BooleanProperty) property2) : new TypedGenericBidirectionalBinding(property, property2);
        property.setValue(property2.getValue2());
        property.addListener(binding);
        property2.addListener(binding);
        return binding;
    }

    public static Object bind(Property<String> stringProperty, Property<?> otherProperty, Format format) {
        checkParameters(stringProperty, otherProperty);
        if (format == null) {
            throw new NullPointerException("Format cannot be null");
        }
        StringConversionBidirectionalBinding<?> binding = new StringFormatBidirectionalBinding(stringProperty, otherProperty, format);
        stringProperty.setValue(format.format(otherProperty.getValue2()));
        stringProperty.addListener(binding);
        otherProperty.addListener(binding);
        return binding;
    }

    public static <T> Object bind(Property<String> stringProperty, Property<T> otherProperty, StringConverter<T> converter) {
        checkParameters(stringProperty, otherProperty);
        if (converter == null) {
            throw new NullPointerException("Converter cannot be null");
        }
        StringConversionBidirectionalBinding<T> binding = new StringConverterBidirectionalBinding<>(stringProperty, otherProperty, converter);
        stringProperty.setValue(converter.toString(otherProperty.getValue2()));
        stringProperty.addListener(binding);
        otherProperty.addListener(binding);
        return binding;
    }

    public static <T> void unbind(Property<T> property1, Property<T> property2) {
        checkParameters(property1, property2);
        BidirectionalBinding binding = new UntypedGenericBidirectionalBinding(property1, property2);
        property1.removeListener(binding);
        property2.removeListener(binding);
    }

    public static void unbind(Object property1, Object property2) {
        checkParameters(property1, property2);
        UntypedGenericBidirectionalBinding untypedGenericBidirectionalBinding = new UntypedGenericBidirectionalBinding(property1, property2);
        if (property1 instanceof ObservableValue) {
            ((ObservableValue) property1).removeListener(untypedGenericBidirectionalBinding);
        }
        if (property2 instanceof Observable) {
            ((ObservableValue) property2).removeListener(untypedGenericBidirectionalBinding);
        }
    }

    public static BidirectionalBinding bindNumber(Property<Integer> property1, IntegerProperty property2) {
        return bindNumber(property1, (Property<Number>) property2);
    }

    public static BidirectionalBinding bindNumber(Property<Long> property1, LongProperty property2) {
        return bindNumber(property1, (Property<Number>) property2);
    }

    public static BidirectionalBinding bindNumber(Property<Float> property1, FloatProperty property2) {
        return bindNumber(property1, (Property<Number>) property2);
    }

    public static BidirectionalBinding bindNumber(Property<Double> property1, DoubleProperty property2) {
        return bindNumber(property1, (Property<Number>) property2);
    }

    public static BidirectionalBinding bindNumber(IntegerProperty property1, Property<Integer> property2) {
        return bindNumberObject(property1, property2);
    }

    public static BidirectionalBinding bindNumber(LongProperty property1, Property<Long> property2) {
        return bindNumberObject(property1, property2);
    }

    public static BidirectionalBinding bindNumber(FloatProperty property1, Property<Float> property2) {
        return bindNumberObject(property1, property2);
    }

    public static BidirectionalBinding bindNumber(DoubleProperty property1, Property<Double> property2) {
        return bindNumberObject(property1, property2);
    }

    private static <T extends Number> BidirectionalBinding bindNumberObject(Property<Number> property, Property<T> property2) {
        checkParameters(property, property2);
        TypedNumberBidirectionalBinding typedNumberBidirectionalBinding = new TypedNumberBidirectionalBinding(property2, property);
        property.setValue(property2.getValue2());
        property.addListener(typedNumberBidirectionalBinding);
        property2.addListener(typedNumberBidirectionalBinding);
        return typedNumberBidirectionalBinding;
    }

    private static <T extends Number> BidirectionalBinding bindNumber(Property<T> property1, Property<Number> property2) {
        checkParameters(property1, property2);
        BidirectionalBinding<Number> binding = new TypedNumberBidirectionalBinding<>(property1, property2);
        property1.setValue(property2.getValue2());
        property1.addListener(binding);
        property2.addListener(binding);
        return binding;
    }

    public static <T extends Number> void unbindNumber(Property<T> property1, Property<Number> property2) {
        checkParameters(property1, property2);
        BidirectionalBinding binding = new UntypedGenericBidirectionalBinding(property1, property2);
        if (property1 instanceof ObservableValue) {
            property1.removeListener(binding);
        }
        if (property2 instanceof Observable) {
            property2.removeListener(binding);
        }
    }

    private BidirectionalBinding(Object property1, Object property2) {
        this.cachedHashCode = property1.hashCode() * property2.hashCode();
    }

    public int hashCode() {
        return this.cachedHashCode;
    }

    @Override // javafx.beans.WeakListener
    public boolean wasGarbageCollected() {
        return getProperty1() == null || getProperty2() == null;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        Object propertyA1 = getProperty1();
        Object propertyA2 = getProperty2();
        if (propertyA1 != null && propertyA2 != null && (obj instanceof BidirectionalBinding)) {
            BidirectionalBinding otherBinding = (BidirectionalBinding) obj;
            Object propertyB1 = otherBinding.getProperty1();
            Object propertyB2 = otherBinding.getProperty2();
            if (propertyB1 == null || propertyB2 == null) {
                return false;
            }
            if (propertyA1 == propertyB1 && propertyA2 == propertyB2) {
                return true;
            }
            if (propertyA1 == propertyB2 && propertyA2 == propertyB1) {
                return true;
            }
            return false;
        }
        return false;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/binding/BidirectionalBinding$BidirectionalBooleanBinding.class */
    private static class BidirectionalBooleanBinding extends BidirectionalBinding<Boolean> {
        private final WeakReference<BooleanProperty> propertyRef1;
        private final WeakReference<BooleanProperty> propertyRef2;
        private boolean updating;

        @Override // javafx.beans.value.ChangeListener
        public /* bridge */ /* synthetic */ void changed(ObservableValue observableValue, Object obj, Object obj2) {
            changed((ObservableValue<? extends Boolean>) observableValue, (Boolean) obj, (Boolean) obj2);
        }

        private BidirectionalBooleanBinding(BooleanProperty property1, BooleanProperty property2) {
            super(property1, property2);
            this.updating = false;
            this.propertyRef1 = new WeakReference<>(property1);
            this.propertyRef2 = new WeakReference<>(property2);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.sun.javafx.binding.BidirectionalBinding
        public Property<Boolean> getProperty1() {
            return this.propertyRef1.get();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.sun.javafx.binding.BidirectionalBinding
        public Property<Boolean> getProperty2() {
            return this.propertyRef2.get();
        }

        public void changed(ObservableValue<? extends Boolean> sourceProperty, Boolean oldValue, Boolean newValue) {
            if (!this.updating) {
                BooleanProperty property1 = this.propertyRef1.get();
                BooleanProperty property2 = this.propertyRef2.get();
                if (property1 != null) {
                    try {
                        if (property2 != null) {
                            try {
                                this.updating = true;
                                if (property1 == sourceProperty) {
                                    property2.set(newValue.booleanValue());
                                } else {
                                    property1.set(newValue.booleanValue());
                                }
                                return;
                            } catch (RuntimeException e2) {
                                try {
                                    if (property1 == sourceProperty) {
                                        property1.set(oldValue.booleanValue());
                                    } else {
                                        property2.set(oldValue.booleanValue());
                                    }
                                    throw new RuntimeException("Bidirectional binding failed, setting to the previous value", e2);
                                } catch (Exception e22) {
                                    e22.addSuppressed(e2);
                                    unbind((Property) property1, (Property) property2);
                                    throw new RuntimeException("Bidirectional binding failed together with an attempt to restore the source property to the previous value. Removing the bidirectional binding from properties " + ((Object) property1) + " and " + ((Object) property2), e22);
                                }
                            }
                        }
                    } finally {
                        this.updating = false;
                    }
                }
                if (property1 != null) {
                    property1.removeListener(this);
                }
                if (property2 != null) {
                    property2.removeListener(this);
                }
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/binding/BidirectionalBinding$BidirectionalDoubleBinding.class */
    private static class BidirectionalDoubleBinding extends BidirectionalBinding<Number> {
        private final WeakReference<DoubleProperty> propertyRef1;
        private final WeakReference<DoubleProperty> propertyRef2;
        private boolean updating;

        @Override // javafx.beans.value.ChangeListener
        public /* bridge */ /* synthetic */ void changed(ObservableValue observableValue, Object obj, Object obj2) {
            changed((ObservableValue<? extends Number>) observableValue, (Number) obj, (Number) obj2);
        }

        private BidirectionalDoubleBinding(DoubleProperty property1, DoubleProperty property2) {
            super(property1, property2);
            this.updating = false;
            this.propertyRef1 = new WeakReference<>(property1);
            this.propertyRef2 = new WeakReference<>(property2);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.sun.javafx.binding.BidirectionalBinding
        public Property<Number> getProperty1() {
            return this.propertyRef1.get();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.sun.javafx.binding.BidirectionalBinding
        public Property<Number> getProperty2() {
            return this.propertyRef2.get();
        }

        public void changed(ObservableValue<? extends Number> sourceProperty, Number oldValue, Number newValue) {
            if (!this.updating) {
                DoubleProperty property1 = this.propertyRef1.get();
                DoubleProperty property2 = this.propertyRef2.get();
                if (property1 != null) {
                    try {
                        if (property2 != null) {
                            try {
                                this.updating = true;
                                if (property1 == sourceProperty) {
                                    property2.set(newValue.doubleValue());
                                } else {
                                    property1.set(newValue.doubleValue());
                                }
                                return;
                            } catch (RuntimeException e2) {
                                try {
                                    if (property1 == sourceProperty) {
                                        property1.set(oldValue.doubleValue());
                                    } else {
                                        property2.set(oldValue.doubleValue());
                                    }
                                    throw new RuntimeException("Bidirectional binding failed, setting to the previous value", e2);
                                } catch (Exception e22) {
                                    e22.addSuppressed(e2);
                                    unbind((Property) property1, (Property) property2);
                                    throw new RuntimeException("Bidirectional binding failed together with an attempt to restore the source property to the previous value. Removing the bidirectional binding from properties " + ((Object) property1) + " and " + ((Object) property2), e22);
                                }
                            }
                        }
                    } finally {
                        this.updating = false;
                    }
                }
                if (property1 != null) {
                    property1.removeListener(this);
                }
                if (property2 != null) {
                    property2.removeListener(this);
                }
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/binding/BidirectionalBinding$BidirectionalFloatBinding.class */
    private static class BidirectionalFloatBinding extends BidirectionalBinding<Number> {
        private final WeakReference<FloatProperty> propertyRef1;
        private final WeakReference<FloatProperty> propertyRef2;
        private boolean updating;

        @Override // javafx.beans.value.ChangeListener
        public /* bridge */ /* synthetic */ void changed(ObservableValue observableValue, Object obj, Object obj2) {
            changed((ObservableValue<? extends Number>) observableValue, (Number) obj, (Number) obj2);
        }

        private BidirectionalFloatBinding(FloatProperty property1, FloatProperty property2) {
            super(property1, property2);
            this.updating = false;
            this.propertyRef1 = new WeakReference<>(property1);
            this.propertyRef2 = new WeakReference<>(property2);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.sun.javafx.binding.BidirectionalBinding
        public Property<Number> getProperty1() {
            return this.propertyRef1.get();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.sun.javafx.binding.BidirectionalBinding
        public Property<Number> getProperty2() {
            return this.propertyRef2.get();
        }

        public void changed(ObservableValue<? extends Number> sourceProperty, Number oldValue, Number newValue) {
            if (!this.updating) {
                FloatProperty property1 = this.propertyRef1.get();
                FloatProperty property2 = this.propertyRef2.get();
                if (property1 != null) {
                    try {
                        if (property2 != null) {
                            try {
                                this.updating = true;
                                if (property1 == sourceProperty) {
                                    property2.set(newValue.floatValue());
                                } else {
                                    property1.set(newValue.floatValue());
                                }
                                return;
                            } catch (RuntimeException e2) {
                                try {
                                    if (property1 == sourceProperty) {
                                        property1.set(oldValue.floatValue());
                                    } else {
                                        property2.set(oldValue.floatValue());
                                    }
                                    throw new RuntimeException("Bidirectional binding failed, setting to the previous value", e2);
                                } catch (Exception e22) {
                                    e22.addSuppressed(e2);
                                    unbind((Property) property1, (Property) property2);
                                    throw new RuntimeException("Bidirectional binding failed together with an attempt to restore the source property to the previous value. Removing the bidirectional binding from properties " + ((Object) property1) + " and " + ((Object) property2), e22);
                                }
                            }
                        }
                    } finally {
                        this.updating = false;
                    }
                }
                if (property1 != null) {
                    property1.removeListener(this);
                }
                if (property2 != null) {
                    property2.removeListener(this);
                }
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/binding/BidirectionalBinding$BidirectionalIntegerBinding.class */
    private static class BidirectionalIntegerBinding extends BidirectionalBinding<Number> {
        private final WeakReference<IntegerProperty> propertyRef1;
        private final WeakReference<IntegerProperty> propertyRef2;
        private boolean updating;

        @Override // javafx.beans.value.ChangeListener
        public /* bridge */ /* synthetic */ void changed(ObservableValue observableValue, Object obj, Object obj2) {
            changed((ObservableValue<? extends Number>) observableValue, (Number) obj, (Number) obj2);
        }

        private BidirectionalIntegerBinding(IntegerProperty property1, IntegerProperty property2) {
            super(property1, property2);
            this.updating = false;
            this.propertyRef1 = new WeakReference<>(property1);
            this.propertyRef2 = new WeakReference<>(property2);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.sun.javafx.binding.BidirectionalBinding
        public Property<Number> getProperty1() {
            return this.propertyRef1.get();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.sun.javafx.binding.BidirectionalBinding
        public Property<Number> getProperty2() {
            return this.propertyRef2.get();
        }

        public void changed(ObservableValue<? extends Number> sourceProperty, Number oldValue, Number newValue) {
            if (!this.updating) {
                IntegerProperty property1 = this.propertyRef1.get();
                IntegerProperty property2 = this.propertyRef2.get();
                if (property1 != null) {
                    try {
                        if (property2 != null) {
                            try {
                                this.updating = true;
                                if (property1 == sourceProperty) {
                                    property2.set(newValue.intValue());
                                } else {
                                    property1.set(newValue.intValue());
                                }
                                return;
                            } catch (RuntimeException e2) {
                                try {
                                    if (property1 == sourceProperty) {
                                        property1.set(oldValue.intValue());
                                    } else {
                                        property2.set(oldValue.intValue());
                                    }
                                    throw new RuntimeException("Bidirectional binding failed, setting to the previous value", e2);
                                } catch (Exception e22) {
                                    e22.addSuppressed(e2);
                                    unbind((Property) property1, (Property) property2);
                                    throw new RuntimeException("Bidirectional binding failed together with an attempt to restore the source property to the previous value. Removing the bidirectional binding from properties " + ((Object) property1) + " and " + ((Object) property2), e22);
                                }
                            }
                        }
                    } finally {
                        this.updating = false;
                    }
                }
                if (property1 != null) {
                    property1.removeListener(this);
                }
                if (property2 != null) {
                    property2.removeListener(this);
                }
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/binding/BidirectionalBinding$BidirectionalLongBinding.class */
    private static class BidirectionalLongBinding extends BidirectionalBinding<Number> {
        private final WeakReference<LongProperty> propertyRef1;
        private final WeakReference<LongProperty> propertyRef2;
        private boolean updating;

        @Override // javafx.beans.value.ChangeListener
        public /* bridge */ /* synthetic */ void changed(ObservableValue observableValue, Object obj, Object obj2) {
            changed((ObservableValue<? extends Number>) observableValue, (Number) obj, (Number) obj2);
        }

        private BidirectionalLongBinding(LongProperty property1, LongProperty property2) {
            super(property1, property2);
            this.updating = false;
            this.propertyRef1 = new WeakReference<>(property1);
            this.propertyRef2 = new WeakReference<>(property2);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.sun.javafx.binding.BidirectionalBinding
        public Property<Number> getProperty1() {
            return this.propertyRef1.get();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.sun.javafx.binding.BidirectionalBinding
        public Property<Number> getProperty2() {
            return this.propertyRef2.get();
        }

        public void changed(ObservableValue<? extends Number> sourceProperty, Number oldValue, Number newValue) {
            if (!this.updating) {
                LongProperty property1 = this.propertyRef1.get();
                LongProperty property2 = this.propertyRef2.get();
                if (property1 != null) {
                    try {
                        if (property2 != null) {
                            try {
                                this.updating = true;
                                if (property1 == sourceProperty) {
                                    property2.set(newValue.longValue());
                                } else {
                                    property1.set(newValue.longValue());
                                }
                                return;
                            } catch (RuntimeException e2) {
                                try {
                                    if (property1 == sourceProperty) {
                                        property1.set(oldValue.longValue());
                                    } else {
                                        property2.set(oldValue.longValue());
                                    }
                                    throw new RuntimeException("Bidirectional binding failed, setting to the previous value", e2);
                                } catch (Exception e22) {
                                    e22.addSuppressed(e2);
                                    unbind((Property) property1, (Property) property2);
                                    throw new RuntimeException("Bidirectional binding failed together with an attempt to restore the source property to the previous value. Removing the bidirectional binding from properties " + ((Object) property1) + " and " + ((Object) property2), e22);
                                }
                            }
                        }
                    } finally {
                        this.updating = false;
                    }
                }
                if (property1 != null) {
                    property1.removeListener(this);
                }
                if (property2 != null) {
                    property2.removeListener(this);
                }
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/binding/BidirectionalBinding$TypedGenericBidirectionalBinding.class */
    private static class TypedGenericBidirectionalBinding<T> extends BidirectionalBinding<T> {
        private final WeakReference<Property<T>> propertyRef1;
        private final WeakReference<Property<T>> propertyRef2;
        private boolean updating;

        private TypedGenericBidirectionalBinding(Property<T> property1, Property<T> property2) {
            super(property1, property2);
            this.updating = false;
            this.propertyRef1 = new WeakReference<>(property1);
            this.propertyRef2 = new WeakReference<>(property2);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.sun.javafx.binding.BidirectionalBinding
        public Property<T> getProperty1() {
            return this.propertyRef1.get();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.sun.javafx.binding.BidirectionalBinding
        public Property<T> getProperty2() {
            return this.propertyRef2.get();
        }

        @Override // javafx.beans.value.ChangeListener
        public void changed(ObservableValue<? extends T> sourceProperty, T oldValue, T newValue) {
            if (!this.updating) {
                Property<T> property1 = this.propertyRef1.get();
                Property<T> property2 = this.propertyRef2.get();
                if (property1 != null) {
                    try {
                        if (property2 != null) {
                            try {
                                this.updating = true;
                                if (property1 == sourceProperty) {
                                    property2.setValue(newValue);
                                } else {
                                    property1.setValue(newValue);
                                }
                                return;
                            } catch (RuntimeException e2) {
                                try {
                                    if (property1 == sourceProperty) {
                                        property1.setValue(oldValue);
                                    } else {
                                        property2.setValue(oldValue);
                                    }
                                    throw new RuntimeException("Bidirectional binding failed, setting to the previous value", e2);
                                } catch (Exception e22) {
                                    e22.addSuppressed(e2);
                                    unbind((Property) property1, (Property) property2);
                                    throw new RuntimeException("Bidirectional binding failed together with an attempt to restore the source property to the previous value. Removing the bidirectional binding from properties " + ((Object) property1) + " and " + ((Object) property2), e22);
                                }
                            }
                        }
                    } finally {
                        this.updating = false;
                    }
                }
                if (property1 != null) {
                    property1.removeListener(this);
                }
                if (property2 != null) {
                    property2.removeListener(this);
                }
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/binding/BidirectionalBinding$TypedNumberBidirectionalBinding.class */
    private static class TypedNumberBidirectionalBinding<T extends Number> extends BidirectionalBinding<Number> {
        private final WeakReference<Property<T>> propertyRef1;
        private final WeakReference<Property<Number>> propertyRef2;
        private boolean updating;

        @Override // javafx.beans.value.ChangeListener
        public /* bridge */ /* synthetic */ void changed(ObservableValue observableValue, Object obj, Object obj2) {
            changed((ObservableValue<? extends Number>) observableValue, (Number) obj, (Number) obj2);
        }

        private TypedNumberBidirectionalBinding(Property<T> property1, Property<Number> property2) {
            super(property1, property2);
            this.updating = false;
            this.propertyRef1 = new WeakReference<>(property1);
            this.propertyRef2 = new WeakReference<>(property2);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.sun.javafx.binding.BidirectionalBinding
        public Property<T> getProperty1() {
            return this.propertyRef1.get();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.sun.javafx.binding.BidirectionalBinding
        public Property<Number> getProperty2() {
            return this.propertyRef2.get();
        }

        public void changed(ObservableValue<? extends Number> sourceProperty, Number oldValue, Number newValue) {
            if (!this.updating) {
                Property<T> property1 = this.propertyRef1.get();
                Property<Number> property2 = this.propertyRef2.get();
                if (property1 != null) {
                    try {
                        if (property2 != null) {
                            try {
                                this.updating = true;
                                if (property1 == sourceProperty) {
                                    property2.setValue(newValue);
                                } else {
                                    property1.setValue(newValue);
                                }
                                return;
                            } catch (RuntimeException e2) {
                                try {
                                    if (property1 == sourceProperty) {
                                        property1.setValue(oldValue);
                                    } else {
                                        property2.setValue(oldValue);
                                    }
                                    throw new RuntimeException("Bidirectional binding failed, setting to the previous value", e2);
                                } catch (Exception e22) {
                                    e22.addSuppressed(e2);
                                    unbind((Object) property1, (Object) property2);
                                    throw new RuntimeException("Bidirectional binding failed together with an attempt to restore the source property to the previous value. Removing the bidirectional binding from properties " + ((Object) property1) + " and " + ((Object) property2), e22);
                                }
                            }
                        }
                    } finally {
                        this.updating = false;
                    }
                }
                if (property1 != null) {
                    property1.removeListener(this);
                }
                if (property2 != null) {
                    property2.removeListener(this);
                }
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/binding/BidirectionalBinding$UntypedGenericBidirectionalBinding.class */
    private static class UntypedGenericBidirectionalBinding extends BidirectionalBinding<Object> {
        private final Object property1;
        private final Object property2;

        public UntypedGenericBidirectionalBinding(Object property1, Object property2) {
            super(property1, property2);
            this.property1 = property1;
            this.property2 = property2;
        }

        @Override // com.sun.javafx.binding.BidirectionalBinding
        protected Object getProperty1() {
            return this.property1;
        }

        @Override // com.sun.javafx.binding.BidirectionalBinding
        protected Object getProperty2() {
            return this.property2;
        }

        @Override // javafx.beans.value.ChangeListener
        public void changed(ObservableValue<? extends Object> sourceProperty, Object oldValue, Object newValue) {
            throw new RuntimeException("Should not reach here");
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/binding/BidirectionalBinding$StringConversionBidirectionalBinding.class */
    public static abstract class StringConversionBidirectionalBinding<T> extends BidirectionalBinding<Object> {
        private final WeakReference<Property<String>> stringPropertyRef;
        private final WeakReference<Property<T>> otherPropertyRef;
        private boolean updating;

        protected abstract String toString(T t2);

        protected abstract T fromString(String str) throws ParseException;

        public StringConversionBidirectionalBinding(Property<String> stringProperty, Property<T> otherProperty) {
            super(stringProperty, otherProperty);
            this.stringPropertyRef = new WeakReference<>(stringProperty);
            this.otherPropertyRef = new WeakReference<>(otherProperty);
        }

        @Override // com.sun.javafx.binding.BidirectionalBinding
        protected Object getProperty1() {
            return this.stringPropertyRef.get();
        }

        @Override // com.sun.javafx.binding.BidirectionalBinding
        protected Object getProperty2() {
            return this.otherPropertyRef.get();
        }

        @Override // javafx.beans.value.ChangeListener
        public void changed(ObservableValue<? extends Object> observableValue, Object obj, Object obj2) {
            if (!this.updating) {
                Property<String> property = this.stringPropertyRef.get();
                Property property2 = (Property<T>) this.otherPropertyRef.get();
                if (property == null || property2 == null) {
                    if (property != null) {
                        property.removeListener(this);
                    }
                    if (property2 != null) {
                        property2.removeListener(this);
                        return;
                    }
                    return;
                }
                try {
                    this.updating = true;
                    if (property == observableValue) {
                        try {
                            property2.setValue(fromString(property.getValue2()));
                        } catch (Exception e2) {
                            Logging.getLogger().warning("Exception while parsing String in bidirectional binding", e2);
                            property2.setValue(null);
                        }
                    } else {
                        try {
                            property.setValue(toString(property2.getValue2()));
                        } catch (Exception e3) {
                            Logging.getLogger().warning("Exception while converting Object to String in bidirectional binding", e3);
                            property.setValue("");
                        }
                    }
                } finally {
                    this.updating = false;
                }
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/binding/BidirectionalBinding$StringFormatBidirectionalBinding.class */
    private static class StringFormatBidirectionalBinding extends StringConversionBidirectionalBinding {
        private final Format format;

        public StringFormatBidirectionalBinding(Property<String> stringProperty, Property<?> otherProperty, Format format) {
            super(stringProperty, otherProperty);
            this.format = format;
        }

        @Override // com.sun.javafx.binding.BidirectionalBinding.StringConversionBidirectionalBinding
        protected String toString(Object value) {
            return this.format.format(value);
        }

        @Override // com.sun.javafx.binding.BidirectionalBinding.StringConversionBidirectionalBinding
        protected Object fromString(String value) throws ParseException {
            return this.format.parseObject(value);
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/binding/BidirectionalBinding$StringConverterBidirectionalBinding.class */
    private static class StringConverterBidirectionalBinding<T> extends StringConversionBidirectionalBinding<T> {
        private final StringConverter<T> converter;

        public StringConverterBidirectionalBinding(Property<String> stringProperty, Property<T> otherProperty, StringConverter<T> converter) {
            super(stringProperty, otherProperty);
            this.converter = converter;
        }

        @Override // com.sun.javafx.binding.BidirectionalBinding.StringConversionBidirectionalBinding
        protected String toString(T value) {
            return this.converter.toString(value);
        }

        @Override // com.sun.javafx.binding.BidirectionalBinding.StringConversionBidirectionalBinding
        protected T fromString(String value) throws ParseException {
            return this.converter.fromString(value);
        }
    }
}
