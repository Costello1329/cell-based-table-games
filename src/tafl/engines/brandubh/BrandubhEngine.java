package tafl.engines.brandubh;

import tafl.TaflEngine;
import tafl.TaflSettings;

public class BrandubhEngine extends TaflEngine {
    public BrandubhEngine () { super(new BrandubhFieldFactory(), new TaflSettings(false, false)); }
}
