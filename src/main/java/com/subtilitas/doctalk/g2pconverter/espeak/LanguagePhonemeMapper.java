package com.subtilitas.doctalk.g2pconverter.espeak;

import java.nio.charset.Charset;

public interface LanguagePhonemeMapper {

    String map(String eSpeakWord);

    Charset getCharset();

}
