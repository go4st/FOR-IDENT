CREATE INDEX index_tbl_substances_eid
  ON tbl_substances
  USING btree
  (eid);

CREATE INDEX tbl_substances_mass_value
  ON tbl_substances
  USING btree
  (mass_value);

CREATE UNIQUE INDEX tbl_substances_eid
  ON tbl_substances
  USING btree
  (eid);

CREATE INDEX tbl_substances_upper_formula_value
  ON tbl_substances
  USING btree
  (upper(formula_value::text) COLLATE pg_catalog."default");

CREATE INDEX tbl_substances_upper_iupac_value
  ON tbl_substances
  USING btree
  (upper(iupac_value::text) COLLATE pg_catalog."default");

CREATE INDEX tbl_substances_upper_name_value
  ON tbl_substances
  USING btree
  (upper(name_value::text) COLLATE pg_catalog."default");

CREATE INDEX tbl_substances_upper_smiles_value
  ON tbl_substances
  USING btree
  (upper(smiles_value::text) COLLATE pg_catalog."default");

CREATE INDEX tbl_additionalnames_substances_eid
  ON tbl_additionalnames
  USING btree
  (substances_eid);

CREATE INDEX tbl_additionalnames_value
  ON tbl_additionalnames
  USING btree
  (upper(value COLLATE pg_catalog."default"));