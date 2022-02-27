package core;

public record Vector (int x, int y) {
    public Vector add (final Vector other) {  return new Vector(x + other.x, y + other.y); }
    public Vector subtract (final Vector other) {  return new Vector(x - other.x, y - other.y); }
    public Vector multiply (final int coefficient) {  return new Vector(x * coefficient, y * coefficient); }
    public Vector divide (final int coefficient) {  return new Vector(x / coefficient, y / coefficient); }
    public Vector copy () { return new Vector(x, y); }

    @Override
    public boolean equals (final Object other) {
        return other instanceof final Vector otherVector && x == otherVector.x() && y == otherVector.y();
    }

    @Override
    public int hashCode () { return x ^ y; }

    @Override
    public String toString () { return String.format("(%d, %d)", x, y); }

    public static Vector[] DIRECTIONS_L_1 = new Vector[] {
        new Vector(0, 1),
        new Vector(1, 0),
        new Vector(0, -1),
        new Vector(-1, 0)
    };

    public static Vector[] DIRECTIONS_L_INF = new Vector[] {
        new Vector(0, 1),
        new Vector(1, 1),
        new Vector(1, 0),
        new Vector(1, -1),
        new Vector(0, -1),
        new Vector(-1, -1),
        new Vector(-1, 0),
        new Vector(-1, 1)
    };

    public static int distanceL1 (final Vector first, final Vector second) {
        return Math.abs(first.y() - second.y()) + Math.abs(first.x() - second.x());
    }

    public static int distanceLInf (final Vector first, final Vector second) {
        return Math.max(Math.abs(first.y() - second.y()), Math.abs(first.x() - second.x()));
    }
}
