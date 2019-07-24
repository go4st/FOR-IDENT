package de.hswt.fi.ui.vaadin.views.components.processing;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import de.hswt.fi.common.ValueFormatUtil;
import de.hswt.fi.model.I18nKeys;
import de.hswt.fi.model.Score;
import de.hswt.fi.processing.service.model.ProcessCandidate;
import de.hswt.fi.ui.vaadin.CustomValoTheme;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.PrototypeScope;
import org.vaadin.spring.i18n.I18N;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@SpringComponent
@PrototypeScope
public class ScoringDetailsComponent extends CssLayout {

    private static final long serialVersionUID = 1L;

    private final I18N i18n;

    @Autowired
    public ScoringDetailsComponent(I18N i18n) {
        this.i18n = i18n;
    }

    public void init(ProcessCandidate candidate) {

        Map<String, Score> scores = new LinkedHashMap<>();

        if (candidate.getMassSearchResult() != null) {
            scores.put(i18n.get(UIMessageKeys.SCORING_DETAILS_COMPONENT_SCORES_SEARCH), candidate.getMassSearchResult().getScore());
        }

        if (candidate.getRtiSearchResult() != null) {
            scores.put(i18n.get(UIMessageKeys.SCORING_DETAILS_COMPONENT_SCORES_RTI), candidate.getRtiSearchResult().getScore());
        }

        if (candidate.getMsMsCandidate() != null && candidate.getMsMsCandidate().getScore() != null) {
            scores.put(i18n.get(UIMessageKeys.SCORING_DETAILS_COMPONENT_SCORES_MSMS), candidate.getMsMsCandidate().getScore());
        }

        if (candidate.getMassBankSimpleScore() != null) {
            scores.put(i18n.get(UIMessageKeys.SCORING_DETAILS_COMPONENT_SCORES_MASSBANK), candidate.getMassBankSimpleScore());
        }

        addComponent(createHeaderRow());

        // Add score rows
        boolean highlightRow = false;

        for (String key : scores.keySet()) {

            Score score = scores.get(key);

            Component row = createScoreRow(key,
                    Double.toString(ValueFormatUtil.round(score.getScoreValue(), 2)),
                    Double.toString(ValueFormatUtil.round(score.getWeight(), 2)),
                    Double.toString(ValueFormatUtil.round(score.getWeightedValue(), 2)));

            if (highlightRow) {
                row.addStyleName(CustomValoTheme.BACKGROUND_COLOR_ROW_STRIPE);
                highlightRow = false;
            } else {
                highlightRow = true;
            }
            addComponent(row);
        }

        // Add line before bottom row
        getComponent(getComponentCount() - 1)
                .addStyleName(CustomValoTheme.BORDER_BOTTOM_COLOR_ALT3);

        addComponent(createFooterRow(
                Double.toString(ValueFormatUtil.round(candidate.getScore().getWeightedValue(), 2))));
    }

    private Component createHeaderRow() {
        Component headerRow = createScoreRow(i18n.get(UIMessageKeys.SCORING_DETAILS_COMPONENT_PROCESS_ROW_CAPTION),
                i18n.get(I18nKeys.SCORE_SCORE),
                i18n.get(I18nKeys.SCORE_WEIGHT),
                i18n.get(I18nKeys.SCORE_WEIGHTED_SCORE));
        headerRow.addStyleName(CustomValoTheme.BORDER_BOTTOM_COLOR_ALT3);
        return headerRow;
    }

    private Component createScoreRow(String... values) {

        HorizontalLayout layout = new HorizontalLayout();
        layout.addStyleName(CustomValoTheme.PADDING_HALF);
        layout.setWidth("100%");
        layout.setSpacing(false);
        layout.setMargin(false);

        Arrays.stream(values).forEach(value -> layout.addComponent(new Label(value)));

        return layout;
    }

    private Component createFooterRow(String value) {

        HorizontalLayout layout = new HorizontalLayout();
        layout.addStyleName(CustomValoTheme.PADDING_HALF);
        layout.setWidth("100%");
        layout.setSpacing(false);
        layout.setMargin(false);

        Label combinedLabel = new Label(i18n.get(UIMessageKeys.SCORING_DETAILS_COMPONENT_SCORES_COMBINED));
        combinedLabel.addStyleName(CustomValoTheme.LABEL_VERY_BOLD);
        layout.addComponent(combinedLabel);

        // Add dummy lables for correct positioning
        layout.addComponent(new Label());
        layout.addComponent(new Label());

        Label combinedScore = new Label(value);
        combinedScore.addStyleName(CustomValoTheme.LABEL_VERY_BOLD);
        layout.addComponent(combinedScore);

        return layout;
    }
}