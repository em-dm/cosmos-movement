package emdm.otus.cosmos.movement.move;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Move {
    Movable movable;

    public void execute() {
        movable.setPosition(Vector.plus(
                movable.getPosition(),
                movable.getVelocity()));
    }
}