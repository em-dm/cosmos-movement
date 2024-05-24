package emdm.otus.cosmos.movement.exceptions;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import emdm.otus.cosmos.movement.commands.Command;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BiFunction;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionHandler {
    private static final Table<String, String, BiFunction<Command, Exception, Command>> dictionary = HashBasedTable.create();

    public static Command handle(Command command, Exception exception) {
        return Optional.ofNullable(
                        dictionary.get(command.getClass().getName(), exception.getClass().getName()))
                .orElseThrow(() ->
                        new NoSuchElementException(String.format("%s %s", command.getClass().getName(), exception.getClass().getName())))
                .apply(command, exception);
    }

    public static void registerHandler(Class<? extends Command> commandClass,
                                       Class<? extends Exception> exceptionClass,
                                       BiFunction<Command, Exception, Command> function) {
        dictionary.put(commandClass.getName(), exceptionClass.getName(), function);
    }
}
