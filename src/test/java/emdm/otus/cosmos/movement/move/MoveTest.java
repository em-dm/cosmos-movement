package emdm.otus.cosmos.movement.move;

import emdm.otus.cosmos.movement.commands.commands.MoveCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class MoveTest {
    public static final Vector START_POSITION = new Vector(12, 5);
    public static final Vector START_VELOCITY = new Vector(-7, 3);
    public static final Vector EXPECTED_POSITION = new Vector(5, 8);

    @Mock
    Movable movable;

    @Captor
    ArgumentCaptor<Vector> argumentCaptor;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Движение меняет положение объекта")
    void movementTest() {
        when(movable.getPosition()).thenReturn(START_POSITION);
        when(movable.getVelocity()).thenReturn(START_VELOCITY);

        new Move(movable).execute();

        Mockito.verify(movable).setPosition(argumentCaptor.capture());
        Vector actualPosition = argumentCaptor.getValue();

        assertEquals(EXPECTED_POSITION, actualPosition);
    }

    @Test
    @DisplayName("Движение меняет положение объекта")
    void moveCommandTest() {
        when(movable.getPosition()).thenReturn(START_POSITION);
        when(movable.getVelocity()).thenReturn(START_VELOCITY);

        new MoveCommand(movable).execute();

        Mockito.verify(movable).setPosition(argumentCaptor.capture());
        Vector actualPosition = argumentCaptor.getValue();

        assertEquals(EXPECTED_POSITION, actualPosition);
    }

    @Test
    @DisplayName("Попытка сдвинуть объект, у которого невозможно прочитать положение в пространстве, приводит к ошибке")
    void noMovementGetPositionTest() {
        when(movable.getPosition()).thenThrow(new RuntimeException());
        when(movable.getVelocity()).thenReturn(START_VELOCITY);

        Move move = new Move(movable);

        assertThrows(RuntimeException.class, move::execute);
    }

    @Test
    @DisplayName("Попытка сдвинуть объект, у которого невозможно прочитать значение мгновенной скорости, приводит к ошибке")
    void noMovementGetVelocityTest() {
        when(movable.getPosition()).thenReturn(START_POSITION);
        when(movable.getVelocity()).thenThrow(new RuntimeException());

        Move move = new Move(movable);

        assertThrows(RuntimeException.class, move::execute);
    }

    @Test
    @DisplayName("Попытка сдвинуть объект, у которого невозможно изменить положение в пространстве, приводит к ошибке")
    void noMovementSetPositionTest() {
        when(movable.getPosition()).thenReturn(START_POSITION);
        when(movable.getVelocity()).thenReturn(START_VELOCITY);
        doThrow(new RuntimeException())
                .when(movable)
                .setPosition(any());

        Move move = new Move(movable);

        assertThrows(RuntimeException.class, move::execute);
    }
}