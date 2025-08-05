package com.sun.beans.decoder;

/* loaded from: rt.jar:com/sun/beans/decoder/CharElementHandler.class */
final class CharElementHandler extends StringElementHandler {
    CharElementHandler() {
    }

    @Override // com.sun.beans.decoder.ElementHandler
    public void addAttribute(String str, String str2) {
        if (str.equals("code")) {
            for (char c2 : Character.toChars(Integer.decode(str2).intValue())) {
                addCharacter(c2);
            }
            return;
        }
        super.addAttribute(str, str2);
    }

    @Override // com.sun.beans.decoder.StringElementHandler
    public Object getValue(String str) {
        if (str.length() != 1) {
            throw new IllegalArgumentException("Wrong characters count");
        }
        return Character.valueOf(str.charAt(0));
    }
}
