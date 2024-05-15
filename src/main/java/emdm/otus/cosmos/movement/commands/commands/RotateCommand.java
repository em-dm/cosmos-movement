package emdm.otus.cosmos.movement.commands.commands;

import emdm.otus.cosmos.movement.commands.Command;
import emdm.otus.cosmos.movement.rotate.Rotable;
import emdm.otus.cosmos.movement.rotate.Rotate;

public class RotateCommand extends Rotate implements Command {
    public RotateCommand(Rotable rotable) {
        super(rotable);
    }
}
