package emdm.otus.cosmos.movement.commands.commands;

import emdm.otus.cosmos.movement.commands.Command;
import emdm.otus.cosmos.movement.exceptions.CommandException;
import emdm.otus.cosmos.movement.fuel.Fuelable;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BurnFuelCommand implements Command {
    public static final String BURN_FUEL_EXCEPTION_MESSAGE = "BurnFuelCommand: Not enough fuel";

    private Fuelable fuelable;

    @Override
    public void execute() {
        if (fuelable.getCurrentFuel() >= fuelable.getFuelForAction()) {
            fuelable.setFuel(fuelable.getCurrentFuel() - fuelable.getFuelForAction());
        } else {
            throw new CommandException(BURN_FUEL_EXCEPTION_MESSAGE);
        }
    }
}
