package emdm.otus.cosmos.movement.rotate;

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

class RotateTest {
    public static final Direction START_DIRECTION = new Direction(1);
    public static final int START_ANGULAR_VELOCITY = 10;
    public static final Direction EXPECTED_DIRECTION = new Direction(3);

    @Mock
    Rotable rotable;

    @Captor
    ArgumentCaptor<Direction> argumentCaptor;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Поворот меняет направление объекта")
    void rotationTest() {
        when(rotable.getDirection()).thenReturn(START_DIRECTION);
        when(rotable.getAngularVelocity()).thenReturn(START_ANGULAR_VELOCITY);

        new Rotate(rotable).execute();

        Mockito.verify(rotable).setDirection(argumentCaptor.capture());
        Direction actualDirection = argumentCaptor.getValue();

        assertEquals(EXPECTED_DIRECTION, actualDirection);
    }

    @Test
    @DisplayName("Попытка повернуть объект, у которого невозможно прочитать направление, приводит к ошибке")
    void noRotationGetDirectionTest() {
        when(rotable.getDirection()).thenThrow(new RuntimeException());
        when(rotable.getAngularVelocity()).thenReturn(START_ANGULAR_VELOCITY);

        Rotate rotate = new Rotate(rotable);

        assertThrows(RuntimeException.class, rotate::execute);
    }

    @Test
    @DisplayName("Попытка повернуть объект, у которого невозможно прочитать скорость, приводит к ошибке")
    void noRotationGetVelocityTest() {
        when(rotable.getDirection()).thenReturn(START_DIRECTION);
        when(rotable.getAngularVelocity()).thenThrow(new RuntimeException());

        Rotate rotate = new Rotate(rotable);

        assertThrows(RuntimeException.class, rotate::execute);
    }

    @Test
    @DisplayName("Попытка повернуть объект, у которого невозможно изменить направление, приводит к ошибке")
    void noRotationSetDirectionTest() {
        when(rotable.getDirection()).thenReturn(START_DIRECTION);
        when(rotable.getAngularVelocity()).thenReturn(START_ANGULAR_VELOCITY);
        doThrow(new RuntimeException())
                .when(rotable)
                .setDirection(any());

        Rotate rotate = new Rotate(rotable);

        assertThrows(RuntimeException.class, rotate::execute);
    }
}