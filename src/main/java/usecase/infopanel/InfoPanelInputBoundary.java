package usecase.infopanel;

/**
 * Input boundary for the info panel use case.
 * Receives requests to load or update info panel data.
 */
public interface InfoPanelInputBoundary {
    /**
     * Executes the info panel use case with the given input data.
     *
     * @param req the input data describing the requested info panel update
     */
    void execute(InfoPanelInputData req);
}
