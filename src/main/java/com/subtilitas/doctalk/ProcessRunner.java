package com.subtilitas.doctalk;

import java.io.IOException;

public interface ProcessRunner
{
    Process run(ProcessBuilder processBuilder) throws IOException;

}
