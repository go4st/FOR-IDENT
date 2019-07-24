package de.hswt.fi.calculation.service.api;

import java.util.List;

/**
 * @author Marco Luthardt
 */
public interface CalculationService {

	/**
	 * Checks if the tool box is available. It could not be available if no
	 * valid license file could be found by the tool box.
	 *
	 * @return true, if the tool box is available, otherwise false
	 */
	boolean isAvailable();

	/**
	 * Creates a structure image from the given smiles code. If no image could
	 * be created, e.g. the SMILES is not valid, null is returned.
	 *
	 * @param smiles the SMILES Code to create a image of
	 * @param width  the width of the image
	 * @param height the height of the image
	 * @return the image as byte array, or null if image could not be created
	 */
	byte[] getSmilesAsImage(String smiles, int width, int height);

	/**
	 * Returns the mass from formula.
	 *
	 * @param formula the formula to get the mass from
	 * @return the calculated mass
	 */
	Double getMassFromFormula(String formula);

	List<String> getInvalidSmiles(List<String> smiles);


	String getMolecularFormula(String smiles);

	String getInChiKey(String smiles);

	String getInChi(String smiles);
}
