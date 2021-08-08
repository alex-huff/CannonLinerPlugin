package phonis.cannonliner.util;

public class Offset {

    private final float xOff;
    private final float yOff;
    private final float zOff;

    public Offset(float xOff, float yOff, float zOff) {
        this.xOff = xOff;
        this.yOff = yOff;
        this.zOff = zOff;
    }

    public float getX() {
        return this.xOff;
    }

    public float getY() {
        return this.yOff;
    }

    public float getZ() {
        return this.zOff;
    }

}
