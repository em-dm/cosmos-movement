package emdm.otus.cosmos.movement.exceptions.handlers;

import emdm.otus.cosmos.movement.commands.Command;
import emdm.otus.cosmos.movement.commands.CommandQueue;
import emdm.otus.cosmos.movement.commands.commands.LogExceptionCommand;
import emdm.otus.cosmos.movement.commands.commands.RepeatAgainCommand;
import emdm.otus.cosmos.movement.commands.commands.RepeatCommand;
import emdm.otus.cosmos.movement.exceptions.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RepeatTwiceAndLogExceptionHandler {
    public RepeatTwiceAndLogExceptionHandler(Class<? extends Command> commandClass,
                                             Class<? extends Exception> exceptionClass) {

        ExceptionHandler.registerHandler(commandClass, exceptionClass,
                (command, exception) -> () -> CommandQueue.add(new RepeatCommand(command)));
        ExceptionHandler.registerHandler(RepeatCommand.class, exceptionClass,
                (command, exception) -> () -> CommandQueue.add(new RepeatAgainCommand(command)));
        ExceptionHandler.registerHandler(RepeatAgainCommand.class, exceptionClass,
                (command, exception) -> () -> CommandQueue.add(new LogExceptionCommand(exception)));
    }
}
