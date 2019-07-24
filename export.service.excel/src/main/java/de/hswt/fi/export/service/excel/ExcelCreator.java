package de.hswt.fi.export.service.excel;

import de.hswt.fi.beans.BeanComponentMapper;
import de.hswt.fi.calculation.service.api.CalculationService;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
@Scope("prototype")
public class ExcelCreator {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelCreator.class);

	private CalculationService calculationService;

	private BeanComponentMapper mapper;

	@Autowired
	public ExcelCreator(CalculationService calculationService, BeanComponentMapper mapper) {
		this.calculationService = calculationService;
		this.mapper = mapper;
	}

	public Workbook createWorkbook(ExcelFileDefinition definition) {
		if (definition == null) {
			return null;
		}

		// create a creator on each request, to get non conflicts with calls
		// from different threads
		WorkbookCreator workbookCreator = new WorkbookCreator(mapper, calculationService);

		return workbookCreator.createWorkbook(definition);
	}

	public byte[] createExcelFile(ExcelFileDefinition definition) {
		return createFile(createWorkbook(definition));
	}

	private byte[] createFile(Workbook workbook) {

		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			workbook.write(outputStream);
			return outputStream.toByteArray();
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}

		return new byte[0];
	}
}
