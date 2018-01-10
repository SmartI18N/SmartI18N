package org.smarti18n.admin.views;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import javax.annotation.PostConstruct;
import org.smarti18n.api.UserApi;
import org.smarti18n.exceptions.UserExistException;
import org.smarti18n.models.User;
import org.smarti18n.vaadin.components.AddButton;
import org.smarti18n.vaadin.components.CancelButton;
import org.smarti18n.vaadin.components.FormWindow;
import org.smarti18n.vaadin.components.IconButton;
import org.smarti18n.vaadin.utils.I18N;
import org.smarti18n.vaadin.utils.VaadinExceptionHandler;

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
        super.init(
                translate("smarti18n.admin.users.caption"),
                newUserButton()
        );

        setSizeFull();

        this.grid = new Grid<>(User.class);
        this.grid.setSizeFull();
        this.grid.setColumns("mail", "vorname", "nachname", "company", "role");

        addComponent(this.grid);
        setExpandRatio(this.grid, 1);
    }

    private IconButton newUserButton() {
        return new IconButton(
                translate("smarti18n.admin.users.add-new-button"),
                VaadinIcons.FILE_ADD,
                e -> {
                    final FormWindow window = new FormWindow(
                            translate("smarti18n.admin.users.add-new-button")
                    );

                    final TextField textFieldMail = new TextField(I18N.translate("smarti18n.admin.users.new-user-mail"));
                    window.addFormComponent(textFieldMail);

                    final TextField textFieldPassword = new TextField(I18N.translate("smarti18n.admin.users.new-user-password"));
                    window.addFormComponent(textFieldPassword);

                    final AddButton addButton = new AddButton(registerUser(window, textFieldMail, textFieldPassword));
                    final CancelButton cancelButton = new CancelButton(clickEvent -> window.close());

                    window.addFormButtons(addButton, cancelButton);

                    getUI().addWindow(window);
                }
        );
    }

    private Button.ClickListener registerUser(final FormWindow window, final TextField textFieldMail, final TextField textFieldPassword) {
        return clickEvent -> {
            try {
                this.userApi.register(textFieldMail.getValue(), textFieldPassword.getValue());
            } catch (UserExistException e) {
                VaadinExceptionHandler.handleUserExistException();
            }

            reloadGrid();

            window.close();
        };
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        reloadGrid();
    }

    private void reloadGrid() {
        this.grid.setItems(
                this.userApi.findAll()
        );
    }
}
