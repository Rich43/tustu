package com.efiAnalytics.ui.calculators.calculator;

import java.util.OptionalDouble;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/calculators/calculator/Expression.class */
class Expression {

    /* renamed from: a, reason: collision with root package name */
    private double f11201a;

    /* renamed from: b, reason: collision with root package name */
    private int f11202b;

    /* renamed from: c, reason: collision with root package name */
    private Operator f11203c = null;

    Expression() {
    }

    private double a(Operator operator, double d2) {
        if (operator == Operator.EQUAL) {
            this.f11201a = this.f11203c.a(this.f11201a, d2);
            this.f11202b = 0;
        } else {
            this.f11202b++;
            this.f11201a = this.f11202b > 1 ? this.f11203c.a(this.f11201a, d2) : d2;
            this.f11203c = operator;
        }
        return this.f11201a;
    }

    double a(Operator operator, OptionalDouble optionalDouble) {
        return a(operator, optionalDouble.orElse(this.f11201a));
    }

    void a() {
        this.f11201a = 0.0d;
        this.f11202b = 0;
        this.f11203c = null;
    }
}
