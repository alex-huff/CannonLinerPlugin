package dev.phonis.cannonliner.trace;

public enum ParticleType {

    SAND, PLAYER, TNT, TNTENDPOS, SANDENDPOS;

    final static Color colorSand = new Color(255, 255, 0);
    final static Color colorTNT = new Color(255, 0, 0);
    final static Color colorExplosion = new Color(255, 0, 255);
    final static Color colorPlayer = new Color(0, 0, 255);

    public Color getRGB() {
        if (this == SAND) {
            return colorSand;
        } else if (this == TNT) {
            return colorTNT;
        } else if (this == PLAYER) {
            return colorPlayer;
        } else {
            return colorExplosion;
        }
    }

}
