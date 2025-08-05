package sun.nio.cs;

import java.nio.charset.Charset;
import net.lingala.zip4j.util.InternalZipConstants;
import sun.util.PreHashedMap;

/* loaded from: rt.jar:sun/nio/cs/StandardCharsets.class */
public class StandardCharsets extends FastCharsetProvider {
    static final String[] aliases_US_ASCII = {"iso-ir-6", "ANSI_X3.4-1986", "ISO_646.irv:1991", "ASCII", "ISO646-US", "us", "IBM367", "cp367", "csASCII", "default", "646", "iso_646.irv:1983", "ANSI_X3.4-1968", "ascii7"};
    static final String[] aliases_UTF_8 = {InternalZipConstants.CHARSET_UTF8, "unicode-1-1-utf-8"};
    static final String[] aliases_CESU_8 = {"CESU8", "csCESU-8"};
    static final String[] aliases_UTF_16 = {"UTF_16", "utf16", "unicode", "UnicodeBig"};
    static final String[] aliases_UTF_16BE = {"UTF_16BE", "ISO-10646-UCS-2", "X-UTF-16BE", "UnicodeBigUnmarked"};
    static final String[] aliases_UTF_16LE = {"UTF_16LE", "X-UTF-16LE", "UnicodeLittleUnmarked"};
    static final String[] aliases_UTF_16LE_BOM = {"UnicodeLittle"};
    static final String[] aliases_UTF_32 = {"UTF_32", "UTF32"};
    static final String[] aliases_UTF_32LE = {"UTF_32LE", "X-UTF-32LE"};
    static final String[] aliases_UTF_32BE = {"UTF_32BE", "X-UTF-32BE"};
    static final String[] aliases_UTF_32LE_BOM = {"UTF_32LE_BOM", "UTF-32LE-BOM"};
    static final String[] aliases_UTF_32BE_BOM = {"UTF_32BE_BOM", "UTF-32BE-BOM"};
    static final String[] aliases_ISO_8859_1 = {"iso-ir-100", "ISO_8859-1", "latin1", "l1", "IBM819", "cp819", "csISOLatin1", "819", "IBM-819", "ISO8859_1", "ISO_8859-1:1987", "ISO_8859_1", "8859_1", "ISO8859-1"};
    static final String[] aliases_ISO_8859_2 = {"iso8859_2", "8859_2", "iso-ir-101", "ISO_8859-2", "ISO_8859-2:1987", "ISO8859-2", "latin2", "l2", "ibm912", "ibm-912", "cp912", "912", "csISOLatin2"};
    static final String[] aliases_ISO_8859_4 = {"iso8859_4", "iso8859-4", "8859_4", "iso-ir-110", "ISO_8859-4", "ISO_8859-4:1988", "latin4", "l4", "ibm914", "ibm-914", "cp914", "914", "csISOLatin4"};
    static final String[] aliases_ISO_8859_5 = {"iso8859_5", "8859_5", "iso-ir-144", "ISO_8859-5", "ISO_8859-5:1988", "ISO8859-5", "cyrillic", "ibm915", "ibm-915", "cp915", "915", "csISOLatinCyrillic"};
    static final String[] aliases_ISO_8859_7 = {"iso8859_7", "8859_7", "iso-ir-126", "ISO_8859-7", "ISO_8859-7:1987", "ELOT_928", "ECMA-118", "greek", "greek8", "csISOLatinGreek", "sun_eu_greek", "ibm813", "ibm-813", "813", "cp813", "iso8859-7"};
    static final String[] aliases_ISO_8859_9 = {"iso8859_9", "8859_9", "iso-ir-148", "ISO_8859-9", "ISO_8859-9:1989", "ISO8859-9", "latin5", "l5", "ibm920", "ibm-920", "920", "cp920", "csISOLatin5"};
    static final String[] aliases_ISO_8859_13 = {"iso8859_13", "8859_13", "iso_8859-13", "ISO8859-13"};
    static final String[] aliases_ISO_8859_15 = {"ISO_8859-15", "8859_15", "ISO-8859-15", "ISO8859_15", "ISO8859-15", "IBM923", "IBM-923", "cp923", "923", "LATIN0", "LATIN9", "L9", "csISOlatin0", "csISOlatin9", "ISO8859_15_FDIS"};
    static final String[] aliases_KOI8_R = {"koi8_r", "koi8", "cskoi8r"};
    static final String[] aliases_KOI8_U = {"koi8_u"};
    static final String[] aliases_MS1250 = {"cp1250", "cp5346"};
    static final String[] aliases_MS1251 = {"cp1251", "cp5347", "ansi-1251"};
    static final String[] aliases_MS1252 = {"cp1252", "cp5348"};
    static final String[] aliases_MS1253 = {"cp1253", "cp5349"};
    static final String[] aliases_MS1254 = {"cp1254", "cp5350"};
    static final String[] aliases_MS1257 = {"cp1257", "cp5353"};
    static final String[] aliases_IBM437 = {"cp437", "ibm437", "ibm-437", "437", "cspc8codepage437", "windows-437"};
    static final String[] aliases_IBM737 = {"cp737", "ibm737", "ibm-737", "737"};
    static final String[] aliases_IBM775 = {"cp775", "ibm775", "ibm-775", "775"};
    static final String[] aliases_IBM850 = {"cp850", "ibm-850", "ibm850", "850", "cspc850multilingual"};
    static final String[] aliases_IBM852 = {"cp852", "ibm852", "ibm-852", "852", "csPCp852"};
    static final String[] aliases_IBM855 = {"cp855", "ibm-855", "ibm855", "855", "cspcp855"};
    static final String[] aliases_IBM857 = {"cp857", "ibm857", "ibm-857", "857", "csIBM857"};
    static final String[] aliases_IBM858 = {"cp858", "ccsid00858", "cp00858", "858", "PC-Multilingual-850+euro"};
    static final String[] aliases_IBM862 = {"cp862", "ibm862", "ibm-862", "862", "csIBM862", "cspc862latinhebrew"};
    static final String[] aliases_IBM866 = {"cp866", "ibm866", "ibm-866", "866", "csIBM866"};
    static final String[] aliases_IBM874 = {"cp874", "ibm874", "ibm-874", "874"};

    /* loaded from: rt.jar:sun/nio/cs/StandardCharsets$Aliases.class */
    private static final class Aliases extends PreHashedMap<String> {
        private static final int ROWS = 1024;
        private static final int SIZE = 211;
        private static final int SHIFT = 0;
        private static final int MASK = 1023;

        private Aliases() {
            super(1024, 211, 0, 1023);
        }

        @Override // sun.util.PreHashedMap
        protected void init(Object[] objArr) {
            objArr[1] = new Object[]{"csisolatin0", "iso-8859-15"};
            objArr[2] = new Object[]{"csisolatin1", "iso-8859-1"};
            objArr[3] = new Object[]{"csisolatin2", "iso-8859-2"};
            objArr[5] = new Object[]{"csisolatin4", "iso-8859-4"};
            objArr[6] = new Object[]{"csisolatin5", "iso-8859-9"};
            objArr[10] = new Object[]{"csisolatin9", "iso-8859-15"};
            objArr[19] = new Object[]{"unicodelittle", "x-utf-16le-bom"};
            objArr[24] = new Object[]{"iso646-us", "us-ascii"};
            objArr[25] = new Object[]{"iso_8859-7:1987", "iso-8859-7"};
            objArr[26] = new Object[]{"912", "iso-8859-2"};
            objArr[28] = new Object[]{"914", "iso-8859-4"};
            objArr[29] = new Object[]{"915", "iso-8859-5"};
            objArr[55] = new Object[]{"920", "iso-8859-9"};
            objArr[58] = new Object[]{"923", "iso-8859-15"};
            objArr[86] = new Object[]{"csisolatincyrillic", "iso-8859-5", new Object[]{"8859_1", "iso-8859-1"}};
            objArr[87] = new Object[]{"8859_2", "iso-8859-2"};
            objArr[89] = new Object[]{"8859_4", "iso-8859-4"};
            objArr[90] = new Object[]{"813", "iso-8859-7", new Object[]{"8859_5", "iso-8859-5"}};
            objArr[92] = new Object[]{"8859_7", "iso-8859-7"};
            objArr[94] = new Object[]{"8859_9", "iso-8859-9"};
            objArr[95] = new Object[]{"iso_8859-1:1987", "iso-8859-1"};
            objArr[96] = new Object[]{"819", "iso-8859-1"};
            objArr[106] = new Object[]{"unicode-1-1-utf-8", "utf-8"};
            objArr[121] = new Object[]{"x-utf-16le", "utf-16le"};
            objArr[125] = new Object[]{"ecma-118", "iso-8859-7"};
            objArr[134] = new Object[]{"koi8_r", "koi8-r"};
            objArr[137] = new Object[]{"koi8_u", "koi8-u"};
            objArr[141] = new Object[]{"cp912", "iso-8859-2"};
            objArr[143] = new Object[]{"cp914", "iso-8859-4"};
            objArr[144] = new Object[]{"cp915", "iso-8859-5"};
            objArr[170] = new Object[]{"cp920", "iso-8859-9"};
            objArr[173] = new Object[]{"cp923", "iso-8859-15"};
            objArr[177] = new Object[]{"utf_32le_bom", "x-utf-32le-bom"};
            objArr[192] = new Object[]{"utf_16be", "utf-16be"};
            objArr[199] = new Object[]{"cspc8codepage437", "ibm437", new Object[]{"ansi-1251", "windows-1251"}};
            objArr[205] = new Object[]{"cp813", "iso-8859-7"};
            objArr[211] = new Object[]{"850", "ibm850", new Object[]{"cp819", "iso-8859-1"}};
            objArr[213] = new Object[]{"852", "ibm852"};
            objArr[216] = new Object[]{"855", "ibm855"};
            objArr[218] = new Object[]{"857", "ibm857", new Object[]{"iso-ir-6", "us-ascii"}};
            objArr[219] = new Object[]{"858", "ibm00858", new Object[]{"737", "x-ibm737"}};
            objArr[225] = new Object[]{"csascii", "us-ascii"};
            objArr[244] = new Object[]{"862", "ibm862"};
            objArr[248] = new Object[]{"866", "ibm866"};
            objArr[253] = new Object[]{"x-utf-32be", "utf-32be"};
            objArr[254] = new Object[]{"iso_8859-2:1987", "iso-8859-2"};
            objArr[259] = new Object[]{"unicodebig", "utf-16"};
            objArr[269] = new Object[]{"iso8859_15_fdis", "iso-8859-15"};
            objArr[277] = new Object[]{"874", "x-ibm874"};
            objArr[280] = new Object[]{"unicodelittleunmarked", "utf-16le"};
            objArr[283] = new Object[]{"iso8859_1", "iso-8859-1"};
            objArr[284] = new Object[]{"iso8859_2", "iso-8859-2"};
            objArr[286] = new Object[]{"iso8859_4", "iso-8859-4"};
            objArr[287] = new Object[]{"iso8859_5", "iso-8859-5"};
            objArr[289] = new Object[]{"iso8859_7", "iso-8859-7"};
            objArr[291] = new Object[]{"iso8859_9", "iso-8859-9"};
            objArr[294] = new Object[]{"ibm912", "iso-8859-2"};
            objArr[296] = new Object[]{"ibm914", "iso-8859-4"};
            objArr[297] = new Object[]{"ibm915", "iso-8859-5"};
            objArr[305] = new Object[]{"iso_8859-13", "iso-8859-13"};
            objArr[307] = new Object[]{"iso_8859-15", "iso-8859-15"};
            objArr[312] = new Object[]{"greek8", "iso-8859-7", new Object[]{"646", "us-ascii"}};
            objArr[321] = new Object[]{"ibm-912", "iso-8859-2"};
            objArr[323] = new Object[]{"ibm920", "iso-8859-9", new Object[]{"ibm-914", "iso-8859-4"}};
            objArr[324] = new Object[]{"ibm-915", "iso-8859-5"};
            objArr[325] = new Object[]{"l1", "iso-8859-1"};
            objArr[326] = new Object[]{"cp850", "ibm850", new Object[]{"ibm923", "iso-8859-15", new Object[]{"l2", "iso-8859-2"}}};
            objArr[327] = new Object[]{"cyrillic", "iso-8859-5"};
            objArr[328] = new Object[]{"cp852", "ibm852", new Object[]{"l4", "iso-8859-4"}};
            objArr[329] = new Object[]{"l5", "iso-8859-9"};
            objArr[331] = new Object[]{"cp855", "ibm855"};
            objArr[333] = new Object[]{"cp857", "ibm857", new Object[]{"l9", "iso-8859-15"}};
            objArr[334] = new Object[]{"cp858", "ibm00858", new Object[]{"cp737", "x-ibm737"}};
            objArr[336] = new Object[]{"iso_8859_1", "iso-8859-1"};
            objArr[339] = new Object[]{"koi8", "koi8-r"};
            objArr[341] = new Object[]{"775", "ibm775"};
            objArr[345] = new Object[]{"iso_8859-9:1989", "iso-8859-9"};
            objArr[350] = new Object[]{"ibm-920", "iso-8859-9"};
            objArr[353] = new Object[]{"ibm-923", "iso-8859-15"};
            objArr[358] = new Object[]{"ibm813", "iso-8859-7"};
            objArr[359] = new Object[]{"cp862", "ibm862"};
            objArr[363] = new Object[]{"cp866", "ibm866"};
            objArr[364] = new Object[]{"ibm819", "iso-8859-1"};
            objArr[378] = new Object[]{"ansi_x3.4-1968", "us-ascii"};
            objArr[385] = new Object[]{"ibm-813", "iso-8859-7"};
            objArr[391] = new Object[]{"ibm-819", "iso-8859-1"};
            objArr[392] = new Object[]{"cp874", "x-ibm874"};
            objArr[405] = new Object[]{"iso-ir-100", "iso-8859-1"};
            objArr[406] = new Object[]{"iso-ir-101", "iso-8859-2"};
            objArr[408] = new Object[]{"437", "ibm437"};
            objArr[421] = new Object[]{"iso-8859-15", "iso-8859-15"};
            objArr[428] = new Object[]{"latin0", "iso-8859-15"};
            objArr[429] = new Object[]{"latin1", "iso-8859-1"};
            objArr[430] = new Object[]{"latin2", "iso-8859-2"};
            objArr[432] = new Object[]{"latin4", "iso-8859-4"};
            objArr[433] = new Object[]{"latin5", "iso-8859-9"};
            objArr[436] = new Object[]{"iso-ir-110", "iso-8859-4"};
            objArr[437] = new Object[]{"latin9", "iso-8859-15"};
            objArr[438] = new Object[]{"ansi_x3.4-1986", "us-ascii"};
            objArr[443] = new Object[]{"utf-32be-bom", "x-utf-32be-bom"};
            objArr[456] = new Object[]{"cp775", "ibm775"};
            objArr[473] = new Object[]{"iso-ir-126", "iso-8859-7"};
            objArr[479] = new Object[]{"ibm850", "ibm850"};
            objArr[481] = new Object[]{"ibm852", "ibm852"};
            objArr[484] = new Object[]{"ibm855", "ibm855"};
            objArr[486] = new Object[]{"ibm857", "ibm857"};
            objArr[487] = new Object[]{"ibm737", "x-ibm737"};
            objArr[502] = new Object[]{"utf_16le", "utf-16le"};
            objArr[506] = new Object[]{"ibm-850", "ibm850"};
            objArr[508] = new Object[]{"ibm-852", "ibm852"};
            objArr[511] = new Object[]{"ibm-855", "ibm855"};
            objArr[512] = new Object[]{"ibm862", "ibm862"};
            objArr[513] = new Object[]{"ibm-857", "ibm857"};
            objArr[514] = new Object[]{"ibm-737", "x-ibm737"};
            objArr[516] = new Object[]{"ibm866", "ibm866"};
            objArr[520] = new Object[]{"unicodebigunmarked", "utf-16be"};
            objArr[523] = new Object[]{"cp437", "ibm437"};
            objArr[524] = new Object[]{"utf16", "utf-16"};
            objArr[533] = new Object[]{"iso-ir-144", "iso-8859-5"};
            objArr[537] = new Object[]{"iso-ir-148", "iso-8859-9"};
            objArr[539] = new Object[]{"ibm-862", "ibm862"};
            objArr[543] = new Object[]{"ibm-866", "ibm866"};
            objArr[545] = new Object[]{"ibm874", "x-ibm874"};
            objArr[563] = new Object[]{"x-utf-32le", "utf-32le"};
            objArr[572] = new Object[]{"ibm-874", "x-ibm874"};
            objArr[573] = new Object[]{"iso_8859-4:1988", "iso-8859-4"};
            objArr[577] = new Object[]{"default", "us-ascii"};
            objArr[582] = new Object[]{"utf32", "utf-32"};
            objArr[583] = new Object[]{"pc-multilingual-850+euro", "ibm00858"};
            objArr[588] = new Object[]{"elot_928", "iso-8859-7"};
            objArr[593] = new Object[]{"csisolatingreek", "iso-8859-7"};
            objArr[598] = new Object[]{"csibm857", "ibm857"};
            objArr[609] = new Object[]{"ibm775", "ibm775"};
            objArr[617] = new Object[]{"cp1250", "windows-1250"};
            objArr[618] = new Object[]{"cp1251", "windows-1251"};
            objArr[619] = new Object[]{"cp1252", "windows-1252"};
            objArr[620] = new Object[]{"cp1253", "windows-1253"};
            objArr[621] = new Object[]{"cp1254", InternalZipConstants.CHARSET_COMMENTS_DEFAULT};
            objArr[624] = new Object[]{"csibm862", "ibm862", new Object[]{"cp1257", "windows-1257"}};
            objArr[628] = new Object[]{"csibm866", "ibm866", new Object[]{"cesu8", "cesu-8"}};
            objArr[632] = new Object[]{"iso8859_13", "iso-8859-13"};
            objArr[634] = new Object[]{"iso8859_15", "iso-8859-15", new Object[]{"utf_32be", "utf-32be"}};
            objArr[635] = new Object[]{"utf_32be_bom", "x-utf-32be-bom"};
            objArr[636] = new Object[]{"ibm-775", "ibm775"};
            objArr[654] = new Object[]{"cp00858", "ibm00858"};
            objArr[669] = new Object[]{"8859_13", "iso-8859-13"};
            objArr[670] = new Object[]{"us", "us-ascii"};
            objArr[671] = new Object[]{"8859_15", "iso-8859-15"};
            objArr[676] = new Object[]{"ibm437", "ibm437"};
            objArr[679] = new Object[]{"cp367", "us-ascii"};
            objArr[686] = new Object[]{"iso-10646-ucs-2", "utf-16be"};
            objArr[703] = new Object[]{"ibm-437", "ibm437"};
            objArr[710] = new Object[]{"iso8859-13", "iso-8859-13"};
            objArr[712] = new Object[]{"iso8859-15", "iso-8859-15"};
            objArr[732] = new Object[]{"iso_8859-5:1988", "iso-8859-5"};
            objArr[733] = new Object[]{"unicode", "utf-16"};
            objArr[768] = new Object[]{"greek", "iso-8859-7"};
            objArr[774] = new Object[]{"ascii7", "us-ascii"};
            objArr[781] = new Object[]{"iso8859-1", "iso-8859-1"};
            objArr[782] = new Object[]{"iso8859-2", "iso-8859-2"};
            objArr[783] = new Object[]{"cskoi8r", "koi8-r"};
            objArr[784] = new Object[]{"iso8859-4", "iso-8859-4"};
            objArr[785] = new Object[]{"iso8859-5", "iso-8859-5"};
            objArr[787] = new Object[]{"iso8859-7", "iso-8859-7"};
            objArr[789] = new Object[]{"iso8859-9", "iso-8859-9"};
            objArr[813] = new Object[]{"ccsid00858", "ibm00858"};
            objArr[818] = new Object[]{"cspc862latinhebrew", "ibm862"};
            objArr[832] = new Object[]{"ibm367", "us-ascii"};
            objArr[834] = new Object[]{"iso_8859-1", "iso-8859-1"};
            objArr[835] = new Object[]{"iso_8859-2", "iso-8859-2", new Object[]{"x-utf-16be", "utf-16be"}};
            objArr[836] = new Object[]{"sun_eu_greek", "iso-8859-7"};
            objArr[837] = new Object[]{"iso_8859-4", "iso-8859-4"};
            objArr[838] = new Object[]{"iso_8859-5", "iso-8859-5"};
            objArr[840] = new Object[]{"cspcp852", "ibm852", new Object[]{"iso_8859-7", "iso-8859-7"}};
            objArr[842] = new Object[]{"iso_8859-9", "iso-8859-9"};
            objArr[843] = new Object[]{"cspcp855", "ibm855"};
            objArr[846] = new Object[]{"windows-437", "ibm437"};
            objArr[849] = new Object[]{"ascii", "us-ascii"};
            objArr[863] = new Object[]{"cscesu-8", "cesu-8"};
            objArr[881] = new Object[]{"utf8", "utf-8"};
            objArr[896] = new Object[]{"iso_646.irv:1983", "us-ascii"};
            objArr[909] = new Object[]{"cp5346", "windows-1250"};
            objArr[910] = new Object[]{"cp5347", "windows-1251"};
            objArr[911] = new Object[]{"cp5348", "windows-1252"};
            objArr[912] = new Object[]{"cp5349", "windows-1253"};
            objArr[925] = new Object[]{"iso_646.irv:1991", "us-ascii"};
            objArr[934] = new Object[]{"cp5350", InternalZipConstants.CHARSET_COMMENTS_DEFAULT};
            objArr[937] = new Object[]{"cp5353", "windows-1257"};
            objArr[944] = new Object[]{"utf_32le", "utf-32le"};
            objArr[957] = new Object[]{"utf_16", "utf-16"};
            objArr[993] = new Object[]{"cspc850multilingual", "ibm850"};
            objArr[1009] = new Object[]{"utf-32le-bom", "x-utf-32le-bom"};
            objArr[1015] = new Object[]{"utf_32", "utf-32"};
        }
    }

    /* loaded from: rt.jar:sun/nio/cs/StandardCharsets$Classes.class */
    private static final class Classes extends PreHashedMap<String> {
        private static final int ROWS = 32;
        private static final int SIZE = 39;
        private static final int SHIFT = 1;
        private static final int MASK = 31;

        private Classes() {
            super(32, 39, 1, 31);
        }

        @Override // sun.util.PreHashedMap
        protected void init(Object[] objArr) {
            objArr[0] = new Object[]{"ibm862", "IBM862"};
            objArr[2] = new Object[]{"ibm866", "IBM866", new Object[]{"utf-32", "UTF_32", new Object[]{"utf-16le", "UTF_16LE"}}};
            objArr[3] = new Object[]{"windows-1251", "MS1251", new Object[]{"windows-1250", "MS1250"}};
            objArr[4] = new Object[]{"windows-1253", "MS1253", new Object[]{"windows-1252", "MS1252", new Object[]{"utf-32be", "UTF_32BE"}}};
            objArr[5] = new Object[]{InternalZipConstants.CHARSET_COMMENTS_DEFAULT, "MS1254", new Object[]{"utf-16", "UTF_16"}};
            objArr[6] = new Object[]{"windows-1257", "MS1257"};
            objArr[7] = new Object[]{"utf-16be", "UTF_16BE"};
            objArr[8] = new Object[]{"iso-8859-2", "ISO_8859_2", new Object[]{"iso-8859-1", "ISO_8859_1"}};
            objArr[9] = new Object[]{"iso-8859-4", "ISO_8859_4", new Object[]{"utf-8", "UTF_8"}};
            objArr[10] = new Object[]{"iso-8859-5", "ISO_8859_5"};
            objArr[11] = new Object[]{"x-ibm874", "IBM874", new Object[]{"iso-8859-7", "ISO_8859_7"}};
            objArr[12] = new Object[]{"iso-8859-9", "ISO_8859_9"};
            objArr[14] = new Object[]{"x-ibm737", "IBM737"};
            objArr[15] = new Object[]{"ibm850", "IBM850"};
            objArr[16] = new Object[]{"ibm852", "IBM852", new Object[]{"ibm775", "IBM775"}};
            objArr[17] = new Object[]{"iso-8859-13", "ISO_8859_13", new Object[]{"us-ascii", "US_ASCII"}};
            objArr[18] = new Object[]{"ibm855", "IBM855", new Object[]{"ibm437", "IBM437", new Object[]{"iso-8859-15", "ISO_8859_15"}}};
            objArr[19] = new Object[]{"ibm00858", "IBM858", new Object[]{"ibm857", "IBM857", new Object[]{"x-utf-32le-bom", "UTF_32LE_BOM"}}};
            objArr[22] = new Object[]{"x-utf-16le-bom", "UTF_16LE_BOM"};
            objArr[23] = new Object[]{"cesu-8", "CESU_8"};
            objArr[24] = new Object[]{"x-utf-32be-bom", "UTF_32BE_BOM"};
            objArr[28] = new Object[]{"koi8-r", "KOI8_R"};
            objArr[29] = new Object[]{"koi8-u", "KOI8_U"};
            objArr[31] = new Object[]{"utf-32le", "UTF_32LE"};
        }
    }

    /* loaded from: rt.jar:sun/nio/cs/StandardCharsets$Cache.class */
    private static final class Cache extends PreHashedMap<Charset> {
        private static final int ROWS = 32;
        private static final int SIZE = 39;
        private static final int SHIFT = 1;
        private static final int MASK = 31;

        private Cache() {
            super(32, 39, 1, 31);
        }

        @Override // sun.util.PreHashedMap
        protected void init(Object[] objArr) {
            objArr[0] = new Object[]{"ibm862", null};
            objArr[2] = new Object[]{"ibm866", null, new Object[]{"utf-32", null, new Object[]{"utf-16le", null}}};
            objArr[3] = new Object[]{"windows-1251", null, new Object[]{"windows-1250", null}};
            objArr[4] = new Object[]{"windows-1253", null, new Object[]{"windows-1252", null, new Object[]{"utf-32be", null}}};
            objArr[5] = new Object[]{InternalZipConstants.CHARSET_COMMENTS_DEFAULT, null, new Object[]{"utf-16", null}};
            objArr[6] = new Object[]{"windows-1257", null};
            objArr[7] = new Object[]{"utf-16be", null};
            objArr[8] = new Object[]{"iso-8859-2", null, new Object[]{"iso-8859-1", null}};
            objArr[9] = new Object[]{"iso-8859-4", null, new Object[]{"utf-8", null}};
            objArr[10] = new Object[]{"iso-8859-5", null};
            objArr[11] = new Object[]{"x-ibm874", null, new Object[]{"iso-8859-7", null}};
            objArr[12] = new Object[]{"iso-8859-9", null};
            objArr[14] = new Object[]{"x-ibm737", null};
            objArr[15] = new Object[]{"ibm850", null};
            objArr[16] = new Object[]{"ibm852", null, new Object[]{"ibm775", null}};
            objArr[17] = new Object[]{"iso-8859-13", null, new Object[]{"us-ascii", null}};
            objArr[18] = new Object[]{"ibm855", null, new Object[]{"ibm437", null, new Object[]{"iso-8859-15", null}}};
            objArr[19] = new Object[]{"ibm00858", null, new Object[]{"ibm857", null, new Object[]{"x-utf-32le-bom", null}}};
            objArr[22] = new Object[]{"x-utf-16le-bom", null};
            objArr[23] = new Object[]{"cesu-8", null};
            objArr[24] = new Object[]{"x-utf-32be-bom", null};
            objArr[28] = new Object[]{"koi8-r", null};
            objArr[29] = new Object[]{"koi8-u", null};
            objArr[31] = new Object[]{"utf-32le", null};
        }
    }

    public StandardCharsets() {
        super("sun.nio.cs", new Aliases(), new Classes(), new Cache());
    }
}
