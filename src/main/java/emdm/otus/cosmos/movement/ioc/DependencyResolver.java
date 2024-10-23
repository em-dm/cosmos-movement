package emdm.otus.cosmos.movement.ioc;

public interface DependencyResolver {
    Object resolve(String dependencyName, Object[] args);
}
