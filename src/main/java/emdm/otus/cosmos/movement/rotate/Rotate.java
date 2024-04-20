package emdm.otus.cosmos.movement.rotate;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Rotate {
    Rotable rotable;

    public void execute() {
        rotable.setDirection(
                rotable.getDirection()
                        .next(rotable.getAngularVelocity())
        );
    }
}
