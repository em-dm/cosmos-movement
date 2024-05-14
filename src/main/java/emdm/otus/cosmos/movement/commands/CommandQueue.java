package emdm.otus.cosmos.movement.commands;

import emdm.otus.cosmos.movement.exceptions.ExceptionHandler;

import java.util.ArrayDeque;
import java.util.Deque;

public class CommandQueue {
    private static final Deque<Command> queue = new ArrayDeque<>();

    public static void add(Command command) {
        queue.addLast(command);
    }

    public static void run() {
        while (!queue.isEmpty()) {
            Command command = queue.pollFirst();
            try {
                command.execute();
            } catch (Exception exception) {
                ExceptionHandler.handle(command, exception)
                        .execute();
            }
        }
    }
}
