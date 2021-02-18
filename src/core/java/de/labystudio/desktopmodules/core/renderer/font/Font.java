package de.labystudio.desktopmodules.core.renderer.font;

/**
 * Font wrapper
 *
 * @author LabyStudio
 */
public class Font {

    private final String fontFamily;
    private final FontStyle style;
    private final int size;

    /**
     * Create font style
     *
     * @param fontFamily Font family name
     * @param style      Font style type
     * @param size       Font size
     */
    public Font(String fontFamily, FontStyle style, int size) {
        this.fontFamily = fontFamily;
        this.style = style;
        this.size = size;
    }

    public FontStyle getStyle() {
        return style;
    }

    public int getSize() {
        return size;
    }

    public String getFontFamily() {
        return fontFamily;
    }
}
