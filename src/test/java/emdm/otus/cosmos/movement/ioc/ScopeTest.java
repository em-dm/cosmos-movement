package emdm.otus.cosmos.movement.ioc;

import emdm.otus.cosmos.movement.commands.Command;
import emdm.otus.cosmos.movement.commands.commands.ioc.InitIoCCommand;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static emdm.otus.cosmos.movement.commands.commands.ioc.InitIoCCommand.IOC_REGISTER;
import static emdm.otus.cosmos.movement.commands.commands.ioc.InitIoCCommand.IOC_SCOPE_CREATE;
import static emdm.otus.cosmos.movement.commands.commands.ioc.InitIoCCommand.IOC_SCOPE_CURRENT;
import static emdm.otus.cosmos.movement.commands.commands.ioc.InitIoCCommand.IOC_SCOPE_CURRENT_CLEAR;
import static emdm.otus.cosmos.movement.commands.commands.ioc.InitIoCCommand.IOC_SCOPE_CURRENT_SET;
import static emdm.otus.cosmos.movement.commands.commands.ioc.InitIoCCommand.IOC_SCOPE_PARENT;
import static emdm.otus.cosmos.movement.ioc.IoCTest.IOC_UNKNOWN;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ScopeTest {
    public static final String SOME_DEPENDENCY = "someDependency";

    @BeforeAll
    static void initIoc() {
        new InitIoCCommand().execute();
        Map<String, Function<Object[], Object>> scope = IoC.resolve(IOC_SCOPE_CREATE);
        IoC.<Command>resolve(IOC_SCOPE_CURRENT_SET, scope).execute();
    }

    @Test
    @DisplayName("Resolve registered dependency in current scope")
    void resolveTest() {
        IoC.<Command>resolve(IOC_REGISTER, SOME_DEPENDENCY, (Function<Object[], Object>) args -> 1).execute();

        assertEquals(1, IoC.<Integer>resolve(SOME_DEPENDENCY));
    }

    @Test
    @DisplayName("Unknown dependency in scope")
    void unknownDependencyInScopeTest() {
        assertThrows(RuntimeException.class, () -> IoC.<Integer>resolve(IOC_UNKNOWN));
    }

    @Test
    @DisplayName("Use parentScope if no dependency in currentScope")
    void useParentScopeIfNoDependencyInCurrentScopeTest() {
        IoC.<Command>resolve(IOC_REGISTER, SOME_DEPENDENCY, (Function<Object[], Object>) args -> 1).execute();

        Object parentScope = IoC.resolve(IOC_SCOPE_CURRENT);

        Object currentScope = IoC.resolve(IOC_SCOPE_CREATE);
        IoC.<Command>resolve(IOC_SCOPE_CURRENT_SET, currentScope).execute();

        assertEquals(currentScope, IoC.resolve(IOC_SCOPE_CURRENT));
        assertEquals(1, IoC.<Integer>resolve(SOME_DEPENDENCY));
        assertEquals(parentScope, IoC.resolve(IOC_SCOPE_PARENT));
    }

    @Test
    @DisplayName("parentScope can be set manually for creatingScope")
    void parentScopeManuallySetTest() {
        Object scope1 = IoC.resolve(IOC_SCOPE_CREATE);
        Object scope2 = IoC.resolve(IOC_SCOPE_CREATE, scope1);

        IoC.<Command>resolve(IOC_SCOPE_CURRENT_SET, scope1);
        IoC.<Command>resolve(IOC_REGISTER, SOME_DEPENDENCY, (Function<Object[], Object>) args -> 2).execute();
        IoC.<Command>resolve(IOC_SCOPE_CURRENT_SET, scope2);

        assertEquals(2, IoC.<Integer>resolve(SOME_DEPENDENCY));
    }

    @Test
    @DisplayName("Clear current scope")
    void clearCurrentScopeTest() {
        Object parentScope = IoC.resolve(IOC_SCOPE_PARENT);
        assertDoesNotThrow(() -> IoC.<Command>resolve(IOC_SCOPE_CURRENT_CLEAR).execute());
        assertEquals(parentScope, IoC.resolve(IOC_SCOPE_CURRENT));
    }

    @Test
    @DisplayName("Resolve registered dependency in thread local scope")
    void resolveThreadLocalTest() throws InterruptedException {
        IoC.<Command>resolve(IOC_SCOPE_CURRENT_CLEAR).execute();

        Map<String, Function<Object[], Object>> scope = IoC.resolve(IOC_SCOPE_CREATE);
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            IoC.<Command>resolve(IOC_SCOPE_CURRENT_SET, scope).execute();
            IoC.<Command>resolve(IOC_REGISTER, SOME_DEPENDENCY, (Function<Object[], Object>) args -> 1).execute();
            assertEquals(1, IoC.<Integer>resolve(SOME_DEPENDENCY));
        });
        executor.shutdown();

        if (!executor.awaitTermination(1, TimeUnit.SECONDS))
            throw new RuntimeException("Timeout");

        assertThrows(RuntimeException.class, () -> IoC.<Integer>resolve(SOME_DEPENDENCY));
    }

    @Test
    @DisplayName("Resolve registered dependency from scope")
    void resolveThreadTest() throws InterruptedException {
        IoC.<Command>resolve(IOC_SCOPE_CURRENT_CLEAR).execute();
        
        Map<String, Function<Object[], Object>> scope = IoC.resolve(IOC_SCOPE_CREATE);
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            IoC.<Command>resolve(IOC_SCOPE_CURRENT_SET, scope).execute();
            IoC.<Command>resolve(IOC_REGISTER, SOME_DEPENDENCY, (Function<Object[], Object>) args -> 1).execute();
            assertEquals(1, IoC.<Integer>resolve(SOME_DEPENDENCY));
        });
        executor.shutdown();

        if (!executor.awaitTermination(1, TimeUnit.SECONDS))
            throw new RuntimeException("Timeout");

        IoC.<Command>resolve(IOC_SCOPE_CURRENT_SET, scope).execute();
        assertEquals(1, IoC.<Integer>resolve(SOME_DEPENDENCY));
    }
}