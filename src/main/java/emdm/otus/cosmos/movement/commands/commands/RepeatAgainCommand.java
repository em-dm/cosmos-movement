package emdm.otus.cosmos.movement.commands.commands;

import emdm.otus.cosmos.movement.commands.Command;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RepeatAgainCommand implements Command {
    private Command command;

    @Override
    public void execute() {
        command.execute();
    }
}
