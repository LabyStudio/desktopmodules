package de.labystudio.desktopmodules.core.renderer.font;

/**
 * String alignment position
 *
 * @author LabyStudio
 */
public enum StringAlignment {
    /**
     * Left alignment
     */
    LEFT,

    /**
     * Centered alignment
     */
    CENTERED,

    /**
     * Right alignment
     */
    RIGHT;

    /**
     * Convert rightbound state to string aligntment
     *
     * @param rightbound Set the alignment to RIGHT
     * @return StringAlignment
     */
    public StringAlignment from(boolean rightbound) {
        return rightbound ? RIGHT : LEFT;
    }
}