package de.hswt.fi.search.service.mass.search.model;

/**
 * The Class JPADbDefs contains constants for several DB definitions like DB
 * field lengths and table names.
 * 
 * @author Marco Luthardt
 */
public class JpaPreferences {

	/** The Constant LENGTH_SOURCE. */
	public static final int LENGTH_SOURCE = 255;

	/** The Constant LENGTH_EDITOR. */
	public static final int LENGTH_EDITOR = 255;

	/** The Constant LENGTH_ADDITIONAL. */
	public static final int LENGTH_ADDITIONAL = 1000;

	/** The Constant LENGTH_NAME. */
	public static final int LENGTH_NAME = 3000;

	/** The Constant LENGTH_SMILES. */
	public static final int LENGTH_SMILES = 3000;

	/** The Constant LENGTH_IUPAC. */
	public static final int LENGTH_IUPAC = 3000;

	public static final int LENGTH_INCHY = 3000;

	public static final int LENGTH_INCHY_KEY = 27;

	/** The Constant LENGTH_CAS. */
	public static final int LENGTH_CAS = 1000;

	/** The Constant LENGTH_EC. */
	public static final int LENGTH_EC = 20;

	/** The Constant LENGTH_TONNAGE. */
	public static final int LENGTH_TONNAGE = 100;

	/** The Constant LENGTH_FORMULA. */
	public static final int LENGTH_FORMULA = 1000;

	/** The Constant TABLE_NAME_ENTRY. */
	public static final String TABLE_NAME_ENTRY = "tbl_substances";

	/** The Constant TABLE_NAME_LOGP. */
	public static final String TABLE_NAME_LOGP = "tbl_logp";

	/** The Constant TABLE_NAME_LOGD. */
	public static final String TABLE_NAME_LOGD = "tbl_logd";

	/** The Constant TABLE_NAME_WATER_SOLUBILITY. */
	public static final String TABLE_NAME_WATER_SOLUBILITY = "tbl_watersolubility";

	/** The Constant TABLE_NAME_ADDITIONAL_NAME. */
	public static final String TABLE_NAME_ADDITIONAL_NAME = "tbl_additionalnames";

	/** The Constant TABLE_NAME_MASSBANK_IDS. */
	public static final String TABLE_NAME_MASSBANK_IDS = "tbl_massbankids";

	/** The Constant TABLE_NAME_CATEGORIES. */
	public static final String TABLE_NAME_CATEGORIES = "tbl_categories";

	/** The Constant TABLE_NAME_SOURCE_LISTS. */
	public static final String TABLE_NAME_SOURCE_LISTS = "tbl_source_lists";
}
