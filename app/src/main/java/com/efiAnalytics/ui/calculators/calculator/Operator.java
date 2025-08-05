package com.efiAnalytics.ui.calculators.calculator;

import java.util.Map;
import java.util.Optional;
import java.util.function.DoubleBinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Marker;
import sun.util.locale.LanguageTag;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/calculators/calculator/Operator.class */
enum Operator {
    EQUAL("=", (d2, d3) -> {
        throw new UnsupportedOperationException();
    }),
    PLUS(Marker.ANY_NON_NULL_MARKER, (d4, d5) -> {
        return d4 + d5;
    }),
    MINUS(LanguageTag.SEP, (d6, d7) -> {
        return d6 - d7;
    }),
    MULTIPLY(LanguageTag.PRIVATEUSE, (d8, d9) -> {
        return d8 * d9;
    }),
    DIVIDE("/", (d10, d11) -> {
        return d10 / d11;
    });


    /* renamed from: f, reason: collision with root package name */
    private final String f11236f;

    /* renamed from: g, reason: collision with root package name */
    private final DoubleBinaryOperator f11237g;

    /* renamed from: h, reason: collision with root package name */
    private static final Map f11238h = (Map) Stream.of((Object[]) values()).collect(Collectors.toMap((v0) -> {
        return v0.toString();
    }, operator -> {
        return operator;
    }));

    Operator(String str, DoubleBinaryOperator doubleBinaryOperator) {
        this.f11236f = str;
        this.f11237g = doubleBinaryOperator;
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.f11236f;
    }

    public double a(double d2, double d3) {
        return this.f11237g.applyAsDouble(d2, d3);
    }

    public static Optional a(String str) {
        return Optional.ofNullable(f11238h.get(str));
    }
}
