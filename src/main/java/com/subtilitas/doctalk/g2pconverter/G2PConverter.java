package com.subtilitas.doctalk.g2pconverter;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public interface G2PConverter
{


    List<String> toPhonemes(List<String> wordsToConvert, Locale locale) throws InterruptedException, IOException;

}
