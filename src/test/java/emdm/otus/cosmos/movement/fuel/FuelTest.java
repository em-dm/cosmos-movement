package emdm.otus.cosmos.movement.fuel;

import emdm.otus.cosmos.movement.commands.Command;
import emdm.otus.cosmos.movement.commands.commands.BurnFuelCommand;
import emdm.otus.cosmos.movement.commands.commands.CheckFuelCommand;
import emdm.otus.cosmos.movement.commands.commands.MacroCommand;
import emdm.otus.cosmos.movement.commands.commands.MoveWithFuelCommand;
import emdm.otus.cosmos.movement.exceptions.CommandException;
import emdm.otus.cosmos.movement.move.Movable;
import emdm.otus.cosmos.movement.move.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static emdm.otus.cosmos.movement.commands.commands.BurnFuelCommand.BURN_FUEL_EXCEPTION_MESSAGE;
import static emdm.otus.cosmos.movement.commands.commands.CheckFuelCommand.CHECK_FUEL_EXCEPTION_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FuelTest {
    public static final Vector START_POSITION = new Vector(12, 5);
    public static final Vector START_VELOCITY = new Vector(-7, 3);
    public static final Vector EXPECTED_POSITION = new Vector(5, 8);

    private static final Integer CURRENT_FUEL_VALUE_GREATER = 5;
    private static final Integer CURRENT_FUEL_VALUE_LESS = 3;
    private static final Integer FUEL_FOR_ACTION_VALUE = 4;
    private static final Integer EXPECTED_CURRENT_FUEL = 1;

    @Mock
    Fuelable fuelable;
    @Mock
    Movable movable;

    @Captor
    ArgumentCaptor<Vector> vectorArgumentCaptor;
    @Captor
    ArgumentCaptor<Integer> integerArgumentCaptor;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest
    @CsvSource(value = {"5", "4"})
    @DisplayName("CheckFuelComamnd currentFuel >= fuelForAction")
    void checkFuelOkTest(Integer currentFuelValue) {
        when(fuelable.getCurrentFuel()).thenReturn(currentFuelValue);
        when(fuelable.getFuelForAction()).thenReturn(FUEL_FOR_ACTION_VALUE);
        Command checkFuelCommand = new CheckFuelCommand(fuelable);

        assertDoesNotThrow(checkFuelCommand::execute);
    }

    @Test
    @DisplayName("CheckFuelComamnd currentFuel < fuelForAction")
    void checkFuelLessTest() {
        when(fuelable.getCurrentFuel()).thenReturn(CURRENT_FUEL_VALUE_LESS);
        when(fuelable.getFuelForAction()).thenReturn(FUEL_FOR_ACTION_VALUE);
        Command checkFuelCommand = new CheckFuelCommand(fuelable);

        Exception exception = assertThrows(CommandException.class, checkFuelCommand::execute);
        assertTrue(exception.getMessage().contains(CHECK_FUEL_EXCEPTION_MESSAGE));
    }

    @Test
    @DisplayName("Попытка проверить топливо, когда невозможно прочитать текущее значение, приводит к ошибке")
    void noCheckFuelGetCurrentFuelTest() {
        when(fuelable.getCurrentFuel()).thenThrow(new RuntimeException());
        when(fuelable.getFuelForAction()).thenReturn(FUEL_FOR_ACTION_VALUE);

        Command checkFuelCommand = new CheckFuelCommand(fuelable);

        assertThrows(RuntimeException.class, checkFuelCommand::execute);
    }

    @Test
    @DisplayName("Попытка проверить топливо, когда невозможно прочитать потребление топлива, приводит к ошибке")
    void noCheckFuelGetFuelForActionTest() {
        when(fuelable.getCurrentFuel()).thenReturn(CURRENT_FUEL_VALUE_GREATER);
        when(fuelable.getFuelForAction()).thenThrow(new RuntimeException());

        Command checkFuelCommand = new CheckFuelCommand(fuelable);

        assertThrows(RuntimeException.class, checkFuelCommand::execute);
    }

    @ParameterizedTest
    @CsvSource(value = {"5", "4"})
    @DisplayName("BurnFuelComamnd currentFuel >= fuelForAction")
    void burnFuelOkTest(Integer currentFuelValue) {
        when(fuelable.getCurrentFuel()).thenReturn(currentFuelValue);
        when(fuelable.getFuelForAction()).thenReturn(FUEL_FOR_ACTION_VALUE);
        Command burnFuelCommand = new BurnFuelCommand(fuelable);

        assertDoesNotThrow(burnFuelCommand::execute);
    }

    @Test
    @DisplayName("BurnFuelComamnd currentFuel < fuelForAction")
    void burnFuelLessTest() {
        when(fuelable.getCurrentFuel()).thenReturn(CURRENT_FUEL_VALUE_LESS);
        when(fuelable.getFuelForAction()).thenReturn(FUEL_FOR_ACTION_VALUE);
        Command burnFuelCommand = new BurnFuelCommand(fuelable);

        Exception exception = assertThrows(CommandException.class, burnFuelCommand::execute);
        assertTrue(exception.getMessage().contains(BURN_FUEL_EXCEPTION_MESSAGE));
    }

    @Test
    @DisplayName("Попытка сжечь топливо, когда невозможно прочитать текущее значение, приводит к ошибке")
    void noBurnFuelGetCurrentFuelTest() {
        when(fuelable.getCurrentFuel()).thenThrow(new RuntimeException());
        when(fuelable.getFuelForAction()).thenReturn(FUEL_FOR_ACTION_VALUE);

        Command burnFuelCommand = new BurnFuelCommand(fuelable);

        assertThrows(RuntimeException.class, burnFuelCommand::execute);
    }

    @Test
    @DisplayName("Попытка сжечь топливо, когда невозможно прочитать потребление топлива, приводит к ошибке")
    void noBurnFuelGetFuelForActionTest() {
        when(fuelable.getCurrentFuel()).thenReturn(CURRENT_FUEL_VALUE_GREATER);
        when(fuelable.getFuelForAction()).thenThrow(new RuntimeException());

        Command burnFuelCommand = new BurnFuelCommand(fuelable);

        assertThrows(RuntimeException.class, burnFuelCommand::execute);
    }

    @Test
    @DisplayName("MacroCommand ОК")
    void macroCommandOkTest() {
        when(fuelable.getCurrentFuel()).thenReturn(CURRENT_FUEL_VALUE_GREATER);
        when(fuelable.getFuelForAction()).thenReturn(FUEL_FOR_ACTION_VALUE);

        Command checkFuelCommand = new CheckFuelCommand(fuelable);
        Command burnFuelCommand = new BurnFuelCommand(fuelable);

        Command macroCommand = new MacroCommand(List.of(checkFuelCommand, burnFuelCommand));

        assertDoesNotThrow(macroCommand::execute);
    }

    @Test
    @DisplayName("MacroCommand fail")
    void macroCommandFailTest() {
        when(fuelable.getCurrentFuel()).thenReturn(CURRENT_FUEL_VALUE_LESS);
        when(fuelable.getFuelForAction()).thenReturn(FUEL_FOR_ACTION_VALUE);

        Command checkFuelCommand = new CheckFuelCommand(fuelable);
        Command burnFuelCommand = new BurnFuelCommand(fuelable);

        Command macroCommand = new MacroCommand(List.of(checkFuelCommand, burnFuelCommand));

        Exception exception = assertThrows(CommandException.class, macroCommand::execute);
        assertTrue(exception.getMessage().contains(CHECK_FUEL_EXCEPTION_MESSAGE));
    }

    @Test
    @DisplayName("MacroCommand exception")
    void macroCommandExceptionTest() {
        when(fuelable.getCurrentFuel()).thenThrow(new RuntimeException());
        when(fuelable.getFuelForAction()).thenReturn(FUEL_FOR_ACTION_VALUE);

        Command checkFuelCommand = new CheckFuelCommand(fuelable);
        Command burnFuelCommand = new BurnFuelCommand(fuelable);

        Command macroCommand = new MacroCommand(List.of(checkFuelCommand, burnFuelCommand));

        assertThrows(CommandException.class, macroCommand::execute);
    }

    @Test
    @DisplayName("Макрокоманда движения по прямой с расходом топлива")
    void moveWithFuelCommandTest() {
        when(fuelable.getCurrentFuel()).thenReturn(CURRENT_FUEL_VALUE_GREATER);
        when(fuelable.getFuelForAction()).thenReturn(FUEL_FOR_ACTION_VALUE);
        when(movable.getPosition()).thenReturn(START_POSITION);
        when(movable.getVelocity()).thenReturn(START_VELOCITY);

        Command moveWithFuelCommand = new MoveWithFuelCommand(fuelable, movable);
        assertDoesNotThrow(moveWithFuelCommand::execute);

        verify(fuelable).setFuel(integerArgumentCaptor.capture());
        Integer actualCurrentFuel = integerArgumentCaptor.getValue();
        assertEquals(EXPECTED_CURRENT_FUEL, actualCurrentFuel);

        verify(movable).setPosition(vectorArgumentCaptor.capture());
        Vector actualPosition = vectorArgumentCaptor.getValue();
        assertEquals(EXPECTED_POSITION, actualPosition);
    }

    @Test
    @DisplayName("Макрокоманда движения по прямой с недостаточным топливом")
    void noMovementWithoutFuelTest() {
        when(fuelable.getCurrentFuel()).thenReturn(CURRENT_FUEL_VALUE_LESS);
        when(fuelable.getFuelForAction()).thenReturn(FUEL_FOR_ACTION_VALUE);
        when(movable.getPosition()).thenReturn(START_POSITION);
        when(movable.getVelocity()).thenReturn(START_VELOCITY);

        Command moveWithFuelCommand = new MoveWithFuelCommand(fuelable, movable);

        Exception exception = assertThrows(CommandException.class, moveWithFuelCommand::execute);
        assertTrue(exception.getMessage().contains(CHECK_FUEL_EXCEPTION_MESSAGE));

        verify(fuelable, times(0)).setFuel(integerArgumentCaptor.capture());
        verify(movable, times(0)).setPosition(vectorArgumentCaptor.capture());
    }

    @Test
    @DisplayName("Макрокоманда движения по прямой с расходом топлива, когда невозможно прочитать текущее значение топлива")
    void noMovementObjectWithoutFuelTest() {
        when(fuelable.getCurrentFuel()).thenThrow(new RuntimeException());
        when(fuelable.getFuelForAction()).thenReturn(FUEL_FOR_ACTION_VALUE);
        when(movable.getPosition()).thenReturn(START_POSITION);
        when(movable.getVelocity()).thenReturn(START_VELOCITY);

        Command moveWithFuelCommand = new MoveWithFuelCommand(fuelable, movable);
        assertThrows(CommandException.class, moveWithFuelCommand::execute);

        verify(fuelable, times(0)).setFuel(integerArgumentCaptor.capture());
        verify(movable, times(0)).setPosition(vectorArgumentCaptor.capture());
    }
}
