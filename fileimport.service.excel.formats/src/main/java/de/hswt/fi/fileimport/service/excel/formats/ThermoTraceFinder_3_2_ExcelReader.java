package de.hswt.fi.fileimport.service.excel.formats;

import de.hswt.fi.fileimport.service.excel.AbstractExcelReader;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;


/**
 * The Class RTIThermoTraceFinder_3_2_ExcelReader reads excel files with a
 * defined content layout. Two sheets are read, one with the calibration data
 * and one with the target data (search targets).
 * 
 * The content layout or format is defined in an xml file in the config folder
 * called rti_waters_markerlyncs_marker_config.xml
 * 
 * There could be 100 calibration substances and 5000 target substances.
 * 
 * @author Marco Luthardt
 */
@Component
@Scope("prototype")
public class ThermoTraceFinder_3_2_ExcelReader extends AbstractExcelReader {

	/** The Constant ID. */
	private static final String ID = "de.hswt.fi.rti.excel.thermo.tracefinder";

	/** The Constant TARGETS_SHEET_NAME. */
	private static final String TARGETS_SHEET_NAME = "de.hswt.fi.rti.excel.thermo.targets";

	/** The Constant TARGETS_SHEET_IDENTIFIER. */
	private static final String TARGETS_SHEET_IDENTIFIER = "de.hswt.fi.rti.excel.thermo.target.label";

	/** The Constant TARGETS_SHEET_MASS_VALUE. */
	private static final String TARGETS_SHEET_MASS_VALUE = "de.hswt.fi.rti.excel.thermo.target.mass";

	/** The Constant TARGETS_SHEET_FORMULA. */
	private static final String TARGETS_SHEET_FORMULA = "de.hswt.fi.rti.excel.thermo.target.formula";

	/** The Constant TARGETS_SHEET_RT. */
	private static final String TARGETS_SHEET_RT = "de.hswt.fi.rti.excel.thermo.target.rt";

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
		return getClass().getResource("/config/rti_thermo_tracefinder_3_2_config.xml");
	}

	@Override
	protected String getTargetsSheetNameId() {
		return TARGETS_SHEET_NAME;
	}

	@Override
	protected String getTargetsIdentifierId() {
		return TARGETS_SHEET_IDENTIFIER;
	}

	@Override
	protected String getTargetsMassId() {
		return TARGETS_SHEET_MASS_VALUE;
	}

	@Override
	protected String getTargetsFormulaId() {
		return TARGETS_SHEET_FORMULA;
	}

	@Override
	protected String getTargetsRtId() {
		return TARGETS_SHEET_RT;
	}
}
