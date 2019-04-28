package com.subtilitas.doctalk.espeakwrapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.subtilitas.doctalk.YamlConfigurationParameterResolver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(YamlConfigurationParameterResolver.class)
@ExtendWith(MockitoExtension.class)
class ESpeakParametersTest
{

    @Test
    public void testSettingParameter() {
        ESpeakParameters eSpeakParameters = ESpeakParameters
            .builder()
            .amplitude(20)
            .ipaPhonemes()
            .build();
        Assertions.assertEquals(Arrays.asList("-a 20", "--ipa"), getCommands(eSpeakParameters));
    }

    @Test
    public void testSettingPathParam() throws IOException
    {
        Path testedFile = Files.createTempFile("temp", "");
        ESpeakParameters eSpeakParameters = ESpeakParameters
            .builder()
            .textFileToSpeak(testedFile)
            .build();
        Assertions.assertEquals(Arrays.asList("-f " + testedFile.toString()), getCommands(eSpeakParameters));
    }

    @Test
    public void testStringParameter() {
        String voiceName = "pl";
        ESpeakParameters eSpeakParameters = ESpeakParameters
            .builder()
            .voiceName(voiceName)
            .build();
        Assertions.assertEquals(Arrays.asList("-v pl"), getCommands(eSpeakParameters));
    }

    @Test
    public void testAddingTwoParametersWithTheSameArgument() {
        ESpeakParameters eSpeakParameters = ESpeakParameters
            .builder()
            .voiceName("pl")
            .voiceName("en")
            .build();
        Assertions.assertEquals(Arrays.asList("-v en"), getCommands(eSpeakParameters));
    }


    private List<String> getCommands(ESpeakParameters eSpeakParameters) {
        return eSpeakParameters
            .commandStream()
            .collect(Collectors.toList());
    }
}