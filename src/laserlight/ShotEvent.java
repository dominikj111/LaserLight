package laserlight;

import java.util.ArrayList;

interface ShotEvent {
    void targetDestroyed(Target target, ArrayList<Target> otherTargets);
}
