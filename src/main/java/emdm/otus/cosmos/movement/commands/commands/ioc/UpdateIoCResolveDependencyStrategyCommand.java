package emdm.otus.cosmos.movement.commands.commands.ioc;

import emdm.otus.cosmos.movement.commands.Command;
import emdm.otus.cosmos.movement.ioc.IoC;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.util.function.BiFunction;
import java.util.function.Function;

@AllArgsConstructor
public class UpdateIoCResolveDependencyStrategyCommand implements Command, Serializable {
    Function<BiFunction<String, Object[], Object>, BiFunction<String, Object[], Object>> updateIoCStrategy;

    @Override
    public void execute() {
        IoC.setStrategy(updateIoCStrategy.apply(IoC.getStrategy()));
    }
}
