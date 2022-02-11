package tafl.engines.tablut;

import tafl.TaflEngine;
import tafl.TaflSettings;

public class TablutEngine extends TaflEngine {
    public TablutEngine () { super(new TablutFieldFactory(), new TaflSettings(false, false)); }
}
