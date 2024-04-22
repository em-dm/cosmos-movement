package emdm.otus.cosmos.movement.rotate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Direction {
    private static final int NUMBER = 8;

    private int currentDirection;

    public Direction next(int angularVelocity) {
        return new Direction((currentDirection + angularVelocity) % NUMBER);
    }
}
