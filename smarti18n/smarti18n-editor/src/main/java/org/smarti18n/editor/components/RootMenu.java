package org.smarti18n.editor.components;

import org.smarti18n.editor.vaadin.VaadinUtils;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

public class RootMenu extends HorizontalLayout {

    public RootMenu(final String menuTitleText) {
        setPrimaryStyleName(ValoTheme.MENU_ROOT);
        setWidth(100, Unit.PERCENTAGE);

        final Label menuTitle = new Label(menuTitleText);

        addComponent(menuTitle);
    }

    public RootMenu addMenuItem(final String menuItemText, final Resource icon, final String viewId) {
        return addMenuItem(menuItemText, icon, () -> VaadinUtils.navigateTo(viewId));
    }

    public RootMenu addMenuItem(
            final String menuItemText,
            final Resource icon,
            final MenuItemClickListener clickListener) {

        final Button menuItem = new Button(menuItemText);
//        menuItem.setIcon(icon);
        menuItem.setSizeUndefined();
        menuItem.setPrimaryStyleName(ValoTheme.MENU_ITEM);
        menuItem.addClickListener(event -> clickListener.click());
        addComponent(menuItem);

        return this;
    }

    public interface MenuItemClickListener {
        void click();
    }
}
