package emdm.otus.cosmos.movement.commands.commands;

import emdm.otus.cosmos.movement.commands.Command;
import emdm.otus.cosmos.movement.move.Movable;
import emdm.otus.cosmos.movement.rotate.Rotable;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class RotateWithVelocityCommand implements Command {
    Movable movable;
    Rotable rotable;

    @Override
    public void execute() {
        Command macroCommand = new MacroCommand(List.of(
                new RotateCommand(rotable),
                new ChangeVelocityCommand(movable, rotable)));
        macroCommand.execute();
    }
}
