package com.subtilitas.doctalk.espeakwrapper;

enum ESpeakArgument
{
    TEXT_FILE_TO_SPEAK("-f"),
    STANDARD_INPUT("--stdin"),
    AMPLITUDE("-a"),
    WORD_GAP("-g"),
    CAPITAL_LETTER_INDICATION("-k"),
    LINE_LENGTH("-l"),
    PITCH_ADJUSTMENT("-p"),
    WORD_SPEED("-s"),
    VOICE_NAME("-v"),
    WAVE_FILE("-w"),
    INPUT_TEXT_ENCODING("-b"),
    INTERCEPT_SSML_MARKUP("-m"),
    QUIET("-q"),
    WRITE_PHONEMES("-x"),
    WRITE_PHONEMES_AND_TRANSLATION_TRACE("-X"),
    NO_FINAL_SENTENCE_PAUSE("-z"),
    COMPILE_VOICE("--compile"),
    IPA_PHONEMES("--ipa"),
    ESPEAK_DATA_PATH("--path=\"%s\""),
    MBROLA_PHONEME_DATA("--pho"),
    PHONOUT_FILE("--phonout=\"%s\""),
    PUNCTUATION("--punct=\"%s\""),
    SPLIT_FILES("--split=\"%s\""),
    STANDARD_OUTPUT("--stdout"),
    VERSION("--version")
    ;

    private final String argumentCharacter;

    ESpeakArgument(String argumentCharacter)
    {
        this.argumentCharacter = argumentCharacter;
    }

    String getArgumentCharacter()
    {
        return argumentCharacter;
    }
}
