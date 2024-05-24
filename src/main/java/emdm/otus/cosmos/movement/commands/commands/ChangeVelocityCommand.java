package emdm.otus.cosmos.movement.commands.commands;

import emdm.otus.cosmos.movement.commands.Command;
import emdm.otus.cosmos.movement.exceptions.CommandException;
import emdm.otus.cosmos.movement.move.Movable;
import emdm.otus.cosmos.movement.move.Vector;
import emdm.otus.cosmos.movement.rotate.Rotable;
import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class ChangeVelocityCommand implements Command {
    // (int) ceil(1 / sqrt(2)) = 1
    public static final Map<Integer, Vector> VELOCITY_FROM_DIRECTION = Map.of(
            0, new Vector(0, 1),
            1, new Vector(1, 1),
            2, new Vector(1, 0),
            3, new Vector(1, -1),
            4, new Vector(0, -1),
            5, new Vector(-1, -1),
            6, new Vector(-1, 0),
            7, new Vector(-1, 1)
    );
    public static final String NO_VELOCITY = "No velocity";

    private Movable movable;
    private Rotable rotable;

    @Override

    public void execute() {
        Vector velocity;

        try {
            velocity = movable.getVelocity();
        } catch (Exception exception) {
            throw new CommandException(NO_VELOCITY);
        }

        int direction = rotable.getDirection()
                .getCurrentDirection();

        movable.setVelocity(
                Vector.multiply(
                        VELOCITY_FROM_DIRECTION.get(direction),
                        Vector.length(velocity)));
    }
}
