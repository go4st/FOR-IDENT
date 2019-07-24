package de.hswt.fi.processing.service.model;

import de.hswt.fi.beans.annotations.BeanColumn;
import de.hswt.fi.beans.annotations.BeanComponent;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@BeanComponent
public class ScoreSettings implements Serializable {

	private static final long serialVersionUID = -2809427387252527311L;

	@BeanColumn(selector = true, i18nId = I18nKeys.PROCESS_MODEL_MASS_SCORE_SETTINGS)
	private ProcessingUnitState massScreeningState;

	@BeanColumn(selector = true, i18nId = I18nKeys.PROCESS_MODEL_RTI_SCORE_SETTINGS)
	private ProcessingUnitState rtiScreeningState;

	@BeanColumn(selector = true, i18nId = I18nKeys.PROCESS_MODEL_MSMS_SCORE_SETTINGS)
	private ProcessingUnitState msmsState;

	@BeanColumn(selector = true, i18nId = I18nKeys.PROCESS_MODEL_TP_SCORE_SETTINGS)
	private ProcessingUnitState tpState;

	@BeanColumn(selector = true, i18nId = I18nKeys.PROCESS_MODEL_MASSBANK_SIMPLE_SCORE_SETTINGS)
	private ProcessingUnitState massBankSimpleState;

	public ScoreSettings() {
		massScreeningState = new ProcessingUnitState(ProcessingUnit.MASS_SCREENING, 0.0, true);
		rtiScreeningState = new ProcessingUnitState(ProcessingUnit.RTI_SCREENING, 0.0, true);
		tpState = new ProcessingUnitState(ProcessingUnit.TP, 0.0, false);
		msmsState = new ProcessingUnitState(ProcessingUnit.MSMS, 0.0, true);
		massBankSimpleState = new ProcessingUnitState(ProcessingUnit.MASSBANK_SIMPLE, 0.0, true);
	}

	public ProcessingUnitState getMassScreeningState() {
		return massScreeningState;
	}

	public void setMassScreeningState(ProcessingUnitState searchScreeningState) {
		this.massScreeningState = searchScreeningState;
	}

	public ProcessingUnitState getRtiScreeningState() {
		return rtiScreeningState;
	}

	public void setRtiScreeningState(ProcessingUnitState rtiScreeningState) {
		this.rtiScreeningState = rtiScreeningState;
	}

	public ProcessingUnitState getTpState() {
		return tpState;
	}

	public void setTpState(ProcessingUnitState tpState) {
		this.tpState = tpState;
	}

	public ProcessingUnitState getMsmsState() {
		return msmsState;
	}

	public void setMsmsState(ProcessingUnitState msmsState) {
		this.msmsState = msmsState;
	}

	public ProcessingUnitState getMassBankSimpleState() {
		return massBankSimpleState;
	}

	public void setMassBankSimpleState(ProcessingUnitState massBankSimpleState) {
		this.massBankSimpleState = massBankSimpleState;
	}

	public List<ProcessingUnitState> getProcessingUnitStates() {
		return new LinkedList<>(
				Arrays.asList(massScreeningState, rtiScreeningState, tpState, msmsState, massBankSimpleState));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ScoreSettings)) return false;
		ScoreSettings that = (ScoreSettings) o;
		return Objects.equals(massScreeningState, that.massScreeningState) &&
				Objects.equals(rtiScreeningState, that.rtiScreeningState) &&
				Objects.equals(msmsState, that.msmsState) &&
				Objects.equals(tpState, that.tpState) &&
				Objects.equals(massBankSimpleState, that.massBankSimpleState);
	}

	@Override
	public int hashCode() {
		return Objects.hash(massScreeningState, rtiScreeningState, msmsState, tpState, massBankSimpleState);
	}

	@Override
	public String toString() {
		return "ScoreSettings [searchScreeningState=" + massScreeningState + ", msmsState="
				+ msmsState + ", massBankSimpleState=" + massBankSimpleState + "]";
	}

}
