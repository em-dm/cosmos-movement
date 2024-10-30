package emdm.otus.cosmos.movement.ioc;

import emdm.otus.cosmos.movement.commands.Command;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;
import java.util.function.Function;

import static emdm.otus.cosmos.movement.ioc.IoC.DEPENDENCY_NOT_FOUND;
import static emdm.otus.cosmos.movement.ioc.IoC.IOC_UPDATE_IOC_RESOLVE_DEPENDENCY_STRATEGY;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IoCTest {
    public static final String IOC_UNKNOWN = "IoC.Unknown";

    @Test
    @DisplayName(IOC_UPDATE_IOC_RESOLVE_DEPENDENCY_STRATEGY)
    void updateIoCResolveDependencyStrategyTest() {
        AtomicBoolean wasCalled = new AtomicBoolean(false);

        IoC.<Command>resolve(
                IOC_UPDATE_IOC_RESOLVE_DEPENDENCY_STRATEGY,
                (Function<BiFunction<String, Object[], Object>, BiFunction<String, Object[], Object>>) oldStrategy -> {
                    wasCalled.set(true);
                    return oldStrategy;
                }
        ).execute();

        assertTrue(wasCalled.get());
    }

    @Test
    @DisplayName("Unknown dependency")
    void unknownDependencyTest() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> IoC.<Command>resolve(IOC_UNKNOWN));
        assertTrue(exception.getMessage().contains(String.format(DEPENDENCY_NOT_FOUND, IOC_UNKNOWN)));
    }

    @Test
    @DisplayName("Invalid cast dependency")
    void invalidCastDependencyTest() {
        Exception exception = assertThrows(ClassCastException.class,
                () -> IoC.<Map<String, String>>resolve(IOC_UPDATE_IOC_RESOLVE_DEPENDENCY_STRATEGY,
                        (Function<BiFunction<String, Object[], Object>, BiFunction<String, Object[], Object>>) oldStrategy -> oldStrategy).containsKey("a"));
        assertTrue(exception.getMessage().contains("cannot be cast to class"));
    }
}