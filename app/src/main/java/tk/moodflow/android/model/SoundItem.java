package tk.moodflow.android.model;

import org.parceler.Parcel;

/**
 * Created by yurkiv on 21.05.2015.
 */

@Parcel
public class SoundItem {
    private int id;
    private String name;
    private int image;
    private int paletteColor;

    public SoundItem() {
    }

    public SoundItem(String name, int image) {
        this.name = name;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getPaletteColor() {
        return paletteColor;
    }

    public void setPaletteColor(int paletteColor) {
        this.paletteColor = paletteColor;
    }
}
