package org.smarti18n.editor.views;

import org.smarti18n.editor.vaadin.AbstractView;
import org.smarti18n.editor.vaadin.I18N;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;

import javax.annotation.PostConstruct;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@UIScope
@SpringView(name = ProfileView.VIEW_NAME)
public class ProfileView extends AbstractView implements View {

    public static final String VIEW_NAME = "profile";

    public ProfileView(final I18N i18N) {
        super(i18N);
    }

    @PostConstruct
    private void init() {
        setCaption(translate("smarti18n.editor.profile.caption"));
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    }

}
