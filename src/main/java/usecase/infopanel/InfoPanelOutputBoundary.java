package usecase.infopanel;

/**
 * Output boundary for presenting info panel state to the UI layer.
 * Implementations decide how loading, success, and error states are shown.
 */
public interface InfoPanelOutputBoundary {
    /**
     * Presents a loading state while new info panel data is being fetched.
     */
    void presentLoading();

    /**
     * Presents freshly fetched info panel data.
     *
     * @param data the data to be shown on the info panel
     */
    void present(InfoPanelOutputData data);

    /**
     * Presents an error state for the info panel.
     *
     * @param error the error to display
     */
    void presentError(InfoPanelError error);
}
