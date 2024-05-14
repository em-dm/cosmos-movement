package emdm.otus.cosmos.movement.commands.commands;

import emdm.otus.cosmos.movement.commands.Command;
import emdm.otus.cosmos.movement.move.Movable;
import emdm.otus.cosmos.movement.move.Move;

public class MoveCommand extends Move implements Command {
    public MoveCommand(Movable movable) {
        super(movable);
    }
}
