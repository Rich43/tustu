package com.efiAnalytics.ui.calculators.calculator;

import com.efiAnalytics.ui.eJ;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.function.BiConsumer;
import javax.swing.JButton;
import javax.swing.JPanel;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/calculators/calculator/Keypad.class */
class Keypad extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    private final Calculator f11227a;

    /* renamed from: b, reason: collision with root package name */
    private final Key[][] f11228b = {new Key[]{Key.MPLUS, Key.MMINUS, Key.PERCENT, Key.CLEAR}, new Key[]{Key.SEVEN, Key.EIGHT, Key.NINE, Key.DIVIDE}, new Key[]{Key.FOUR, Key.FIVE, Key.SIX, Key.MULTIPLY}, new Key[]{Key.ONE, Key.TWO, Key.THREE, Key.MINUS}, new Key[]{Key.ZERO, Key.DOT, Key.EQUAL, Key.PLUS}};

    /* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/calculators/calculator/Keypad$KeyButton.class */
    class KeyButton extends JButton implements Command {

        /* renamed from: b, reason: collision with root package name */
        private BiConsumer f11229b;

        KeyButton(Key key) {
            super(key.toString());
            addActionListener(Keypad.this.f11227a);
            this.f11229b = key.a();
            setFont(new Font("Consolas", 1, eJ.a(30)));
            setFocusable(false);
        }

        @Override // com.efiAnalytics.ui.calculators.calculator.Command
        public void a() {
            this.f11229b.accept(Keypad.this.f11227a, getText());
        }
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.efiAnalytics.ui.calculators.calculator.Key[], com.efiAnalytics.ui.calculators.calculator.Key[][]] */
    Keypad(Calculator calculator) {
        this.f11227a = calculator;
        a();
    }

    private void a() {
        setLayout(new GridLayout(this.f11228b.length, this.f11228b.length));
        Arrays.stream(this.f11228b).flatMap((v0) -> {
            return Arrays.stream(v0);
        }).map(key -> {
            return new KeyButton(key);
        }).forEach((v1) -> {
            add(v1);
        });
    }
}
