package com.subtilitas.doctalk.espeakwrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@AllArgsConstructor
@RequiredArgsConstructor
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
    VOICES("--voices"),
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
    VERSION("--version"),
    WORDS("\"%s\"", 0)
    ;

    private final String argumentCharacter;

    private int order = -1;



}
