package emdm.otus.cosmos.movement.commands.commands.ioc;

import emdm.otus.cosmos.movement.commands.Command;
import emdm.otus.cosmos.movement.ioc.IoC;
import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.function.Function;

@AllArgsConstructor
public class RegisterDependencyCommand implements Command {
    String dependencyName;
    Function<Object[], Object> dependencyResolverStrategy;

    @Override
    public void execute() {
        Map<String, Function<Object[], Object>> currentScope = IoC.resolve("IoC.Scope.Current");
        currentScope.put(dependencyName, dependencyResolverStrategy);
    }
}
