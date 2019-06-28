package com.subtilitas.doctalk.g2pconverter;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public interface G2PConverter
{
    Map<String, String> toPhonemes(List<String> wordsToConvert, Locale locale) throws InterruptedException, IOException;
}
