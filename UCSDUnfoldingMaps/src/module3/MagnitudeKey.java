package module3;

/**
 * Created by hhoang on 9/2/2017.
 */
public class MagnitudeKey {
    private String key;
    private int color;
    private float radius;
    private String description;

    public static final String MAGNITUDE_KEY_SMALL = "Small";
    public static final String MAGNITUDE_KEY_LIGHT = "Light";
    public static final String MAGNITUDE_KEY_MODERATE = "Moderate";

    public MagnitudeKey(String key, int color, float radius, String description) {
        this.key = key;
        this.color = color;
        this.radius = radius;
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
