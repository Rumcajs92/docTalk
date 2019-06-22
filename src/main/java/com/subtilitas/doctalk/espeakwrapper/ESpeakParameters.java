package com.subtilitas.doctalk.espeakwrapper;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public final class ESpeakParameters {

    private final Map<ESpeakArgument, ESpeakParameter> parameterMap;

    private ESpeakParameters(Builder builder) {
        this.parameterMap = Collections.unmodifiableMap(builder.parameterMap);
    }

    public Map<ESpeakArgument, ESpeakParameter> getParameterMap() {
        return parameterMap;
    }

    Stream<String> commandStream() {
        return parameterMap
                .values()
                .stream()
                .sorted(Comparator.comparing(ESpeakParameter::getESpeakArgument,
                        Comparator.comparingInt(ESpeakArgument::getOrder)))
                .map(ESpeakParameter::write)
                .flatMap(Collection::stream);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Map<ESpeakArgument, ESpeakParameter> parameterMap = new EnumMap<>(ESpeakArgument.class);

        public Builder textFileToSpeak(Path path) {
            parameterMap.put(ESpeakArgument.TEXT_FILE_TO_SPEAK, new ValuedESpeakParameter<>(ESpeakArgument.TEXT_FILE_TO_SPEAK, path));
            return this;
        }

        public Builder standardInput() {
            parameterMap.put(ESpeakArgument.STANDARD_INPUT, new ESpeakParameter(ESpeakArgument.STANDARD_INPUT));
            return this;
        }

        public Builder amplitude(Integer amplitude) {
            parameterMap.put(ESpeakArgument.AMPLITUDE, new ValuedESpeakParameter<>(ESpeakArgument.AMPLITUDE, amplitude));
            return this;
        }

        public Builder wordGap(Integer wordGap) {
            parameterMap.put(ESpeakArgument.WORD_GAP, new ValuedESpeakParameter<>(ESpeakArgument.WORD_GAP, wordGap));
            return this;
        }

        public Builder capitalLetterIndication(Integer capitalLetterIndication) {
            parameterMap.put(ESpeakArgument.CAPITAL_LETTER_INDICATION, new ValuedESpeakParameter<>(ESpeakArgument.CAPITAL_LETTER_INDICATION, capitalLetterIndication));
            return this;
        }

        public Builder lineLength(Integer lineLength) {
            parameterMap.put(ESpeakArgument.LINE_LENGTH, new ValuedESpeakParameter<>(ESpeakArgument.LINE_LENGTH, lineLength));
            return this;
        }

        public Builder pitchAdjustment(Integer pitchAdjustment) {
            parameterMap.put(ESpeakArgument.PITCH_ADJUSTMENT, new ValuedESpeakParameter<>(ESpeakArgument.PITCH_ADJUSTMENT, pitchAdjustment));
            return this;
        }

        public Builder wordSpeed(Integer wordSpeed) {
            parameterMap.put(ESpeakArgument.WORD_SPEED, new ValuedESpeakParameter<>(ESpeakArgument.WORD_SPEED, wordSpeed));
            return this;
        }

        public Builder voiceName(String voiceName) {
            parameterMap.put(ESpeakArgument.VOICE_NAME, new ValuedESpeakParameter<>(ESpeakArgument.VOICE_NAME, voiceName));
            return this;
        }

        public Builder waveFile(Path waveFile) {
            parameterMap.put(ESpeakArgument.WAVE_FILE, new ValuedESpeakParameter<>(ESpeakArgument.WAVE_FILE, waveFile));
            return this;
        }

        public Builder inputTextEncoding(Integer inputTextEncoding) {
            parameterMap.put(ESpeakArgument.INPUT_TEXT_ENCODING, new ValuedESpeakParameter<>(ESpeakArgument.INPUT_TEXT_ENCODING, inputTextEncoding));
            return this;
        }

        public Builder interceptSSMLMarkup() {
            parameterMap.put(ESpeakArgument.INTERCEPT_SSML_MARKUP, new ESpeakParameter(ESpeakArgument.INTERCEPT_SSML_MARKUP));
            return this;
        }

        public Builder quiet() {
            parameterMap.put(ESpeakArgument.QUIET, new ESpeakParameter(ESpeakArgument.QUIET));
            return this;
        }

        public Builder writePhonemes() {
            parameterMap.put(ESpeakArgument.WRITE_PHONEMES, new ESpeakParameter(ESpeakArgument.WRITE_PHONEMES));
            return this;
        }

        public Builder writePhonemesAndTranslationTrace() {
            parameterMap.put(ESpeakArgument.WRITE_PHONEMES_AND_TRANSLATION_TRACE, new ESpeakParameter(ESpeakArgument.WRITE_PHONEMES_AND_TRANSLATION_TRACE));
            return this;
        }

        public Builder noFinalSentencePause() {
            parameterMap.put(ESpeakArgument.NO_FINAL_SENTENCE_PAUSE, new ESpeakParameter(ESpeakArgument.NO_FINAL_SENTENCE_PAUSE));
            return this;
        }

        public Builder compileVoice(String voiceToCompile) {
            parameterMap.put(ESpeakArgument.COMPILE_VOICE, new ValuedESpeakParameter<>(ESpeakArgument.COMPILE_VOICE, voiceToCompile));
            return this;
        }

        public Builder ipaPhonemes() {
            parameterMap.put(ESpeakArgument.IPA_PHONEMES, new ESpeakParameter(ESpeakArgument.IPA_PHONEMES));
            return this;
        }

        public Builder eSpeakDataPath(Path path) {
            parameterMap.put(ESpeakArgument.ESPEAK_DATA_PATH, new FormattedValuedESpeakParameter<>(ESpeakArgument.ESPEAK_DATA_PATH, path));
            return this;
        }

        public Builder mbrolaPhonemeData() {
            parameterMap.put(ESpeakArgument.MBROLA_PHONEME_DATA, new ESpeakParameter(ESpeakArgument.MBROLA_PHONEME_DATA));
            return this;
        }

        public Builder phonoutFile(Path phonoutFile) {
            parameterMap.put(ESpeakArgument.PHONOUT_FILE, new FormattedValuedESpeakParameter<>(ESpeakArgument.PHONOUT_FILE, phonoutFile));
            return this;
        }

        public Builder punctuation(String characters) {
            parameterMap.put(ESpeakArgument.PUNCTUATION, new FormattedValuedESpeakParameter<>(ESpeakArgument.ESPEAK_DATA_PATH, characters));
            return this;
        }

        public Builder splitFiles(Integer splitTimeout) {
            parameterMap.put(ESpeakArgument.SPLIT_FILES, new FormattedValuedESpeakParameter<>(ESpeakArgument.SPLIT_FILES, splitTimeout));
            return this;
        }

        public Builder standardOutput() {
            parameterMap.put(ESpeakArgument.STANDARD_OUTPUT, new ESpeakParameter(ESpeakArgument.STANDARD_OUTPUT));
            return this;
        }

        public Builder version() {
            parameterMap.put(ESpeakArgument.VERSION, new ESpeakParameter(ESpeakArgument.VERSION));
            return this;
        }

        public Builder words(String words) {
            parameterMap.put(ESpeakArgument.WORDS, new FormattedValuedESpeakParameter<>(ESpeakArgument.WORDS, words));
            return this;
        }

        public Builder voices() {
            parameterMap.put(ESpeakArgument.VOICES, new ESpeakParameter(ESpeakArgument.VOICES));
            return this;
        }

        public ESpeakParameters build() {
            return new ESpeakParameters(this);
        }
    }
}
