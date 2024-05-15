package emdm.otus.cosmos.movement.repeatandlog;

import com.github.valfirst.slf4jtest.CleanupStage;
import com.github.valfirst.slf4jtest.TestLogger;
import com.github.valfirst.slf4jtest.TestLoggerFactory;
import com.github.valfirst.slf4jtest.TestLoggerFactoryExtension;
import com.github.valfirst.slf4jtest.TestLoggerFactorySettings;
import emdm.otus.cosmos.movement.commands.Command;
import emdm.otus.cosmos.movement.commands.CommandQueue;
import emdm.otus.cosmos.movement.commands.commands.LogExceptionCommand;
import emdm.otus.cosmos.movement.commands.commands.RepeatCommand;
import emdm.otus.cosmos.movement.exceptions.handlers.LogExceptionHandler;
import emdm.otus.cosmos.movement.exceptions.handlers.RepeatAndLogExceptionHandler;
import emdm.otus.cosmos.movement.exceptions.handlers.RepeatHandler;
import emdm.otus.cosmos.movement.exceptions.handlers.RepeatTwiceAndLogExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.event.Level;

import java.util.NoSuchElementException;

import static emdm.otus.cosmos.movement.commands.commands.LogExceptionCommand.LOG_MSG;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(TestLoggerFactoryExtension.class)
@TestLoggerFactorySettings(cleanupStage = CleanupStage.BEFORE_EACH)
class RepeatAndLogExceptionTest {
    public static final String EXCEPTION_MSG = "logExceptionTest";

    private final TestLogger logExceptionCommandLogger = TestLoggerFactory.getTestLogger(LogExceptionCommand.class);

    @Mock
    Command throwCommand;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

        doThrow(new RuntimeException(EXCEPTION_MSG))
                .when(throwCommand)
                .execute();
    }

    @Test
    @DisplayName("при первом выбросе исключения повторить команду, при повторном выбросе исключения записать информацию в лог")
    void repreatAndLogExceptionHandlerTest() {
        RepeatAndLogExceptionHandler repeatAndLogExceptionHandler =
                new RepeatAndLogExceptionHandler(throwCommand.getClass(), RuntimeException.class);

        CommandQueue.add(throwCommand);

        assertDoesNotThrow(CommandQueue::run);

        assertNotNull(repeatAndLogExceptionHandler);
        verify(throwCommand, times(2)).execute();
        assertLog();
    }

    @Test
    @DisplayName("повторить два раза, потом записать в лог")
    void repreatTwiceAndLogExceptionHandlerTest() {
        RepeatTwiceAndLogExceptionHandler repeatTwiceAndLogExceptionHandler =
                new RepeatTwiceAndLogExceptionHandler(throwCommand.getClass(), RuntimeException.class);

        CommandQueue.add(throwCommand);

        assertDoesNotThrow(CommandQueue::run);

        assertNotNull(repeatTwiceAndLogExceptionHandler);
        verify(throwCommand, times(3)).execute();
        assertLog();
    }

    @Test
    @DisplayName("Реализовать обработчик исключения, который ставит в очередь Команду - повторитель команды, выбросившей исключение")
    void repeatHandlerTest() {
        RepeatHandler repeatHandler = new RepeatHandler(throwCommand.getClass(), RuntimeException.class);

        CommandQueue.add(throwCommand);

        assertThrows(NoSuchElementException.class, CommandQueue::run);

        verify(throwCommand, times(2)).execute();
        assertNotNull(repeatHandler);
    }

    @Test
    @DisplayName("Реализовать Команду, которая повторяет Команду, выбросившую исключение")
    void repeatTest() {
        Command repeatCommand = new RepeatCommand(throwCommand);

        assertThrows(RuntimeException.class, repeatCommand::execute, EXCEPTION_MSG);

        verify(throwCommand, times(1)).execute();
    }

    @Test
    @DisplayName("Реализовать обработчик исключения, который ставит Команду, пишущую в лог в очередь Команд")
    void logExceptionHandlerTest() {
        LogExceptionHandler logExceptionHandler = new LogExceptionHandler(throwCommand.getClass(), RuntimeException.class);

        CommandQueue.add(throwCommand);

        assertDoesNotThrow(CommandQueue::run);

        assertNotNull(logExceptionHandler);
        verify(throwCommand, times(1)).execute();
        assertLog();
    }

    @Test
    @DisplayName("Реализовать Команду, которая записывает информацию о выброшенном исключении в лог")
    void logExceptionTest() {
        Command logExceptionCommand = new LogExceptionCommand(new Exception(EXCEPTION_MSG));

        assertDoesNotThrow(logExceptionCommand::execute);

        assertLog();
    }

    private void assertLog() {
        assertEquals(1, logExceptionCommandLogger.getLoggingEvents().size());
        assertEquals(Level.ERROR, logExceptionCommandLogger.getLoggingEvents().get(0).getLevel());
        assertEquals(LOG_MSG, logExceptionCommandLogger.getLoggingEvents().get(0).getMessage());
        assertFalse(logExceptionCommandLogger.getLoggingEvents().get(0).getThrowable().isEmpty());
        assertEquals(EXCEPTION_MSG, logExceptionCommandLogger.getLoggingEvents().get(0).getThrowable().get().getMessage());
    }
}