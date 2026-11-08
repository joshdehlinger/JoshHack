package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Path {

	private static PathFinder pf = new PathFinder();
	
	private List<Point> points;
	public List<Point> points() {
		List<Point> copy = new ArrayList<Point>();
		for (Point point : points)
			copy.add(new Point(point.x, point.y, point.z));
		return Collections.unmodifiableList(copy);
	}
	
	public Path(Creature creature, int x, int y){
		points = pf.findPath(creature, new Point(creature.x, creature.y, creature.z), new Point(x, y, creature.z), 300);
	}
}
