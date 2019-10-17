package de.hswt.fi.fileimport.service.excel.formats;

import de.hswt.fi.fileimport.service.excel.AbstractCalibrationExcelReader;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;


/**
 * The Class RTIABSciexExcelReader reads excel files with a defined content
 * layout (Export of ABSciex Software). Two sheets are read, one with the
 * calibration data and one with the target data (search targets).
 * 
 * The content layout or format is defined in an xml file in the config folder
 * called rti_absciex_config.xml.
 * 
 * There could be 100 calibration substances and 5000 target substances.
 * 
 * @author Marco Luthardt
 */
@Component
@Scope("prototype")
public class CalibrationExcelReader extends AbstractCalibrationExcelReader {

	private static final String ID = "de.hswt.fi.rti.excel.calibration";

	private static final String CALIBRATION_SHEET_NAME = "de.hswt.fi.rti.excel.calibration.sheet";

	private static final String CALIBRATION_SHEET_SUBSTANCE_NAME = "de.hswt.fi.rti.excel.calibration.substancename";

	private static final String CALIBRATION_SHEET_RT = "de.hswt.fi.rti.excel.calibration.rt";

	private static final String CALIBRATION_SHEET_SIGNAL = "de.hswt.fi.rti.excel.calibration.signal";

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
		return getClass().getResource("/config/rti_calibration_config.xml");
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
	protected String getCalibrationRtId() {
		return CALIBRATION_SHEET_RT;
	}

	@Override
	protected String getCalibrationSignalId() {
		return CALIBRATION_SHEET_SIGNAL;
	}
}
