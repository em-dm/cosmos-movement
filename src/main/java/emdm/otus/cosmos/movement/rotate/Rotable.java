package emdm.otus.cosmos.movement.rotate;

public interface Rotable {
    Direction getDirection();

    int getAngularVelocity();

    void setDirection(Direction direction);
}
