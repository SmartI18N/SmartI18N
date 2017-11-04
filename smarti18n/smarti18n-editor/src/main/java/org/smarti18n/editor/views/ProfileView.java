package org.smarti18n.editor.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.VerticalLayout;
import org.smarti18n.editor.vaadin.I18N;

import javax.annotation.PostConstruct;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@UIScope
@SpringView(name = ProfileView.VIEW_NAME)
public class ProfileView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "profile";

    private final I18N i18N;

    public ProfileView(final I18N i18N) {
        this.i18N = i18N;
    }

    @PostConstruct
    private void init() {
        setCaption(this.i18N.getMessage("smarti18n.editor.profile.caption"));
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    }

}
