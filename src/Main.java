import core.AbstractEngine;
import core.Move;
import core.Status;
import core.Vector;
import tafl.engines.ardri.ArdRiEngine;
import tafl.engines.brandubh.BrandubhEngine;
import tafl.engines.tablut.TablutEngine;

import java.util.Scanner;

class Main {
    public static void main (String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final AbstractEngine engine;

        while (true) {
            System.out.println("Choose game:");
            System.out.println("1) Brandubh");
            System.out.println("2) Tablut");
            System.out.println("3) Ard Ri");
            final int game = scanner.nextInt();

            if (game == 1) {
                engine = new BrandubhEngine();
                break;
            }

            if (game == 2) {
                engine = new TablutEngine();
                break;
            }

            if (game == 3) {
                engine = new ArdRiEngine();
                break;
            }
        }

        while (engine.getStatus() == Status.Active) {
            System.out.println();
            System.out.println(engine.getPresentation());
            System.out.println("Make move: ");

            if (!engine.makeMove(new Move(
                new Vector(scanner.nextInt(), scanner.nextInt()),
                new Vector(scanner.nextInt(), scanner.nextInt())
            )))
                System.out.println("Wrong move");
        }

        System.out.println();
        System.out.println(engine.getPresentation());

        if (engine.getStatus() == Status.Draw)
            System.out.println("Draw");
        else
            System.out.println("Player " + engine.getCurrentPlayer() + " won");
    }
}
