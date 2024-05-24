package emdm.otus.cosmos.movement.commands.commands;

import emdm.otus.cosmos.movement.commands.Command;
import emdm.otus.cosmos.movement.exceptions.CommandException;
import emdm.otus.cosmos.movement.fuel.Fuelable;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CheckFuelCommand implements Command {
    public static final String CHECK_FUEL_EXCEPTION_MESSAGE = "CheckFuelCommand: Not enough fuel";

    private Fuelable fuelable;

    @Override
    public void execute() {
        if (fuelable.getCurrentFuel() < fuelable.getFuelForAction()) {
            throw new CommandException(CHECK_FUEL_EXCEPTION_MESSAGE);
        }
    }
}
