package org.smarti18n.editor.gateway;

import org.smarti18n.api.Project;

import com.vaadin.ui.Window;

public interface ImportExportHandler {

    String getName();

    boolean hasImport();
    boolean hasExport();

    Window createImportWindow(final Project project);
    Window createExportWindow(final Project project);
}
