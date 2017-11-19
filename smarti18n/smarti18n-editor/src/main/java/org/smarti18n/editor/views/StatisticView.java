package org.smarti18n.editor.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import javax.annotation.PostConstruct;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@UIScope
@SpringView(name = StatisticView.VIEW_NAME)
public class StatisticView extends AbstractView implements View {

    public static final String VIEW_NAME = "statistic";

    public StatisticView() {

    }

    @PostConstruct
    void init() {
        super.init(translate("smarti18n.editor.statistic.caption"));
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }

}
