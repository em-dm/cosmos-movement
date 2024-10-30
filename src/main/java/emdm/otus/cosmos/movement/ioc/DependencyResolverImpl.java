package emdm.otus.cosmos.movement.ioc;

import java.util.Map;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public class DependencyResolverImpl implements DependencyResolver {
    private final Map<String, Function<Object[], Object>> scopeDependencies;

    public DependencyResolverImpl(Object scope) {
        scopeDependencies = (Map<String, Function<Object[], Object>>) scope;
    }

    @Override
    public Object resolve(String dependencyName, Object[] args) {
        Map<String, Function<Object[], Object>> dependencies = scopeDependencies;

        while (true) {
            if (dependencies.containsKey(dependencyName)) {
                return dependencies.get(dependencyName).apply(args);
            } else {
                dependencies = (Map<String, Function<Object[], Object>>) dependencies.get("IoC.Scope.Parent").apply(args);
            }
        }
    }
}
