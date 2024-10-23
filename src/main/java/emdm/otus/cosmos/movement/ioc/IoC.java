package emdm.otus.cosmos.movement.ioc;

import emdm.otus.cosmos.movement.commands.commands.ioc.UpdateIoCResolveDependencyStrategyCommand;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Контейнер инверсии зависимотей (Расширяемая фабрика).
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings("unchecked")
public class IoC {
    public static final String IOC_UPDATE_IOC_RESOLVE_DEPENDENCY_STRATEGY = "IoC.UpdateIoCResolveDependencyStrategy";
    public static final String DEPENDENCY_NOT_FOUND = "Dependency %s is not found";

    @Getter
    @Setter
    private static BiFunction<String, Object[], Object> strategy =
            (String dependencyName, Object[] args) -> {
                if (IOC_UPDATE_IOC_RESOLVE_DEPENDENCY_STRATEGY.equals(dependencyName)) {
                    return new UpdateIoCResolveDependencyStrategyCommand(
                            (Function<BiFunction<String, Object[], Object>, BiFunction<String, Object[], Object>>) args[0]);
                } else {
                    throw new IllegalArgumentException(String.format(DEPENDENCY_NOT_FOUND, dependencyName));
                }
            };

    /**
     * Разрешение зависимости.
     * <p>
     * Паттерн стратегия: Шаблонный метод resolve вызывает соответствующую стратегию
     * </p>
     *
     * @param dependencyName Строковое имя разрешаемой зависимости.
     *                       В реализации контейнера по умолчанию определена только одна зависимость
     *                       <p>{@code UpdateIoCResolveDependencyStrategy}</p>
     *                       которая позволяет переопределить стратегию разрешения зависимостей по-умолчанию
     * @param args           Произвольное количество аргументов, которые получает на вход стратегия
     *                       разрешения зависимостей.
     *                       <p>Для переопределения стратегии разрешения зависимостей по умолчанию
     *                       на вход подается лямбда функция типа</p>
     *
     *                       <p>{@code Function<BiFunction<String, Object[], Object>, BiFunction<String, Object[], Object>> }</p>
     *                       <p>
     *                       которая на вход принимает текущую стратегию разрешения зависмисостей типа
     *                       <p>{@code BiFunction<String, Object[], Object> },</p>
     *                       <p>
     *                       на выходе возвращает новую стратегию типа
     *                       <p>{@code BiFunction<String, Object[], Object> }.</p>
     * @return T Ожидаемый тип объекта, получаемого в результате разрешения зависимости.
     * Если полученный объект невозможно привести в запрашиваемому типу, то выбрасывается исключение
     */
    public static <T> T resolve(String dependencyName, Object... args) {
        return (T) strategy.apply(dependencyName, args);
    }
}
