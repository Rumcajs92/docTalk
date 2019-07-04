package com.subtilitas.doctalk;

import com.google.common.collect.ImmutableList;
import com.subtilitas.doctalk.cmutoolkit.CommandValue;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface CommandBuilder {

    default List<String> getCommands() {
        return Stream.of(getClass().getDeclaredFields())
                .filter(this::filterNotAnnotated)
                .filter(this::filterNullFields)
                .map(this::getFieldsNameAndValueList)
                .flatMap(List::stream).collect(Collectors.toList());
    }

    default boolean filterNotAnnotated(Field field) {
        return field.getAnnotation(CommandValue.class) != null;
    }

    @SneakyThrows
    default boolean filterNullFields(Field field){
        field.setAccessible(true);
        boolean notAnnotated  = field.get(this) != null;
        field.setAccessible(false);
        return notAnnotated;
    }

    @SneakyThrows
    private List<String> getFieldsNameAndValueList(Field field)  {
        field.setAccessible(true);
        String commandPrefix = field.getAnnotation(CommandValue.class).value();
        List<String> command = ImmutableList.of(commandPrefix + field.getName(), field.get(this).toString());
        field.setAccessible(false);
        return command;
    }

}
