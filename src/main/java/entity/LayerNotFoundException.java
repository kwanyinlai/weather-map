package entity;

public class LayerNotFoundException extends Exception {
    public LayerNotFoundException(String layer) {
        super("Layer " + layer + " not found!");
    }
}
