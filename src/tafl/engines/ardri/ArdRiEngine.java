package tafl.engines.ardri;

import tafl.TaflEngine;
import tafl.TaflSettings;

public class ArdRiEngine extends TaflEngine {
    public ArdRiEngine () { super(new ArdRiFieldFactory(), new TaflSettings(true, true)); }
}
