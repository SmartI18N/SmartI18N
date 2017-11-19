package org.smarti18n.editor.vaadin;

import com.vaadin.ui.UI;

import java.util.Arrays;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public final class VaadinUtils {

    private VaadinUtils() {
    }

    public static String buildNavigation(final String viewId, final String... params) {
        final StringBuilder navigationState = new StringBuilder(viewId);
        Arrays.asList(params).forEach(
                param -> navigationState.append("/").append(param)
        );

        return navigationState.toString();
    }

    public static void navigateTo(final String viewId, final String ... params) {
        UI.getCurrent().getNavigator().navigateTo(VaadinUtils.buildNavigation(viewId, params));
    }
}
