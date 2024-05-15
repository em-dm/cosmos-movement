package emdm.otus.cosmos.movement.exceptions.handlers;

import emdm.otus.cosmos.movement.commands.Command;
import emdm.otus.cosmos.movement.commands.CommandQueue;
import emdm.otus.cosmos.movement.commands.commands.LogExceptionCommand;
import emdm.otus.cosmos.movement.exceptions.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogExceptionHandler {
    public LogExceptionHandler(Class<? extends Command> commandClass,
                               Class<? extends Exception> exceptionClass) {

        ExceptionHandler.registerHandler(commandClass, exceptionClass,
                (command, exception) -> () -> CommandQueue.add(new LogExceptionCommand(exception)));
    }
}
