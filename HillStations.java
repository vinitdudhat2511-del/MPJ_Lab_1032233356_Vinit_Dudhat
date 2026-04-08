// ============================================================
//  HillStations.java — Method Overriding / Runtime Polymorphism
// ============================================================

// ── Parent / Superclass ───────────────────────────────────────
class Hillstations {

    String name;

    public Hillstations(String name) {
        this.name = name;
    }

    /** To be overridden by each subclass */
    public void famousfood() {
        System.out.println("  [Hillstations] Generic hill-station food.");
    }

    /** To be overridden by each subclass */
    public void famousfor() {
        System.out.println("  [Hillstations] Generic hill-station attraction.");
    }

    /** Utility — prints a section separator */
    static void separator(String title) {
        System.out.println("\n  ┌─────────────────────────────────────────┐");
        System.out.printf ("  │  %-41s│%n", " " + title);
        System.out.println("  └─────────────────────────────────────────┘");
    }
}

// ── Subclass 1 : Manali ───────────────────────────────────────
class Manali extends Hillstations {

    public Manali() {
        super("Manali");
    }

    @Override
    public void famousfood() {
        System.out.println("  Famous Food  → Siddu, Aktori, Babru, Chha Gosht");
    }

    @Override
    public void famousfor() {
        System.out.println("  Famous For   → Rohtang Pass, Solang Valley, River Rafting, Snow Sports");
    }
}

// ── Subclass 2 : Ooty ─────────────────────────────────────────
class Ooty extends Hillstations {

    public Ooty() {
        super("Ooty");
    }

    @Override
    public void famousfood() {
        System.out.println("  Famous Food  → Varkey, Home-made Chocolate, Ooty Nilgiri Tea, Oatmeal");
    }

    @Override
    public void famousfor() {
        System.out.println("  Famous For   → Nilgiri Mountain Railway, Ooty Lake, Botanical Gardens, Tea Estates");
    }
}

// ── Subclass 3 : Darjeeling ───────────────────────────────────
class Darjeeling extends Hillstations {

    public Darjeeling() {
        super("Darjeeling");
    }

    @Override
    public void famousfood() {
        System.out.println("  Famous Food  → Momos, Thukpa, Sel Roti, Gundruk, Darjeeling Tea");
    }

    @Override
    public void famousfor() {
        System.out.println("  Famous For   → Tiger Hill Sunrise, Tea Gardens, Toy Train, Himalayan Views");
    }
}

// ── Main Driver ───────────────────────────────────────────────
public class HillStations {

    public static void main(String[] args) {

        System.out.println("=======================================================");
        System.out.println("   PART 2 — Method Overriding (Runtime Polymorphism)   ");
        System.out.println("=======================================================");
        System.out.println("  Parent class reference → child class object");
        System.out.println("  JVM decides which method to invoke at RUNTIME.");

       

        Hillstations hs;   // parent-class reference variable

        // --- Manali ---
        Hillstations.separator("Hill Station : Manali");
        hs = new Manali();          // parent ref → child object
        System.out.println("  Station      → " + hs.name);
        hs.famousfood();            // Manali's overridden method called at runtime
        hs.famousfor();             // Manali's overridden method called at runtime

        // --- Ooty ---
        Hillstations.separator("Hill Station : Ooty");
        hs = new Ooty();            // parent ref → child object
        System.out.println("  Station      → " + hs.name);
        hs.famousfood();            // Ooty's overridden method called at runtime
        hs.famousfor();             // Ooty's overridden method called at runtime

        // --- Darjeeling ---
        Hillstations.separator("Hill Station : Darjeeling");
        hs = new Darjeeling();      // parent ref → child object
        System.out.println("  Station      → " + hs.name);
        hs.famousfood();            // Darjeeling's overridden method called at runtime
        hs.famousfor();             // Darjeeling's overridden method called at runtime

        // --- Base class default (no override in effect) ---
        Hillstations.separator("Hill Station : Base class (no subclass)");
        hs = new Hillstations("Generic Hill Station");
        System.out.println("  Station      → " + hs.name);
        hs.famousfood();            // Hillstations' own (parent) method
        hs.famousfor();             // Hillstations' own (parent) method


    }
}
