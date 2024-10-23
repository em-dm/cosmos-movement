package emdm.otus.cosmos.movement.commands.commands.ioc;

import emdm.otus.cosmos.movement.commands.Command;
import emdm.otus.cosmos.movement.ioc.DependencyResolver;
import emdm.otus.cosmos.movement.ioc.DependencyResolverImpl;
import emdm.otus.cosmos.movement.ioc.IoC;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

import static emdm.otus.cosmos.movement.ioc.IoC.IOC_UPDATE_IOC_RESOLVE_DEPENDENCY_STRATEGY;

@SuppressWarnings("unchecked")
public class InitIoCCommand implements Command {
    public static final String IOC_SCOPE_PARENT = "IoC.Scope.Parent";
    public static final String IOC_SCOPE_CURRENT_SET = "IoC.Scope.Current.Set";
    public static final String IOC_SCOPE_CURRENT_CLEAR = "IoC.Scope.Current.Clear";
    public static final String IOC_SCOPE_CURRENT = "IoC.Scope.Current";
    public static final String IOC_SCOPE_CREATE_EMPTY = "IoC.Scope.Create.Empty";
    public static final String IOC_SCOPE_CREATE = "IoC.Scope.Create";
    public static final String IOC_REGISTER = "IoC.Register";
    public static final String THE_ROOT_SCOPE_HAS_NO_PARENT_SCOPE = "The root scope has no parent scope.";

    private static final Map<String, Function<Object[], Object>> rootScope = new ConcurrentHashMap<>();
    private static final ThreadLocal<Map<String, Function<Object[], Object>>> currentScope = new ThreadLocal<>();

    private static boolean isReady = false;

    @Override
    public void execute() {
        if (isReady) {
            return;
        }
        synchronized (rootScope) {
            if (isReady) {
                return;
            }

            rootScope.put(
                    IOC_SCOPE_CURRENT_SET,
                    (Object[] args) -> (Command) () -> currentScope.set((Map<String, Function<Object[], Object>>) args[0]));

            rootScope.put(
                    IOC_SCOPE_CURRENT_CLEAR,
                    (Object[] args) -> (Command) currentScope::remove);

            rootScope.put(
                    IOC_SCOPE_CURRENT,
                    (Object[] args) -> currentScope.get() != null ? currentScope.get() : rootScope);

            rootScope.put(
                    IOC_SCOPE_PARENT,
                    (Object[] args) -> {
                        throw new RuntimeException(THE_ROOT_SCOPE_HAS_NO_PARENT_SCOPE);
                    });

            rootScope.put(
                    IOC_SCOPE_CREATE_EMPTY,
                    (Object[] args) -> new ConcurrentHashMap<String, Function<Object[], Object>>());

            rootScope.put(
                    IOC_SCOPE_CREATE,
                    (Object[] args) -> {
                        Map<String, Function<Object[], Object>> newScope = IoC.resolve(IOC_SCOPE_CREATE_EMPTY);
                        Map<String, Function<Object[], Object>> parentScope;
                        if (args.length > 0) {
                            parentScope = (Map<String, Function<Object[], Object>>) args[0];
                        } else {
                            parentScope = IoC.resolve(IOC_SCOPE_CURRENT);
                        }
                        newScope.put(IOC_SCOPE_PARENT, (Object[] objects) -> parentScope);
                        return newScope;
                    });

            rootScope.put(
                    IOC_REGISTER,
                    (Object[] args) -> new RegisterDependencyCommand((String) args[0], (Function<Object[], Object>) args[1]));

            IoC.<Command>resolve(
                    IOC_UPDATE_IOC_RESOLVE_DEPENDENCY_STRATEGY,
                    (Function<BiFunction<String, Object[], Object>, BiFunction<String, Object[], Object>>) oldStrategy ->
                            (dependencyName, args) -> {
                                Object scope = currentScope.get() != null ? currentScope.get() : rootScope;
                                DependencyResolver dependencyResolver = new DependencyResolverImpl(scope);
                                return dependencyResolver.resolve(dependencyName, args);
                            }
            ).execute();

            isReady = true;
        }
    }
}
