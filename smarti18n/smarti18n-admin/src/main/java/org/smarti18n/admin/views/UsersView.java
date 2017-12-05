package org.smarti18n.admin.views;

import java.util.List;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Grid;
import javax.annotation.PostConstruct;
import org.smarti18n.api.User;
import org.smarti18n.api.UserApi;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@UIScope
@SpringView(name = UsersView.VIEW_NAME)
public class UsersView extends AbstractView implements View {

    public static final String VIEW_NAME = "users";

    private final UserApi userApi;

    private Grid<User> grid;

    public UsersView(final UserApi userApi) {
        this.userApi = userApi;
    }

    @PostConstruct
    void init() {
        super.init(translate("smarti18n.admin.users.caption"));

        setSizeFull();

        this.grid = new Grid<>(User.class);
        this.grid.setSizeFull();
        this.grid.setColumns("mail", "vorname", "nachname", "company", "role");

        addComponent(this.grid);
        setExpandRatio(this.grid, 1);
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        final List<User> users = this.userApi.findAll();

        this.grid.setItems(users);
    }
}
