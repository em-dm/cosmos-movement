package emdm.otus.cosmos.movement.move;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Vector {
    private int x;
    private int y;

    public static Vector plus(Vector a, Vector b) {
        return new Vector(a.getX() + b.getX(), a.getY() + b.getY());
    }
}
