package com.subtilitas.doctalk.wordextractor;

import java.io.IOException;
import java.nio.file.Path;

public interface UniqueWordExtractor
{

    Path extractUniqueWords(Path path) throws IOException;

}
