package emdm.otus.cosmos.movement.velocity;

import emdm.otus.cosmos.movement.commands.Command;
import emdm.otus.cosmos.movement.commands.commands.ChangeVelocityCommand;
import emdm.otus.cosmos.movement.commands.commands.RotateWithVelocityCommand;
import emdm.otus.cosmos.movement.exceptions.CommandException;
import emdm.otus.cosmos.movement.move.Movable;
import emdm.otus.cosmos.movement.move.Vector;
import emdm.otus.cosmos.movement.rotate.Direction;
import emdm.otus.cosmos.movement.rotate.Rotable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static emdm.otus.cosmos.movement.commands.commands.ChangeVelocityCommand.NO_VELOCITY;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class VelocityTest {
    public static final Vector START_VELOCITY = new Vector(-7, 3);
    public static final Vector EXPECTED_VELOCITY = new Vector(8, 8);

    public static final Direction START_DIRECTION = new Direction(1);
    public static final int START_ANGULAR_VELOCITY = 10;
    public static final Direction EXPECTED_DIRECTION = new Direction(3);

    @Mock
    Movable movable;
    @Mock
    Rotable rotable;

    @Captor
    ArgumentCaptor<Vector> vectorArgumentCaptor;
    @Captor
    ArgumentCaptor<Direction> directionArgumentCaptor;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Реализовать команду для модификации вектора мгновенной скорости при повороте")
    void velocityOKTest() {
        when(movable.getVelocity()).thenReturn(START_VELOCITY);
        when(rotable.getDirection()).thenReturn(START_DIRECTION);

        Command changeVelocityCommand = new ChangeVelocityCommand(movable, rotable);

        assertDoesNotThrow(changeVelocityCommand::execute);

        verify(movable).setVelocity(vectorArgumentCaptor.capture());
        Vector actualVelocity = vectorArgumentCaptor.getValue();
        assertEquals(EXPECTED_VELOCITY, actualVelocity);

        verify(movable, times(0)).setPosition(vectorArgumentCaptor.capture());
    }

    @Test
    @DisplayName("Реализовать команду для модификации вектора мгновенной скорости при повороте. Необходимо учесть, что не каждый разворачивающийся объект движется")
    void noVelocityTest() {
        when(movable.getVelocity()).thenThrow(new RuntimeException());
        when(rotable.getDirection()).thenReturn(START_DIRECTION);

        Command changeVelocityCommand = new ChangeVelocityCommand(movable, rotable);

        Exception exception = assertThrows(CommandException.class, changeVelocityCommand::execute);
        assertEquals(NO_VELOCITY, exception.getMessage());

        verify(movable, times(0)).setVelocity(vectorArgumentCaptor.capture());
    }

    @Test
    @DisplayName("Реализовать команду поворота, которая еще и меняет вектор мгновенной скорости (есть)")
    void rotationWithVelocityTest() {
        when(movable.getVelocity()).thenReturn(START_VELOCITY);
        when(rotable.getDirection()).thenReturn(START_DIRECTION);
        when(rotable.getAngularVelocity()).thenReturn(START_ANGULAR_VELOCITY);

        Command rotateWithVelocityCommand = new RotateWithVelocityCommand(movable, rotable);

        assertDoesNotThrow(rotateWithVelocityCommand::execute);

        verify(rotable).setDirection(directionArgumentCaptor.capture());
        Direction actualDirection = directionArgumentCaptor.getValue();
        assertEquals(EXPECTED_DIRECTION, actualDirection);

        verify(movable).setVelocity(vectorArgumentCaptor.capture());
        Vector actualVelocity = vectorArgumentCaptor.getValue();
        assertEquals(EXPECTED_VELOCITY, actualVelocity);
    }

    @Test
    @DisplayName("Реализовать команду поворота, которая еще и меняет вектор мгновенной скорости (нет)")
    void rotationNoVelocityTest() {
        when(movable.getVelocity()).thenThrow(new RuntimeException());
        when(rotable.getDirection()).thenReturn(START_DIRECTION);
        when(rotable.getAngularVelocity()).thenReturn(START_ANGULAR_VELOCITY);

        Command rotateWithVelocityCommand = new RotateWithVelocityCommand(movable, rotable);

        Exception exception = assertThrows(CommandException.class, rotateWithVelocityCommand::execute);
        assertTrue(exception.getMessage().contains(NO_VELOCITY));

        verify(rotable).setDirection(directionArgumentCaptor.capture());
        Direction actualDirection = directionArgumentCaptor.getValue();
        assertEquals(EXPECTED_DIRECTION, actualDirection);

        verify(movable, times(0)).setVelocity(vectorArgumentCaptor.capture());
    }
}
