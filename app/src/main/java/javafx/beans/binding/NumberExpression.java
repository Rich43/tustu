package javafx.beans.binding;

import java.util.Locale;
import javafx.beans.value.ObservableNumberValue;

/* loaded from: jfxrt.jar:javafx/beans/binding/NumberExpression.class */
public interface NumberExpression extends ObservableNumberValue {
    NumberBinding negate();

    NumberBinding add(ObservableNumberValue observableNumberValue);

    NumberBinding add(double d2);

    NumberBinding add(float f2);

    NumberBinding add(long j2);

    NumberBinding add(int i2);

    NumberBinding subtract(ObservableNumberValue observableNumberValue);

    NumberBinding subtract(double d2);

    NumberBinding subtract(float f2);

    NumberBinding subtract(long j2);

    NumberBinding subtract(int i2);

    NumberBinding multiply(ObservableNumberValue observableNumberValue);

    NumberBinding multiply(double d2);

    NumberBinding multiply(float f2);

    NumberBinding multiply(long j2);

    NumberBinding multiply(int i2);

    NumberBinding divide(ObservableNumberValue observableNumberValue);

    NumberBinding divide(double d2);

    NumberBinding divide(float f2);

    NumberBinding divide(long j2);

    NumberBinding divide(int i2);

    BooleanBinding isEqualTo(ObservableNumberValue observableNumberValue);

    BooleanBinding isEqualTo(ObservableNumberValue observableNumberValue, double d2);

    BooleanBinding isEqualTo(double d2, double d3);

    BooleanBinding isEqualTo(float f2, double d2);

    BooleanBinding isEqualTo(long j2);

    BooleanBinding isEqualTo(long j2, double d2);

    BooleanBinding isEqualTo(int i2);

    BooleanBinding isEqualTo(int i2, double d2);

    BooleanBinding isNotEqualTo(ObservableNumberValue observableNumberValue);

    BooleanBinding isNotEqualTo(ObservableNumberValue observableNumberValue, double d2);

    BooleanBinding isNotEqualTo(double d2, double d3);

    BooleanBinding isNotEqualTo(float f2, double d2);

    BooleanBinding isNotEqualTo(long j2);

    BooleanBinding isNotEqualTo(long j2, double d2);

    BooleanBinding isNotEqualTo(int i2);

    BooleanBinding isNotEqualTo(int i2, double d2);

    BooleanBinding greaterThan(ObservableNumberValue observableNumberValue);

    BooleanBinding greaterThan(double d2);

    BooleanBinding greaterThan(float f2);

    BooleanBinding greaterThan(long j2);

    BooleanBinding greaterThan(int i2);

    BooleanBinding lessThan(ObservableNumberValue observableNumberValue);

    BooleanBinding lessThan(double d2);

    BooleanBinding lessThan(float f2);

    BooleanBinding lessThan(long j2);

    BooleanBinding lessThan(int i2);

    BooleanBinding greaterThanOrEqualTo(ObservableNumberValue observableNumberValue);

    BooleanBinding greaterThanOrEqualTo(double d2);

    BooleanBinding greaterThanOrEqualTo(float f2);

    BooleanBinding greaterThanOrEqualTo(long j2);

    BooleanBinding greaterThanOrEqualTo(int i2);

    BooleanBinding lessThanOrEqualTo(ObservableNumberValue observableNumberValue);

    BooleanBinding lessThanOrEqualTo(double d2);

    BooleanBinding lessThanOrEqualTo(float f2);

    BooleanBinding lessThanOrEqualTo(long j2);

    BooleanBinding lessThanOrEqualTo(int i2);

    StringBinding asString();

    StringBinding asString(String str);

    StringBinding asString(Locale locale, String str);
}
