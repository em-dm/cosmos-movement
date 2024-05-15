package emdm.otus.cosmos.movement.commands.commands;

import emdm.otus.cosmos.movement.commands.Command;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class LogExceptionCommand implements Command {
    public static final String LOG_MSG = "LogExceptionCommand.execute";

    private final Exception exception;

    @Override
    public void execute() {
        log.error(LOG_MSG, exception);
    }
}
