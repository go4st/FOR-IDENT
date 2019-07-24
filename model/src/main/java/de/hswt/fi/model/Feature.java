package de.hswt.fi.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.hswt.fi.beans.annotations.BeanColumn;
import de.hswt.fi.beans.annotations.BeanComponent;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@BeanComponent
@JsonDeserialize(builder = Feature.Builder.class)
public class Feature implements Serializable {

    private static final long serialVersionUID = 1L;


    private static final Double DEFAULT_RELATIVE_FACTOR = 0.0;

    @BeanColumn(i18nId = I18nKeys.FEATURE_IDENTIFIER, selector = true)
    private final String identifier;

    @BeanColumn(i18nId = I18nKeys.FEATURE_PRECURSOR_MASS, format = "%.4f", selector = true)
    private Double precursorMass;

    @BeanColumn(i18nId = I18nKeys.FEATURE_FORMULA, selector = true)
    private String neutralFormula;

    @BeanColumn(i18nId = I18nKeys.FEATURE_RTI, selector = true, format = "%.1f")
    private Double retentionTimeIndex;

    @BeanColumn(i18nId = I18nKeys.FEATURE_RT, format = "%.2f", selector = true)
    private Double retentionTime;

    private Double logD;

    @BeanColumn(i18nId = I18nKeys.FEATURE_FORMULA_DERIVED_MASS, format = "%.4f", selector = true)
    private Double formulaDerivedMass;

    @BeanColumn(i18nId = I18nKeys.FEATURE_NEUTRAL_MASS, format = "%.4f", selector = true)
    private Double neutralMass;

    private boolean massCalculated;

    private List<Peak> peaks;

    private Double relativeFactor;

    private Feature(Builder builder) {
        this.identifier = builder.identifier;
        this.precursorMass = builder.precursorMass;
        this.neutralFormula = builder.neutralFormula;
        this.neutralMass = builder.neutralMass;
        this.retentionTime = builder.retentionTime;
        this.retentionTimeIndex = builder.retentionTimeIndex;
        this.logD = builder.logD;
        this.formulaDerivedMass = builder.formulaDerivedMass;
        this.neutralFormula = builder.neutralFormula;
        this.massCalculated = builder.massCalculated;
        this.peaks = builder.peaks;
        this.relativeFactor = builder.relativeFactor;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Double getRetentionTimeIndex() {
        return retentionTimeIndex;
    }

    public void setRetentionTimeIndex(Double retentionTimeIndex) {
        this.retentionTimeIndex = retentionTimeIndex;
    }

    public Double getRetentionTime() {
        return retentionTime;
    }

    public void setRetentionTime(Double retentionTime) {
        this.retentionTime = retentionTime;
    }

    public Double getLogD() {
        return logD;
    }

    public String getNeutralFormula() {
        return neutralFormula;
    }

    public void setNeutralFormula(String neutralFormula) {
        this.neutralFormula = neutralFormula;
    }

    public void setLogD(Double logD) {
        this.logD = logD;
    }

    public boolean isMassCalculated() {
        return massCalculated;
    }

    public void setPrecursorMass(Double precursorMass) {
        this.precursorMass = precursorMass;
    }


    public Double getPrecursorMass() {
        return precursorMass;
    }

    public Double getFormulaDerivedMass() {
        return formulaDerivedMass;
    }

    public void setFormulaDerivedMass(Double formulaDerivedMass) {
        this.formulaDerivedMass = formulaDerivedMass;
        this.massCalculated = true;
    }

    public Double getNeutralMass() {
        return neutralMass;
    }

    public void setNeutralMass(Double neutralMass) {
        this.neutralMass = neutralMass;
    }

    public List<Peak> getPeaks() {
        return peaks;
    }

    public void setPeaks(List<Peak> peaks) {
        this.peaks = peaks;
    }

    public Double getRelativeFactor() {
        return relativeFactor;
    }

    public void setRelativeFactor(Double relativeFactor) {
        this.relativeFactor = relativeFactor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feature feature = (Feature) o;
        return massCalculated == feature.massCalculated &&
                Objects.equals(identifier, feature.identifier) &&
                Objects.equals(precursorMass, feature.precursorMass) &&
                Objects.equals(neutralFormula, feature.neutralFormula) &&
                Objects.equals(retentionTimeIndex, feature.retentionTimeIndex) &&
                Objects.equals(retentionTime, feature.retentionTime) &&
                Objects.equals(logD, feature.logD) &&
                Objects.equals(formulaDerivedMass, feature.formulaDerivedMass) &&
                Objects.equals(neutralMass, feature.neutralMass) &&
                Objects.equals(peaks, feature.peaks) &&
                Objects.equals(relativeFactor, feature.relativeFactor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, precursorMass, neutralFormula, retentionTimeIndex, retentionTime, logD,
                formulaDerivedMass, neutralMass, massCalculated, peaks, relativeFactor);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Feature [identifier=");
        builder.append(identifier);
        builder.append(", formula=");
        builder.append(neutralFormula);
        builder.append(", formulaDerivedMass=");
        builder.append(formulaDerivedMass);
        builder.append(", retentionTimeIndex=");
        builder.append(retentionTimeIndex);
        builder.append(", retentionTime=");
        builder.append(retentionTime);
        builder.append(", logD=");
        builder.append(logD);
        builder.append(", precursorMass=");
        builder.append(precursorMass);
        builder.append(", neutralMass=");
        builder.append(neutralMass);
        builder.append(", massCalculated=");
        builder.append(massCalculated);
        builder.append(", peaks=");
        builder.append(peaks);
        builder.append(", relativeFactor=");
        builder.append(relativeFactor);
        builder.append("]");
        return builder.toString();
    }


    public static class Builder {


        // Mandatory fields
        private String identifier;

        private Double precursorMass;

        // Optional fields
        private String neutralFormula;

        private Double retentionTimeIndex;

        private Double retentionTime;

        private Double logD;

        private Double formulaDerivedMass;

        private Double neutralMass;

        private boolean massCalculated;

        @JsonProperty("fragments")
        private List<Peak> peaks;

        private Double relativeFactor;


        @JsonCreator
        public Builder(@JsonProperty("identifier") String identifier,
                       @JsonProperty("precursorMass") Double precursorMass) {

            if (identifier == null || identifier.isEmpty()) {
                this.identifier = "Target" + UUID.randomUUID().toString();
            } else {
                this.identifier = identifier;
            }
            this.precursorMass = precursorMass;
            this.relativeFactor = DEFAULT_RELATIVE_FACTOR;
            this.peaks = Collections.emptyList();
        }

        public Builder withNeutralFormula(String neutralFormula) {
            this.neutralFormula = neutralFormula;
            return this;
        }

        public Builder withRetentionTimeIndex(Double retentionTimeIndex) {
            this.retentionTimeIndex = retentionTimeIndex;
            return this;
        }

        public Builder withRetentionTime(Double retentionTime) {
            this.retentionTime = retentionTime;
            return this;
        }

        public Builder withLogD(Double logD) {
            this.logD = logD;
            return this;
        }

        public Builder withFormulaDerivedMass(Double formulaDerivedMass) {
            this.formulaDerivedMass = formulaDerivedMass;
            this.massCalculated = true;
            return this;
        }

        public Builder withNeutralMass(Double neutralMass) {
            this.neutralMass = neutralMass;
            return this;
        }

        public Builder withMassCalculated(boolean massCalculated) {
            this.massCalculated = massCalculated;
            return this;
        }

        public Builder withPeaks(List<Peak> peaks) {
            this.peaks = peaks;
            return this;
        }

        public Builder withRelativeFactor(Double relativeFactor) {
            this.relativeFactor = relativeFactor;
            return this;
        }

        public Feature build() {
            return new Feature(this);
        }

    }

}
