package com.efiAnalytics.ui.calculators.calculator;

import java.util.function.BiConsumer;
import javafx.fxml.FXMLLoader;
import org.slf4j.Marker;
import sun.util.locale.LanguageTag;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/calculators/calculator/Key.class */
enum Key {
    ZERO("0", (v0, v1) -> {
        v0.a(v1);
    }),
    ONE("1", (v0, v1) -> {
        v0.a(v1);
    }),
    TWO("2", (v0, v1) -> {
        v0.a(v1);
    }),
    THREE("3", (v0, v1) -> {
        v0.a(v1);
    }),
    FOUR("4", (v0, v1) -> {
        v0.a(v1);
    }),
    FIVE("5", (v0, v1) -> {
        v0.a(v1);
    }),
    SIX("6", (v0, v1) -> {
        v0.a(v1);
    }),
    SEVEN("7", (v0, v1) -> {
        v0.a(v1);
    }),
    EIGHT("8", (v0, v1) -> {
        v0.a(v1);
    }),
    NINE("9", (v0, v1) -> {
        v0.a(v1);
    }),
    DIVIDE("/", (v0, v1) -> {
        v0.c(v1);
    }),
    MULTIPLY(LanguageTag.PRIVATEUSE, (v0, v1) -> {
        v0.c(v1);
    }),
    MINUS(LanguageTag.SEP, (v0, v1) -> {
        v0.c(v1);
    }),
    EQUAL("=", (v0, v1) -> {
        v0.c(v1);
    }),
    PLUS(Marker.ANY_NON_NULL_MARKER, (v0, v1) -> {
        v0.c(v1);
    }),
    MPLUS("M+", (v0, v1) -> {
        v0.e(v1);
    }),
    MMINUS("M-", (v0, v1) -> {
        v0.e(v1);
    }),
    PERCENT(FXMLLoader.RESOURCE_KEY_PREFIX, (v0, v1) -> {
        v0.e(v1);
    }),
    DOT(".", (v0, v1) -> {
        v0.d(v1);
    }),
    CLEAR("C", (v0, v1) -> {
        v0.b(v1);
    });


    /* renamed from: u, reason: collision with root package name */
    private String f11224u;

    /* renamed from: v, reason: collision with root package name */
    private BiConsumer f11225v;

    Key(String str, BiConsumer biConsumer) {
        this.f11224u = str;
        this.f11225v = biConsumer;
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.f11224u;
    }

    public BiConsumer a() {
        return this.f11225v;
    }
}
