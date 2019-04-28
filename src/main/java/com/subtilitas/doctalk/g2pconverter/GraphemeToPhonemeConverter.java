package com.subtilitas.doctalk.g2pconverter;

import java.nio.file.Path;

public interface GraphemeToPhonemeConverter
{

    Path convertToPhonemes(Path pathToConvert);

}
