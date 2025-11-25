package view;

import javax.swing.*;
import java.awt.*;

/**
 * Container view that stacks the bookmark view and map settings/opacity view
 * vertically using BoxLayout.
 */
public class BookmarkAndMapSettingsStructureView extends JPanel {

    public BookmarkAndMapSettingsStructureView() {
        // Use BoxLayout with Y_AXIS to stack components vertically
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    /**
     * Adds a component to this container.
     * Components will be stacked vertically from top to bottom.
     *
     * @param component the component to add
     */
    public void addComponent(JPanel component) {
        component.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(component);
        // Small gap between components for better spacing
        this.add(Box.createVerticalStrut(5));
    }
}

