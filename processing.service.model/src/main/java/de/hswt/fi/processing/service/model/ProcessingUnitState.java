package de.hswt.fi.processing.service.model;

import de.hswt.fi.beans.annotations.BeanColumn;

import java.io.Serializable;
import java.util.Objects;

public class ProcessingUnitState implements Serializable {

	private static final long serialVersionUID = 1L;

	public enum UnitState {
		IDLE, PROCESSING, FINISHED;
	}

	private ProcessingUnit processUnit;

	@BeanColumn(caption = "Weight", format = "%.2f")
	private double scoreWeight;

	@BeanColumn(caption = "Enabled")
	private boolean enabled;

	@BeanColumn(caption = "Data Available")
	private boolean dataAvailable;

	@BeanColumn(caption = "Scoreable")
	private final boolean scoreable;

	private boolean execute;

	private UnitState unitState;

	public ProcessingUnitState(ProcessingUnit processUnit, double scoreWeight, boolean scoreable) {
		this.processUnit = processUnit;
		this.scoreWeight = scoreWeight;
		this.scoreable = scoreable;
		enabled = false;
		dataAvailable = false;
		execute = false;
		unitState = UnitState.IDLE;
	}

	public ProcessingUnit getProcessUnit() {
		return processUnit;
	}

	public void setProcessUnit(ProcessingUnit processUnit) {
		this.processUnit = processUnit;
	}

	public double getScoreWeight() {
		return scoreWeight;
	}

	public void setScoreWeight(double scoreWeight) {
		if (scoreable) {
			this.scoreWeight = scoreWeight;
		}
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled && dataAvailable;
		updateExecute();
	}

	public boolean isDataAvailable() {
		return dataAvailable;
	}

	public void setDataAvailable(boolean dataAvailable) {
		this.dataAvailable = dataAvailable;
		this.enabled = dataAvailable;
		updateExecute();
	}

	public boolean isScoreable() {
		return scoreable;
	}

	public boolean isExecute() {
		return execute;
	}

	public UnitState getUnitState() {
		return unitState;
	}

	public void setUnitState(UnitState unitState) {
		this.unitState = unitState;
	}

	private void updateExecute() {
		execute = enabled && dataAvailable;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ProcessingUnitState)) return false;
		ProcessingUnitState that = (ProcessingUnitState) o;
		return Double.compare(that.scoreWeight, scoreWeight) == 0 &&
				enabled == that.enabled &&
				dataAvailable == that.dataAvailable &&
				scoreable == that.scoreable &&
				execute == that.execute &&
				processUnit == that.processUnit &&
				unitState == that.unitState;
	}

	@Override
	public int hashCode() {
		return Objects.hash(processUnit, scoreWeight, enabled, dataAvailable, scoreable, execute, unitState);
	}

	@Override
	public String toString() {
		return "ProcessUnitState ["
				+ (processUnit != null ? "processUnit=" + processUnit + ", " : "") + "scoreWeight="
				+ scoreWeight + ", enabled=" + enabled + ", execute=" + execute + ", dataAvailable="
				+ dataAvailable + ", " + (unitState != null ? "unitState=" + unitState : "") + "]";
	}
}
