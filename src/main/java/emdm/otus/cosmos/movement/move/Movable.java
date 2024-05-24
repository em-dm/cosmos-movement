package emdm.otus.cosmos.movement.move;

public interface Movable {
    Vector getPosition();

    void setPosition(Vector position);

    Vector getVelocity();

    void setVelocity(Vector velocity);
}
