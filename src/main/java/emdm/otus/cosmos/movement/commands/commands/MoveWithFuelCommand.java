package emdm.otus.cosmos.movement.commands.commands;

import emdm.otus.cosmos.movement.commands.Command;
import emdm.otus.cosmos.movement.fuel.Fuelable;
import emdm.otus.cosmos.movement.move.Movable;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class MoveWithFuelCommand implements Command {
    Fuelable fuelable;
    Movable movable;

    @Override
    public void execute() {
        Command macroCommand = new MacroCommand(List.of(
                new CheckFuelCommand(fuelable),
                new MoveCommand(movable),
                new BurnFuelCommand(fuelable)));
        macroCommand.execute();
    }
}
