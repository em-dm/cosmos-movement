package emdm.otus.cosmos.movement.commands.commands;

import emdm.otus.cosmos.movement.commands.Command;
import emdm.otus.cosmos.movement.exceptions.CommandException;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class MacroCommand implements Command {
    List<Command> commands;

    @Override
    public void execute() {
        try {
            commands.forEach(Command::execute);
        } catch (Exception exception) {
            throw new CommandException(exception);
        }
    }
}
