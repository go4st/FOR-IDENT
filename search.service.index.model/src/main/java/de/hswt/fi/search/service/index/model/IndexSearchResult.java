package de.hswt.fi.search.service.index.model;

import de.hswt.fi.model.Score;
import de.hswt.fi.search.service.mass.search.model.Entry;

public interface IndexSearchResult {

    String getID();

    Entry getEntry();

    void setEntry(Entry entry);

    String getTargetIdentifier();

    void setTargetIdentifier(String targetIdentifier);

    Double getRetentionTime();

    void setRetentionTime(Double targetRetentionTime);

    Double getRetentionTimeIndex();

    void setRetentionTimeIndex(Double retentionTimeIndex);

    Double getRetentionTimeSignal();

    void setRetentionTimeSignal(Double retentionTimeSignal);

    Double getDeltaRetentionTimeSignal();

    void setDeltaRetentionTimeSignal(Double deltaRetentionTimeSignal);

    double getTargetMass();

    double getDeltaMass();

    Score getScore();

    void setScore(Score score);

    double getIonisation();

    double getPpm();

    boolean isFirst();

    void setFirst(boolean isFirst);

    boolean isLast();

    void setLast(boolean isLast);

    boolean isAvailable();
}
