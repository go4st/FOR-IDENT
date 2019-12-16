package de.hswt.fi.search.service.index.model;

import de.hswt.fi.common.Ionisation;
import de.hswt.fi.common.StationaryPhase;

import java.io.Serializable;
import java.util.Objects;

public class IndexSettings implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final double FORMULA_DERIVED_MASSES_PPM = 0.01;
    private double ph;
    private double ppm;
    private Ionisation ionisation = Ionisation.NEUTRAL_IONISATION;
    private StationaryPhase stationaryPhase = StationaryPhase.C18;

    public IndexSettings() {
        ppm = 5.0;
        ph = 3.0;
    }

    public double getFormulaDerivedMassesPpm() {
        return FORMULA_DERIVED_MASSES_PPM;
    }

    public double getPpm() {
        return ppm;
    }

    public void setPpm(double ppm) {
        this.ppm = ppm;
    }

    public Ionisation getIonisation() {
        return ionisation;
    }

    public void setIonisation(Ionisation ionisation) {
        this.ionisation = ionisation;
    }

    public double getPh() {
        return ph;
    }

    public void setPh(double ph) {
        this.ph = ph;
    }

    public StationaryPhase getStationaryPhase() {
        return stationaryPhase;
    }

    public void setStationaryPhase(StationaryPhase stationaryPhase) {
        this.stationaryPhase = stationaryPhase;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndexSettings that = (IndexSettings) o;
        return Double.compare(that.ph, ph) == 0 &&
                Double.compare(that.ppm, ppm) == 0 &&
                ionisation == that.ionisation &&
                stationaryPhase == that.stationaryPhase;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ph, ppm, ionisation, stationaryPhase);
    }

    @Override
    public String toString() {
        return "RTIProcessingSettings [ppm=" + ppm + ", ionisation=" + ionisation
                + ", stationaryPhase=" + stationaryPhase + ", ph=" + ph + "]";
    }
}
