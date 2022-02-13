package core;

public record Vector (int x, int y) {
    public Vector add (final Vector other) {  return new Vector(x + other.x, y + other.y); }
    public Vector subtract (final Vector other) {  return new Vector(x - other.x, y - other.y); }
    public Vector multiply (final int coefficient) {  return new Vector(x * coefficient, y * coefficient); }
    public Vector divide (final int coefficient) {  return new Vector(x / coefficient, y / coefficient); }

    @Override
    public String toString () { return String.format("(%d, %d)", x, y); }

    public static Vector[] DIRECTIONS = new Vector[] {
        new Vector(0, 1),
        new Vector(1, 0),
        new Vector(0, -1),
        new Vector(-1, 0)
    };

    public static int distance (final Vector first, final Vector second) {
        return Math.abs(first.y() - second.y()) + Math.abs(first.x() - second.x());
    }
}
