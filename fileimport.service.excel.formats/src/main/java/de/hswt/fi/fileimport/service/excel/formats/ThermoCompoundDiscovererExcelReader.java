package de.hswt.fi.fileimport.service.excel.formats;

import de.hswt.fi.fileimport.service.excel.AbstractExcelReader;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;

@Component
@Scope("prototype")
public class ThermoCompoundDiscovererExcelReader extends AbstractExcelReader {

	/** The Constant ID. */
	private static final String ID = "de.hswt.fi.si.excel.thermo.cd";

	/** The Constant TARGETS_SHEET_NAME. */
	private static final String TARGETS_SHEET_NAME = "de.hswt.fi.si.excel.thermo.cd.sheet";

	/** The Constant TARGETS_SHEET_IDENTIFIER. */
	private static final String TARGETS_SHEET_IDENTIFIER = "de.hswt.fi.si.excel.thermo.cd.col.label";

	/** The Constant TARGETS_SHEET_MASS_VALUE. */
	private static final String TARGETS_SHEET_MASS_VALUE = "de.hswt.fi.si.excel.thermo.cd.col.mass";

	/** The Constant TARGETS_SHEET_RT. */
	private static final String TARGETS_SHEET_RT = "de.hswt.fi.si.excel.thermo.cd.col.rt";

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
		return getClass().getResource("/config/thermo_compound_discoverer_2_0_config.xml");
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
		return null;
	}

	@Override
	protected String getTargetsRtId() {
		return TARGETS_SHEET_RT;
	}
}
