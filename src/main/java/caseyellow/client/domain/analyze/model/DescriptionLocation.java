package caseyellow.client.domain.analyze.model;

public class DescriptionLocation {

    private String description;
    private Point center;

    public DescriptionLocation() {
        this("", new Point(0,0));
    }

    public DescriptionLocation(String word, Point center) {
        this.description = word;
        this.center = center;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    public static DescriptionLocation defaultDescriptionLocation() {
        return new DescriptionLocation();
    }
}
