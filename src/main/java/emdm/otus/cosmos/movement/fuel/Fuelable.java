package emdm.otus.cosmos.movement.fuel;

public interface Fuelable {
    Integer getCurrentFuel();

    void setFuel(Integer position);

    Integer getFuelForAction();
}
