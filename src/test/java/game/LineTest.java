package game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Test;

public class LineTest {

    @Test
    void returnedPointsCannotMutateLine() {
        Line line = new Line(1, 2, 3, 2);
        List<Point> points = line.getPoints();

        assertThrows(UnsupportedOperationException.class, points::clear);
        points.get(0).x = 99;

        assertEquals(1, line.getPoints().get(0).x);
        Iterator<Point> iterator = line.iterator();
        iterator.next().x = 77;
        assertEquals(1, line.getPoints().get(0).x);
        assertThrows(UnsupportedOperationException.class, iterator::remove);
    }
}
