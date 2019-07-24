package de.hswt.fi.ui.vaadin.handler;

import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import de.hswt.fi.beans.BeanComponentMapper;
import de.hswt.fi.beans.annotations.BeanColumn;
import de.hswt.fi.export.service.excel.ExcelSheetDefinition;
import de.hswt.fi.export.service.excel.ExcelSheetDefinition.ColumnDirection;
import de.hswt.fi.model.Feature;
import de.hswt.fi.processing.service.model.ProcessCandidate;
import de.hswt.fi.processing.service.model.ProcessingSettings;
import de.hswt.fi.processing.service.model.ScoreSettings;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.windows.DownloadWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus.ViewEventBus;
import org.vaadin.spring.i18n.I18N;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class AbstractDownloadHandler<T> extends AbstractWindowHandler<ViewEventBus> {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDownloadHandler.class);

    private DownloadWindow<T> downloadWindow;

    private BeanComponentMapper mapper;

    private I18N i18n;

    protected void downloadRecords(List<T> entries, List<String> selectedColumnIds, List<String> possibleColumnIds,
                                   String fileName) {
		downloadWindow.setEntries(entries);
        downloadWindow.setPossibleExportColumns(
                mapper.getPropertyCaptions(entries.iterator().next(), possibleColumnIds));
        downloadWindow.setExportColumnIds(selectedColumnIds);
        downloadWindow.setFileName(fileName);
        downloadWindow.setSheetName(
        		i18n.get(UIMessageKeys.RESULTS_DOWNLOAD_HANDLER_EXCEL_SPREADSHEET_DEFINITION_RESULTS));
        UI.getCurrent().addWindow(downloadWindow);
    }

    protected void downloadRecords(List<T> entries, List<String> selectedColumnIds, List<String> possibleColumnIds,
                                   List<ExcelSheetDefinition> optionalSheets, String sheetName, Path sourceFilePath,
                                   boolean addSourceFile, boolean enableSourcesCheckbox) {
        downloadWindow.setEntries(entries);
        downloadWindow.setPossibleExportColumns(
                mapper.getPropertyCaptions(entries.iterator().next(), possibleColumnIds));
        downloadWindow.setExportColumnIds(selectedColumnIds);
        if (optionalSheets != null) {
            downloadWindow.setOptionalSheets(optionalSheets);
        }

        downloadWindow.setSourceFilePath(sourceFilePath, addSourceFile);

        if (sourceFilePath != null) {
            downloadWindow.setFileName(sourceFilePath.getFileName().toString());
        }
        downloadWindow.setSheetName(sheetName);
        downloadWindow.setIncludeSourcesEnabled(enableSourcesCheckbox);

        UI.getCurrent().addWindow(downloadWindow);
    }

    protected ExcelSheetDefinition createParameterSheet(ProcessingSettings settings) {
        ExcelSheetDefinition<ProcessingSettings> parameterSheet = new ExcelSheetDefinition<>(
                i18n.get(UIMessageKeys.RESULTS_DOWNLOAD_HANDLER_EXCEL_SPREADSHEET_DEFINITION_PARAMETER));
        parameterSheet.setColumnDirection(ColumnDirection.HORIZONTAL);
        parameterSheet.setData(Collections.singletonList(settings));
        return parameterSheet;
    }

    protected ExcelSheetDefinition createScoreSettingsSheet(ScoreSettings scoreSettings) {
        ExcelSheetDefinition<ScoreSettings> scoreSettingsSheet = new ExcelSheetDefinition<>(
                i18n.get(UIMessageKeys.RESULTS_DOWNLOAD_HANDLER_EXCEL_SPREADSHEET_DEFINITION_SCORE_SETTINGS));
        scoreSettingsSheet.setColumnDirection(ColumnDirection.HORIZONTAL);
        scoreSettingsSheet.setData(Collections.singletonList(scoreSettings));
        return scoreSettingsSheet;
    }

    // Results pre processing
    protected void filterNullBeanColumns(List<String> possibleColumnsProperties, ProcessCandidate processCandidate) {

        for (Field field : ProcessCandidate.class.getDeclaredFields()) {
            if (field.isAnnotationPresent(BeanColumn.class)) {
                removeNullBeanColumn(possibleColumnsProperties, processCandidate, field);
            }
        }
    }

    private void removeNullBeanColumn(List<String> possibleColumnsProperties, ProcessCandidate processCandidate, Field field) {
        field.setAccessible(true);
        try {
            if (field.get(processCandidate) == null) {
                possibleColumnsProperties.removeAll(possibleColumnsProperties.stream()
                        .filter(p -> p.startsWith(field.getName())).collect(Collectors.toList()));
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            LOGGER.error(e.getMessage());
        } finally {
            field.setAccessible(false);
        }
    }

    // Non Results pre processing
    protected List<String> getResultTargetIdentifier(List<ProcessCandidate> resultsContainer) {
        return resultsContainer.stream().filter(Objects::nonNull)
                .map(ProcessCandidate::getTargetIdentifier)
                .collect(Collectors.toList());
    }

    protected List<String> getAllTargetIdentifier(List<Feature> features) {
        return features.stream().map(Feature::getIdentifier).collect(Collectors.toList());
    }

    @Override
    public void windowClose(CloseEvent e) {
    }

    @Override
    protected Window getWindow() {
        return downloadWindow;
    }

    @Autowired
    public void setDownloadWindow(DownloadWindow<T> downloadWindow) {
        this.downloadWindow = downloadWindow;
    }

    @Autowired
    public void setMapper(BeanComponentMapper mapper) {
		this.mapper = mapper;
    }

    @Autowired
    public void setI18n(I18N i18n) {
        this.i18n = i18n;
    }
}
