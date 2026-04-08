public class Shapes {

    // ── Instance fields ──────────────────────────────────────
    private double side;        // square / equilateral triangle
    private double length;      // rectangle
    private double width;       // rectangle
    private double radius;      // circle
    private double base;        // triangle (base & height form)
    private double height;      // triangle / parallelogram
    private String shapeType;   // tracks which shape was created

    // ── Constructor Overloading ───────────────────────────────

    /** Single value → Square */
    public Shapes(double side) {
        this.side      = side;
        this.shapeType = "Square";
    }

    /** Two values → Rectangle */
    public Shapes(double length, double width) {
        this.length    = length;
        this.width     = width;
        this.shapeType = "Rectangle";
    }

    /** Three values → Triangle (base, height, type label) */
    public Shapes(double base, double height, String shapeType) {
        this.base      = base;
        this.height    = height;
        this.shapeType = shapeType;   // e.g. "Triangle"
    }

    /** Boolean flag differentiates Circle from Square */
    public Shapes(double radius, boolean isCircle) {
        if (isCircle) {
            this.radius    = radius;
            this.shapeType = "Circle";
        } else {
            this.side      = radius;   // reuse field
            this.shapeType = "Square";
        }
    }

    // ── Method Overloading — area() ───────────────────────────

    /** Area of a Square */
    public double area(double side) {
        System.out.println("  [Method] area(double side)  →  Square");
        return side * side;
    }

    /** Area of a Rectangle */
    public double area(double length, double width) {
        System.out.println("  [Method] area(double length, double width)  →  Rectangle");
        return length * width;
    }

    /** Area of a Circle */
    public double area(double radius, boolean isCircle) {
        System.out.println("  [Method] area(double radius, boolean)  →  Circle");
        return Math.PI * radius * radius;
    }

    /** Area of a Triangle using base & height */
    public double area(double base, double height, String shape) {
        System.out.println("  [Method] area(double base, double height, String)  →  Triangle");
        return 0.5 * base * height;
    }

    /** Area of an Equilateral Triangle (all sides equal) */
    public double area(int side) {
        System.out.println("  [Method] area(int side)  →  Equilateral Triangle");
        return (Math.sqrt(3) / 4) * side * side;
    }

    // ── Display helper ────────────────────────────────────────
    public void displayArea() {
        double result = 0;
        System.out.println("\n  Shape   : " + shapeType);
        switch (shapeType) {
            case "Square":
                result = area(side);
                break;
            case "Rectangle":
                result = area(length, width);
                break;
            case "Circle":
                result = area(radius, true);
                break;
            case "Triangle":
                result = area(base, height, shapeType);
                break;
        }
        System.out.printf("  Area    : %.4f%n", result);
    }

    // ── main ──────────────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("=======================================================");
        System.out.println("       PART 1 — Constructor & Method Overloading       ");
        System.out.println("=======================================================");

        // Constructor overloading creates different shape objects
        Shapes square    = new Shapes(5.0);                    // side = 5
        Shapes rectangle = new Shapes(8.0, 4.0);              // l = 8, w = 4
        Shapes circle    = new Shapes(7.0, true);              // radius = 7
        Shapes triangle  = new Shapes(6.0, 9.0, "Triangle");  // base=6, h=9

        square   .displayArea();
        rectangle.displayArea();
        circle   .displayArea();
        triangle .displayArea();

        // Direct method-overloading demo (equilateral triangle, int side)
        System.out.println("\n  Shape   : Equilateral Triangle (direct call)");
        Shapes s = new Shapes(10.0);
        double eqArea = s.area(10);          // calls area(int)
        System.out.printf("  Area    : %.4f%n", eqArea);

        System.out.println("\n=======================================================");
        System.out.println("  All shapes processed. Overloading demo complete.");
        System.out.println("=======================================================");
    }
}
