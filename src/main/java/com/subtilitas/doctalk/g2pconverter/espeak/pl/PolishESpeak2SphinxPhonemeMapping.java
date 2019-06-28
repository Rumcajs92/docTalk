package com.subtilitas.doctalk.g2pconverter.espeak.pl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public enum PolishESpeak2SphinxPhonemeMapping {

    _i("i", "i"),
    _y("y", "y"),
    _E("E", "e"),
    _E_HASH("E#", "e"),
    _a("a", "a"),
    _O("O", "o"),
    _u("u", "u"),
    _E_TILDE("E~", "en"),
    _E_n("En", "en"),
    _E_n_CARET("En^", "en"),
    _O_TILDE("O~", "on"),
    _p("p", "p"),
    _p_SEMICOLON("p;", "p j"),
    _b("b", "b"),
    _b_SEMICOLON("b;", "b"),
    _t("t", "t"),
    _t_SEMICOLON("t;", "t"),
    _d("d", "d"),
    _d_SEMICOLON("d;", "d"),
    _k("k", "k"),
    _k_SEMICOLON("k;", "ki"),
    _g("g", "g"),
    _g_SEMICOLON("g;", "gi"),
    _f("f", "f"),
    _f_SEMICOLON("f;", "f"),
    _v("v", "v"),
    _v_SEMICOLON("v;", "v j"),
    _s("s", "s"),
    _z("z", "z"),
    _S("S", "sh"),
    _Z("Z", "zh"),
    _S_SEMICOLON("S;", "si"),
    _Z_SEMICOLON("Z;", "zi"),
    _x("x", "h"),
    _C("C", "h"),
    _h("h", "h"),
    _ts("ts", "ts"),
    _dz("dz", "dz"),
    _tS("tS", "cz"),
    _dZ("dZ", "dh"),
    _ts_SEMICOLON("ts;", "ci"),
    _dz_SEMICOLON("dz;", "di"),
    _m_("m", "m"),
    _m_SEMICOLON("m;", "m"),
    _n("n", "n"),
    _n_CARET_("n^", "ni"),
    _n_CARET_SEMICOLON("n^;", "ni"),
    _N("N", "ng"),
    _l("l", "l"),
    _R("R", "r"),
    _w("w", "w"),
    _j("j", "j"),

    _APOSTROPHE("'", "", false),
    _COMMA(",", "", false),
    _PERCENTILE(",", "", false),
    _EQUALS("=", "",false),
    _UNDERSCORE_COLON("_:", "", false),
    _UNDERSCORE("_", "", false),
    _DOUBLE_PIPE("||", "", false),
    _PIPE("|", "", false),
    _HASH_UNDERSCORE("#_", "", false),
    _HASH("#", "", false),
    ;
    private final String eSpeakSign;

    private final String sphinxPhoneme;

    private boolean isPhoneme = true;


    public int getESpeakSignLength() {
        return eSpeakSign.length();
    }

    static final List<PolishESpeak2SphinxPhonemeMapping> LENGTH_SORTED_E_SPEAK_MAPPINGS = getLengthSortedESpeakMappings();

    static final int MAX_MAPPING_KEY = getMaxMappingKey();

    private static List<PolishESpeak2SphinxPhonemeMapping> getLengthSortedESpeakMappings() {
        return ImmutableList.copyOf(Stream.of(PolishESpeak2SphinxPhonemeMapping.values())
                .sorted((o1, o2) -> o2.getESpeakSignLength() - o1.getESpeakSignLength())
                .collect(Collectors.toList()));
    }

    private static int getMaxMappingKey() {
        return LENGTH_SORTED_E_SPEAK_MAPPINGS
                .stream()
                .mapToInt(PolishESpeak2SphinxPhonemeMapping::getESpeakSignLength)
                .max()
                .orElse(0);
    }

}