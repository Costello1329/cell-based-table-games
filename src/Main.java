import core.*;
import tafl.TaflField;
import tafl.TaflMove;
import tafl.engines.ardri.ArdRiEngine;
import tafl.engines.brandubh.BrandubhEngine;
import tafl.engines.brandubh.BrandubhFieldFactory;
import tafl.engines.tablut.TablutEngine;

import java.util.Scanner;

class Main {
    public static void main (String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final AbstractEngine<? extends AbstractField, TaflMove> engine;

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

        while (engine.getStatus() == Status.ACTIVE) {
            System.out.println();
            System.out.println(engine.getPresentation());
            System.out.println("Make move: ");

            try {
                engine.makeMove(new TaflMove(
                    new Vector(scanner.nextInt(), scanner.nextInt()),
                    new Vector(scanner.nextInt(), scanner.nextInt())
                ));
            } catch (final InvalidMoveException exception) {
                System.out.println("Wrong move: " + exception.getMessage());
            }
        }

        System.out.println();
        System.out.println(engine.getPresentation());

        if (engine.getStatus() == Status.DRAW)
            System.out.println("Draw");
        else
            System.out.println("Player " + engine.getCurrentPlayer() + " won");
    }
}
