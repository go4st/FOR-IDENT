package de.hswt.fi.fileimport.service.excel.formats;

import de.hswt.fi.fileimport.service.excel.AbstractProcessExcelReader;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;


@Component
@Scope("prototype")
public class SciexMSMSExcelReader extends AbstractProcessExcelReader {

	private static final String ID = "de.hswt.fi.rti.excel.sciex.msms.reader";

	private static final String PEAKS_SHEET_NAME = "de.hswt.fi.rti.excel.sciex.msms";

	private static final String PEAKS_SHEET_MASS = "de.hswt.fi.rti.excel.sciex.msms.mass";

	private static final String PEAKS_SHEET_FORMULA = "de.hswt.fi.rti.excel.sciex.msms.formula";

	private static final String PEAKS_SHEET_PEAK_NAMES = "de.hswt.fi.rti.excel.sciex.msms.peaknames";

	private static final String PEAKS_SHEET_TARGET_NAMES = "de.hswt.fi.rti.excel.sciex.msms.targetnames";
	
	private static final String PEAK_SHEET_RT = "de.hswt.fi.rti.excel.sciex.msms.rt";
	
	private static final String CALIBRATION_SHEET_NAME = "de.hswt.fi.rti.excel.sciex.msms.calibration";

	private static final String CALIBRATION_SHEET_SUBSTANCE_NAME = "de.hswt.fi.rti.excel.sciex.msms.calibration.substancename";

	private static final String CALIBRATION_SHEET_RT = "de.hswt.fi.rti.excel.sciex.msms.calibration.rt";

	private static final String CALIBRATION_SHEET_LOGD = "de.hswt.fi.rti.excel.sciex.msms.calibration.logD";


	/*
	 * (non-Javadoc)
	 *
	 * @see de.hswt.filehandler.api.Reader#getID()
	 */
	@Override
	public String getID() {
		return ID;
	}

	@Override
	protected URL getConfigUrl() {
		return getClass().getResource("/config/rti_sciex_msms_config.xml");
	}

	@Override
	protected String getPeakListSheetNameId() {
		return PEAKS_SHEET_NAME;
	}

	@Override
	protected String getPeakListNamesId() {
		return PEAKS_SHEET_PEAK_NAMES;
	}

	@Override
	protected String getPeakListTargetNamesId() {
		return PEAKS_SHEET_TARGET_NAMES;
	}

	@Override
	protected String getPeakListMassId() {
		return PEAKS_SHEET_MASS;
	}

	@Override
	protected String getPeakListFormulaId() {
		return PEAKS_SHEET_FORMULA;
	}

	@Override
	protected String getCalibrationSheetNameId() {
		return CALIBRATION_SHEET_NAME;
	}

	@Override
	protected String getCalibrationSubstanceId() {
		return CALIBRATION_SHEET_SUBSTANCE_NAME;
	}

	@Override
	protected String getPeakRtId() {
		return PEAK_SHEET_RT;
	}

	@Override
	protected String getCalibrationLogDId() {
		return CALIBRATION_SHEET_LOGD;
	}

	@Override
	protected String getCalibrationSheetRtId() {
		return CALIBRATION_SHEET_RT;
	}
}
