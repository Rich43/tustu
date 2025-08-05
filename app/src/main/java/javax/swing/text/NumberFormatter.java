package javax.swing.text;

import java.lang.reflect.Constructor;
import java.text.AttributedCharacterIterator;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Map;
import javax.swing.text.DocumentFilter;
import sun.reflect.misc.ReflectUtil;
import sun.swing.SwingUtilities2;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:javax/swing/text/NumberFormatter.class */
public class NumberFormatter extends InternationalFormatter {
    private String specialChars;

    public NumberFormatter() {
        this(NumberFormat.getNumberInstance());
    }

    public NumberFormatter(NumberFormat numberFormat) {
        super(numberFormat);
        setFormat(numberFormat);
        setAllowsInvalid(true);
        setCommitsOnValidEdit(false);
        setOverwriteMode(false);
    }

    @Override // javax.swing.text.InternationalFormatter
    public void setFormat(Format format) {
        super.setFormat(format);
        DecimalFormatSymbols decimalFormatSymbols = getDecimalFormatSymbols();
        if (decimalFormatSymbols != null) {
            this.specialChars = decimalFormatSymbols.getCurrencySymbol() + decimalFormatSymbols.getDecimalSeparator() + decimalFormatSymbols.getGroupingSeparator() + decimalFormatSymbols.getInfinity() + decimalFormatSymbols.getInternationalCurrencySymbol() + decimalFormatSymbols.getMinusSign() + decimalFormatSymbols.getMonetaryDecimalSeparator() + decimalFormatSymbols.getNaN() + decimalFormatSymbols.getPercent() + '+';
            return;
        }
        this.specialChars = "";
    }

    @Override // javax.swing.text.InternationalFormatter
    Object stringToValue(String str, Format format) throws ParseException {
        if (format == null) {
            return str;
        }
        return convertValueToValueClass(format.parseObject(str), getValueClass());
    }

    private Object convertValueToValueClass(Object obj, Class cls) {
        if (cls != null && (obj instanceof Number)) {
            Number number = (Number) obj;
            if (cls == Integer.class) {
                return Integer.valueOf(number.intValue());
            }
            if (cls == Long.class) {
                return Long.valueOf(number.longValue());
            }
            if (cls == Float.class) {
                return Float.valueOf(number.floatValue());
            }
            if (cls == Double.class) {
                return Double.valueOf(number.doubleValue());
            }
            if (cls == Byte.class) {
                return Byte.valueOf(number.byteValue());
            }
            if (cls == Short.class) {
                return Short.valueOf(number.shortValue());
            }
        }
        return obj;
    }

    private char getPositiveSign() {
        return '+';
    }

    private char getMinusSign() {
        DecimalFormatSymbols decimalFormatSymbols = getDecimalFormatSymbols();
        if (decimalFormatSymbols != null) {
            return decimalFormatSymbols.getMinusSign();
        }
        return '-';
    }

    private char getDecimalSeparator() {
        DecimalFormatSymbols decimalFormatSymbols = getDecimalFormatSymbols();
        if (decimalFormatSymbols != null) {
            return decimalFormatSymbols.getDecimalSeparator();
        }
        return '.';
    }

    private DecimalFormatSymbols getDecimalFormatSymbols() {
        Format format = getFormat();
        if (format instanceof DecimalFormat) {
            return ((DecimalFormat) format).getDecimalFormatSymbols();
        }
        return null;
    }

    @Override // javax.swing.text.DefaultFormatter
    boolean isLegalInsertText(String str) {
        if (getAllowsInvalid()) {
            return true;
        }
        for (int length = str.length() - 1; length >= 0; length--) {
            char cCharAt = str.charAt(length);
            if (!Character.isDigit(cCharAt) && this.specialChars.indexOf(cCharAt) == -1) {
                return false;
            }
        }
        return true;
    }

    @Override // javax.swing.text.InternationalFormatter
    boolean isLiteral(Map map) {
        if (!super.isLiteral(map)) {
            if (map == null) {
                return false;
            }
            int size = map.size();
            if (map.get(NumberFormat.Field.GROUPING_SEPARATOR) != null) {
                size--;
                if (map.get(NumberFormat.Field.INTEGER) != null) {
                    size--;
                }
            }
            if (map.get(NumberFormat.Field.EXPONENT_SYMBOL) != null) {
                size--;
            }
            if (map.get(NumberFormat.Field.PERCENT) != null) {
                size--;
            }
            if (map.get(NumberFormat.Field.PERMILLE) != null) {
                size--;
            }
            if (map.get(NumberFormat.Field.CURRENCY) != null) {
                size--;
            }
            if (map.get(NumberFormat.Field.SIGN) != null) {
                size--;
            }
            return size == 0;
        }
        return true;
    }

    @Override // javax.swing.text.InternationalFormatter, javax.swing.text.DefaultFormatter
    boolean isNavigatable(int i2) {
        return super.isNavigatable(i2) || getBufferedChar(i2) == getDecimalSeparator();
    }

    private NumberFormat.Field getFieldFrom(int i2, int i3) {
        if (isValidMask()) {
            int length = getFormattedTextField().getDocument().getLength();
            AttributedCharacterIterator iterator = getIterator();
            if (i2 >= length) {
                i2 += i3;
            }
            while (i2 >= 0 && i2 < length) {
                iterator.setIndex(i2);
                Map<AttributedCharacterIterator.Attribute, Object> attributes = iterator.getAttributes();
                if (attributes != null && attributes.size() > 0) {
                    for (AttributedCharacterIterator.Attribute attribute : attributes.keySet()) {
                        if (attribute instanceof NumberFormat.Field) {
                            return (NumberFormat.Field) attribute;
                        }
                    }
                }
                i2 += i3;
            }
            return null;
        }
        return null;
    }

    @Override // javax.swing.text.InternationalFormatter, javax.swing.text.DefaultFormatter
    void replace(DocumentFilter.FilterBypass filterBypass, int i2, int i3, String str, AttributeSet attributeSet) throws SecurityException, BadLocationException {
        if (!getAllowsInvalid() && i3 == 0 && str != null && str.length() == 1 && toggleSignIfNecessary(filterBypass, i2, str.charAt(0))) {
            return;
        }
        super.replace(filterBypass, i2, i3, str, attributeSet);
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0035 A[Catch: ParseException -> 0x00a1, TryCatch #0 {ParseException -> 0x00a1, blocks: (B:9:0x001d, B:11:0x0025, B:13:0x002d, B:20:0x004b, B:23:0x0058, B:25:0x0062, B:15:0x0035, B:19:0x0043), top: B:32:0x001d }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean toggleSignIfNecessary(javax.swing.text.DocumentFilter.FilterBypass r6, int r7, char r8) throws javax.swing.text.BadLocationException {
        /*
            r5 = this;
            r0 = r8
            r1 = r5
            char r1 = r1.getMinusSign()
            if (r0 == r1) goto L10
            r0 = r8
            r1 = r5
            char r1 = r1.getPositiveSign()
            if (r0 != r1) goto La7
        L10:
            r0 = r5
            r1 = r7
            r2 = -1
            java.text.NumberFormat$Field r0 = r0.getFieldFrom(r1, r2)
            r9 = r0
            r0 = r9
            if (r0 == 0) goto L35
            r0 = r9
            java.text.NumberFormat$Field r1 = java.text.NumberFormat.Field.EXPONENT     // Catch: java.text.ParseException -> La1
            if (r0 == r1) goto L4b
            r0 = r9
            java.text.NumberFormat$Field r1 = java.text.NumberFormat.Field.EXPONENT_SYMBOL     // Catch: java.text.ParseException -> La1
            if (r0 == r1) goto L4b
            r0 = r9
            java.text.NumberFormat$Field r1 = java.text.NumberFormat.Field.EXPONENT_SIGN     // Catch: java.text.ParseException -> La1
            if (r0 == r1) goto L4b
        L35:
            r0 = r5
            r1 = r8
            r2 = r5
            char r2 = r2.getPositiveSign()     // Catch: java.text.ParseException -> La1
            if (r1 != r2) goto L42
            r1 = 1
            goto L43
        L42:
            r1 = 0
        L43:
            java.lang.Object r0 = r0.toggleSign(r1)     // Catch: java.text.ParseException -> La1
            r10 = r0
            goto L53
        L4b:
            r0 = r5
            r1 = r7
            r2 = r8
            java.lang.Object r0 = r0.toggleExponentSign(r1, r2)     // Catch: java.text.ParseException -> La1
            r10 = r0
        L53:
            r0 = r10
            if (r0 == 0) goto L9e
            r0 = r5
            r1 = r10
            r2 = 0
            boolean r0 = r0.isValidValue(r1, r2)     // Catch: java.text.ParseException -> La1
            if (r0 == 0) goto L9e
            r0 = r5
            r1 = r7
            int r0 = r0.getLiteralCountTo(r1)     // Catch: java.text.ParseException -> La1
            r11 = r0
            r0 = r5
            r1 = r10
            java.lang.String r0 = r0.valueToString(r1)     // Catch: java.text.ParseException -> La1
            r12 = r0
            r0 = r6
            r1 = 0
            r2 = r6
            javax.swing.text.Document r2 = r2.getDocument()     // Catch: java.text.ParseException -> La1
            int r2 = r2.getLength()     // Catch: java.text.ParseException -> La1
            r0.remove(r1, r2)     // Catch: java.text.ParseException -> La1
            r0 = r6
            r1 = 0
            r2 = r12
            r3 = 0
            r0.insertString(r1, r2, r3)     // Catch: java.text.ParseException -> La1
            r0 = r5
            r1 = r10
            r0.updateValue(r1)     // Catch: java.text.ParseException -> La1
            r0 = r5
            r1 = r5
            r2 = r7
            int r1 = r1.getLiteralCountTo(r2)     // Catch: java.text.ParseException -> La1
            r2 = r11
            int r1 = r1 - r2
            r2 = r7
            int r1 = r1 + r2
            r2 = 1
            r0.repositionCursor(r1, r2)     // Catch: java.text.ParseException -> La1
            r0 = 1
            return r0
        L9e:
            goto La7
        La1:
            r11 = move-exception
            r0 = r5
            r0.invalidEdit()
        La7:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.swing.text.NumberFormatter.toggleSignIfNecessary(javax.swing.text.DocumentFilter$FilterBypass, int, char):boolean");
    }

    private Object toggleSign(boolean z2) throws ParseException {
        Object objStringToValue = stringToValue(getFormattedTextField().getText());
        if (objStringToValue != null) {
            String string = objStringToValue.toString();
            if (string != null && string.length() > 0) {
                if (z2) {
                    if (string.charAt(0) == '-') {
                        string = string.substring(1);
                    }
                } else {
                    if (string.charAt(0) == '+') {
                        string = string.substring(1);
                    }
                    if (string.length() > 0 && string.charAt(0) != '-') {
                        string = LanguageTag.SEP + string;
                    }
                }
                if (string != null) {
                    Class<?> valueClass = getValueClass();
                    if (valueClass == null) {
                        valueClass = objStringToValue.getClass();
                    }
                    try {
                        ReflectUtil.checkPackageAccess(valueClass);
                        SwingUtilities2.checkAccess(valueClass.getModifiers());
                        Constructor<?> constructor = valueClass.getConstructor(String.class);
                        if (constructor != null) {
                            SwingUtilities2.checkAccess(constructor.getModifiers());
                            return constructor.newInstance(string);
                        }
                        return null;
                    } catch (Throwable th) {
                        return null;
                    }
                }
                return null;
            }
            return null;
        }
        return null;
    }

    private Object toggleExponentSign(int i2, char c2) throws BadLocationException, ParseException {
        String replaceString;
        getFormattedTextField().getText();
        int i3 = 0;
        int attributeStart = getAttributeStart(NumberFormat.Field.EXPONENT_SIGN);
        if (attributeStart >= 0) {
            i3 = 1;
            i2 = attributeStart;
        }
        if (c2 == getPositiveSign()) {
            replaceString = getReplaceString(i2, i3, null);
        } else {
            replaceString = getReplaceString(i2, i3, new String(new char[]{c2}));
        }
        return stringToValue(replaceString);
    }
}
