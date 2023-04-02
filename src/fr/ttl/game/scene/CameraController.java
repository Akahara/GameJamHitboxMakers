package fr.ttl.game.scene;

public class CameraController {

    private static final float HEIGHT_THRESHOLD = 5;    /* Range 0-inf, if the stack top falls by less than the threshold, it won't affect the camera */
    private static final float SMOOTHING_FACTOR = .01f; /* Range 0-1, the higher, the faster the camera moves */

    private double currentHeight;
    private double targetHeight;

    public void updateHeight(double maxPiecesHeight) {
        if(targetHeight < maxPiecesHeight)
            targetHeight = maxPiecesHeight;
        else if(targetHeight > maxPiecesHeight + HEIGHT_THRESHOLD)
            targetHeight = maxPiecesHeight;
        currentHeight += (targetHeight-currentHeight) * SMOOTHING_FACTOR;
    }

    /**
     * Returns the current camera height, that is, the bottom most y world position
     * that appears in the camera space. In range [0 .. +inf]
     */
    public double getHeight() {
        return Math.max(0, currentHeight - 3f);
    }

}
