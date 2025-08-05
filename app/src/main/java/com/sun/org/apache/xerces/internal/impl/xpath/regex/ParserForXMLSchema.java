package com.sun.org.apache.xerces.internal.impl.xpath.regex;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xpath/regex/ParserForXMLSchema.class */
class ParserForXMLSchema extends RegexParser {
    private static final String SPACES = "\t\n\r\r  ";
    private static final String NAMECHARS = "-.0:AZ__az··ÀÖØöøıĴľŁňŊžƀǃǍǰǴǵǺȗɐʨʻˁːˑ̀͠͡ͅΆΊΌΌΎΡΣώϐϖϚϚϜϜϞϞϠϠϢϳЁЌЎяёќўҁ҃҆ҐӄӇӈӋӌӐӫӮӵӸӹԱՖՙՙաֆֹֻֽֿֿׁׂ֑֣֡ׄׄאתװײءغـْ٠٩ٰڷںھۀێېۓە۪ۭۨ۰۹ँःअह़्॑॔क़ॣ०९ঁঃঅঌএঐওনপরললশহ়়াৄেৈো্ৗৗড়ঢ়য়ৣ০ৱਂਂਅਊਏਐਓਨਪਰਲਲ਼ਵਸ਼ਸਹ਼਼ਾੂੇੈੋ੍ਖ਼ੜਫ਼ਫ਼੦ੴઁઃઅઋઍઍએઑઓનપરલળવહ઼ૅેૉો્ૠૠ૦૯ଁଃଅଌଏଐଓନପରଲଳଶହ଼ୃେୈୋ୍ୖୗଡ଼ଢ଼ୟୡ୦୯ஂஃஅஊஎஐஒகஙசஜஜஞடணதநபமவஷஹாூெைொ்ௗௗ௧௯ఁఃఅఌఎఐఒనపళవహాౄెైొ్ౕౖౠౡ౦౯ಂಃಅಌಎಐಒನಪಳವಹಾೄೆೈೊ್ೕೖೞೞೠೡ೦೯ംഃഅഌഎഐഒനപഹാൃെൈൊ്ൗൗൠൡ൦൯กฮะฺเ๎๐๙ກຂຄຄງຈຊຊຍຍດທນຟມຣລລວວສຫອຮະູົຽເໄໆໆ່ໍ໐໙༘༙༠༩༹༹༵༵༷༷༾ཇཉཀྵ྄ཱ྆ྋྐྕྗྗྙྭྱྷྐྵྐྵႠჅაჶᄀᄀᄂᄃᄅᄇᄉᄉᄋᄌᄎᄒᄼᄼᄾᄾᅀᅀᅌᅌᅎᅎᅐᅐᅔᅕᅙᅙᅟᅡᅣᅣᅥᅥᅧᅧᅩᅩᅭᅮᅲᅳᅵᅵᆞᆞᆨᆨᆫᆫᆮᆯᆷᆸᆺᆺᆼᇂᇫᇫᇰᇰᇹᇹḀẛẠỹἀἕἘἝἠὅὈὍὐὗὙὙὛὛὝὝὟώᾀᾴᾶᾼιιῂῄῆῌῐΐῖΊῠῬῲῴῶῼ⃐⃜⃡⃡ΩΩKÅ℮℮ↀↂ々々〇〇〡〯〱〵ぁゔ゙゚ゝゞァヺーヾㄅㄬ一龥가힣";
    private static final String LETTERS = "AZazÀÖØöøıĴľŁňŊžƀǰǴǵǺȗɐʨʻˁʰˑΆΆΈΊΌΌΎΡΣώϐϖϚϚϜϜϞϞϠϠϢϳЁЌЎяёќўҁҐӄӇӈӋӌӐӫӮӵӸӹԱՖՙՙաֆאתװײءغفيٱڷںھۀێېۓەەۥۦअहऽऽक़ॡঅঌএঐওনপরললশহড়ঢ়য়ৡৰৱਅਊਏਐਓਨਪਰਲਲ਼ਵਸ਼ਸਹਖ਼ੜਫ਼ਫ਼ੲੴઅઋઍઍએઑઓનપરલળવહઽઽૠૠଅଌଏଐଓନପରଲଳଶହଽଽଡ଼ଢ଼ୟୡஅஊஎஐஒகஙசஜஜஞடணதநபமவஷஹఅఌఎఐఒనపళవహౠౡಅಌಎಐಒನಪಳವಹೞೞೠೡഅഌഎഐഒനപഹൠൡกฮะะาำเๅກຂຄຄງຈຊຊຍຍດທນຟມຣລລວວສຫອຮະະາຳຽຽເໄཀཇཉཀྵႠჅაჶᄀᄀᄂᄃᄅᄇᄉᄉᄋᄌᄎᄒᄼᄼᄾᄾᅀᅀᅌᅌᅎᅎᅐᅐᅔᅕᅙᅙᅟᅡᅣᅣᅥᅥᅧᅧᅩᅩᅭᅮᅲᅳᅵᅵᆞᆞᆨᆨᆫᆫᆮᆯᆷᆸᆺᆺᆼᇂᇫᇫᇰᇰᇹᇹḀẛẠỹἀἕἘἝἠὅὈὍὐὗὙὙὛὛὝὝὟώᾀᾴᾶᾼιιῂῄῆῌῐΐῖΊῠῬῲῴῶῼΩΩKÅ℮℮ↀↂ〇〇〡〩ぁゔァヺㄅㄬ一龥가힣ｦﾟ";
    private static final String DIGITS = "09٠٩۰۹०९০৯੦੯૦૯୦୯௧௯౦౯೦೯൦൯๐๙໐໙༠༩၀၉፩፱០៩᠐᠙０９";
    private static Map<String, Token> ranges = null;
    private static Map<String, Token> ranges2 = null;
    private static final int[] LETTERS_INT = {120720, 120744, 120746, 120777, 195099, 195101};
    private static final int[] DIGITS_INT = {120782, 120831};

    public ParserForXMLSchema() {
    }

    public ParserForXMLSchema(Locale locale) {
        super(locale);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xpath.regex.RegexParser
    Token processCaret() throws ParseException {
        next();
        return Token.createChar(94);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xpath.regex.RegexParser
    Token processDollar() throws ParseException {
        next();
        return Token.createChar(36);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xpath.regex.RegexParser
    Token processLookahead() throws ParseException {
        throw ex("parser.process.1", this.offset);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xpath.regex.RegexParser
    Token processNegativelookahead() throws ParseException {
        throw ex("parser.process.1", this.offset);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xpath.regex.RegexParser
    Token processLookbehind() throws ParseException {
        throw ex("parser.process.1", this.offset);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xpath.regex.RegexParser
    Token processNegativelookbehind() throws ParseException {
        throw ex("parser.process.1", this.offset);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xpath.regex.RegexParser
    Token processBacksolidus_A() throws ParseException {
        throw ex("parser.process.1", this.offset);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xpath.regex.RegexParser
    Token processBacksolidus_Z() throws ParseException {
        throw ex("parser.process.1", this.offset);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xpath.regex.RegexParser
    Token processBacksolidus_z() throws ParseException {
        throw ex("parser.process.1", this.offset);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xpath.regex.RegexParser
    Token processBacksolidus_b() throws ParseException {
        throw ex("parser.process.1", this.offset);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xpath.regex.RegexParser
    Token processBacksolidus_B() throws ParseException {
        throw ex("parser.process.1", this.offset);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xpath.regex.RegexParser
    Token processBacksolidus_lt() throws ParseException {
        throw ex("parser.process.1", this.offset);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xpath.regex.RegexParser
    Token processBacksolidus_gt() throws ParseException {
        throw ex("parser.process.1", this.offset);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xpath.regex.RegexParser
    Token processStar(Token tok) throws ParseException {
        next();
        return Token.createClosure(tok);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xpath.regex.RegexParser
    Token processPlus(Token tok) throws ParseException {
        next();
        return Token.createConcat(tok, Token.createClosure(tok));
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xpath.regex.RegexParser
    Token processQuestion(Token tok) throws ParseException {
        next();
        Token par = Token.createUnion();
        par.addChild(tok);
        par.addChild(Token.createEmpty());
        return par;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xpath.regex.RegexParser
    boolean checkQuestion(int off) {
        return false;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xpath.regex.RegexParser
    Token processParen() throws ParseException {
        next();
        Token tok = Token.createParen(parseRegex(), 0);
        if (read() != 7) {
            throw ex("parser.factor.1", this.offset - 1);
        }
        next();
        return tok;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xpath.regex.RegexParser
    Token processParen2() throws ParseException {
        throw ex("parser.process.1", this.offset);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xpath.regex.RegexParser
    Token processCondition() throws ParseException {
        throw ex("parser.process.1", this.offset);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xpath.regex.RegexParser
    Token processModifiers() throws ParseException {
        throw ex("parser.process.1", this.offset);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xpath.regex.RegexParser
    Token processIndependent() throws ParseException {
        throw ex("parser.process.1", this.offset);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xpath.regex.RegexParser
    Token processBacksolidus_c() throws ParseException {
        next();
        return getTokenForShorthand(99);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xpath.regex.RegexParser
    Token processBacksolidus_C() throws ParseException {
        next();
        return getTokenForShorthand(67);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xpath.regex.RegexParser
    Token processBacksolidus_i() throws ParseException {
        next();
        return getTokenForShorthand(105);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xpath.regex.RegexParser
    Token processBacksolidus_I() throws ParseException {
        next();
        return getTokenForShorthand(73);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xpath.regex.RegexParser
    Token processBacksolidus_g() throws ParseException {
        throw ex("parser.process.1", this.offset - 2);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xpath.regex.RegexParser
    Token processBacksolidus_X() throws ParseException {
        throw ex("parser.process.1", this.offset - 2);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xpath.regex.RegexParser
    Token processBackreference() throws ParseException {
        throw ex("parser.process.1", this.offset - 4);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xpath.regex.RegexParser
    int processCIinCharacterClass(RangeToken tok, int c2) {
        tok.mergeRanges(getTokenForShorthand(c2));
        return -1;
    }

    /* JADX WARN: Code restructure failed: missing block: B:132:0x0344, code lost:
    
        if (read() != 1) goto L135;
     */
    /* JADX WARN: Code restructure failed: missing block: B:134:0x0351, code lost:
    
        throw ex("parser.cc.2", r5.offset);
     */
    /* JADX WARN: Code restructure failed: missing block: B:135:0x0352, code lost:
    
        r10.sortRanges();
        r10.compactRanges();
        setContext(0);
        next();
     */
    /* JADX WARN: Code restructure failed: missing block: B:136:0x0367, code lost:
    
        return r10;
     */
    @Override // com.sun.org.apache.xerces.internal.impl.xpath.regex.RegexParser
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected com.sun.org.apache.xerces.internal.impl.xpath.regex.RangeToken parseCharacterClass(boolean r6) throws com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException {
        /*
            Method dump skipped, instructions count: 872
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xerces.internal.impl.xpath.regex.ParserForXMLSchema.parseCharacterClass(boolean):com.sun.org.apache.xerces.internal.impl.xpath.regex.RangeToken");
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xpath.regex.RegexParser
    protected RangeToken parseSetOperations() throws ParseException {
        throw ex("parser.process.1", this.offset);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xpath.regex.RegexParser
    Token getTokenForShorthand(int ch) {
        switch (ch) {
            case 67:
                return getRange("xml:isNameChar", false);
            case 68:
                return getRange("xml:isDigit", false);
            case 73:
                return getRange("xml:isInitialNameChar", false);
            case 83:
                return getRange("xml:isSpace", false);
            case 87:
                return getRange("xml:isWord", false);
            case 99:
                return getRange("xml:isNameChar", true);
            case 100:
                return getRange("xml:isDigit", true);
            case 105:
                return getRange("xml:isInitialNameChar", true);
            case 115:
                return getRange("xml:isSpace", true);
            case 119:
                return getRange("xml:isWord", true);
            default:
                throw new RuntimeException("Internal Error: shorthands: \\u" + Integer.toString(ch, 16));
        }
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xpath.regex.RegexParser
    int decodeEscaped() throws ParseException {
        if (read() != 10) {
            throw ex("parser.next.1", this.offset - 1);
        }
        int c2 = this.chardata;
        switch (c2) {
            case 40:
            case 41:
            case 42:
            case 43:
            case 45:
            case 46:
            case 63:
            case 91:
            case 92:
            case 93:
            case 94:
            case 123:
            case 124:
            case 125:
                break;
            case 110:
                c2 = 10;
                break;
            case 114:
                c2 = 13;
                break;
            case 116:
                c2 = 9;
                break;
            default:
                throw ex("parser.process.1", this.offset - 2);
        }
        return c2;
    }

    protected static synchronized RangeToken getRange(String name, boolean positive) {
        if (ranges == null) {
            ranges = new HashMap();
            ranges2 = new HashMap();
            Token tok = Token.createRange();
            setupRange(tok, SPACES);
            ranges.put("xml:isSpace", tok);
            ranges2.put("xml:isSpace", Token.complementRanges(tok));
            Token tok2 = Token.createRange();
            setupRange(tok2, DIGITS);
            setupRange(tok2, DIGITS_INT);
            ranges.put("xml:isDigit", tok2);
            ranges2.put("xml:isDigit", Token.complementRanges(tok2));
            Token tok3 = Token.createRange();
            setupRange(tok3, LETTERS);
            setupRange(tok3, LETTERS_INT);
            tok3.mergeRanges(ranges.get("xml:isDigit"));
            ranges.put("xml:isWord", tok3);
            ranges2.put("xml:isWord", Token.complementRanges(tok3));
            Token tok4 = Token.createRange();
            setupRange(tok4, NAMECHARS);
            ranges.put("xml:isNameChar", tok4);
            ranges2.put("xml:isNameChar", Token.complementRanges(tok4));
            Token tok5 = Token.createRange();
            setupRange(tok5, LETTERS);
            tok5.addRange(95, 95);
            tok5.addRange(58, 58);
            ranges.put("xml:isInitialNameChar", tok5);
            ranges2.put("xml:isInitialNameChar", Token.complementRanges(tok5));
        }
        return positive ? (RangeToken) ranges.get(name) : (RangeToken) ranges2.get(name);
    }

    static void setupRange(Token range, String src) {
        int len = src.length();
        for (int i2 = 0; i2 < len; i2 += 2) {
            range.addRange(src.charAt(i2), src.charAt(i2 + 1));
        }
    }

    static void setupRange(Token range, int[] src) {
        int len = src.length;
        for (int i2 = 0; i2 < len; i2 += 2) {
            range.addRange(src[i2], src[i2 + 1]);
        }
    }
}
