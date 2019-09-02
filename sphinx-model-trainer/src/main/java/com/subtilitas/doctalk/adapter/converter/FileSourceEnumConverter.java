package com.subtilitas.doctalk.adapter.converter;

import com.subtilitas.doctalk.adapter.constants.FileSourceEnum;
import org.springframework.core.convert.converter.Converter;

public class FileSourceEnumConverter implements Converter<String, FileSourceEnum> {
    @Override
    public FileSourceEnum convert(String s) {
        return FileSourceEnum.of(s);
    }
}
