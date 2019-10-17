package de.hswt.fi.search.service.index.model;

import de.hswt.fi.model.Score;
import de.hswt.fi.search.service.mass.search.model.Entry;

public interface IndexSearchResult {

    String getID();

    Entry getEntry();

    Double getTargetRetentionTime();

    double getTargetMass();

    double getDeltaMass();

    Score getScore();

    void setScore(Score score);

    double getIonisation();

    String getStationaryPhase();

    boolean isCalculatedTargetMass();

    double getPpm();

    boolean isFirst();

    void setFirst(boolean isFirst);

    boolean isLast();

    void setLast(boolean isLast);

    boolean isAvailable();
}
