import core.*;
import tafl.TaflEngine;
import tafl.TaflMove;
import tafl.engines.ardri.ArdRiEngine;
import tafl.engines.brandubh.BrandubhEngine;
import tafl.engines.tablut.TablutEngine;
import tictactoe.TicTacToeEngine;
import tictactoe.TicTacToeMove;

import java.util.Scanner;

class Main {
    public static void main (final String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final AbstractEngine<? extends AbstractField, ? extends AbstractMove> engine;

        while (true) {
            System.out.println("Choose game:");
            System.out.println("1) Brandubh");
            System.out.println("2) Tablut");
            System.out.println("3) Ard Ri");
            System.out.println("4) Tic Tac Toe");
            final int game = scanner.nextInt();

            if (game == 1) {
                engine = new BrandubhEngine();
                break;
            }

            else if (game == 2) {
                engine = new TablutEngine();
                break;
            }

            else if (game == 3) {
                engine = new ArdRiEngine();
                break;
            }

            else if (game == 4) {
                System.out.println("Input field size: ");
                engine = new TicTacToeEngine(scanner.nextInt());
                break;
            }
        }

        while (engine.getStatus() == Status.ACTIVE) {
            System.out.println();
            System.out.println(engine.getPresentation());
            System.out.println("Make move: ");

            try {
                if (engine instanceof final TaflEngine taflEngine)
                    taflEngine.makeMove(new TaflMove(
                        new Vector(scanner.nextInt(), scanner.nextInt()),
                        new Vector(scanner.nextInt(), scanner.nextInt())
                    ));
                else if (engine instanceof final TicTacToeEngine ticTacToeEngine)
                    ticTacToeEngine.makeMove(new TicTacToeMove(new Vector(scanner.nextInt(), scanner.nextInt())));
            } catch (final InvalidMoveException exception) {
                System.out.println("Wrong move: " + exception.getMessage());
            } catch (final StatusException exception) {
                System.out.println(exception.getMessage());
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
