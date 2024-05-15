package emdm.otus.cosmos.movement.move;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class Vector {
    private List<Integer> data;

    public Vector(Integer... xN) {
        data = List.of(xN);
    }

    public static Vector plus(Vector a, Vector b) {
        if (a.getData().size() != b.getData().size()) {
            throw new IllegalArgumentException("Different size of vectors");
        }

        List<Integer> resultData = new ArrayList<>(a.getData().size());
        for (int i = 0; i < a.getData().size(); i++) {
            resultData.add(a.getData().get(i) + b.getData().get(i));
        }
        return new Vector(resultData);
    }
}
