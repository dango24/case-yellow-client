package caseyellow.client.domain.analyze.model;

import lombok.Data;

@Data
public class DescriptionLocation {

    private String description;
    private Point center;

    public DescriptionLocation() {
        this("", new Point(0,0));
    }

    public DescriptionLocation(Point center) {
        this("SUCCESS", center);
    }

    public DescriptionLocation(String word, Point center) {
        this.description = word;
        this.center = center;
    }

    public static DescriptionLocation defaultDescriptionLocation() {
        return new DescriptionLocation();
    }
}
