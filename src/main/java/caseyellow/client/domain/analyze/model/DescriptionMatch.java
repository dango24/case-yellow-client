package caseyellow.client.domain.analyze.model;

import caseyellow.client.common.Point;

public class DescriptionMatch {

    private boolean isMatchedDescription;
    private DescriptionLocation descriptionLocation;

    public DescriptionMatch() {
        this(true);
    }

    private DescriptionMatch(boolean found) {
        this(found, DescriptionLocation.defaultDescriptionLocation());
    }

    public DescriptionMatch(String description, Point center) {
        this(true, new DescriptionLocation(description, center));
    }

    public DescriptionMatch(boolean isMatchedDescription, DescriptionLocation descriptionLocation) {
        this.isMatchedDescription = isMatchedDescription;
        this.descriptionLocation = descriptionLocation;
    }

    public boolean foundMatchedDescription() {
        return isMatchedDescription;
    }

    public void setMatchedDescription(boolean matchedDescription) {
        isMatchedDescription = matchedDescription;
    }

    public DescriptionLocation getDescriptionLocation() {
        return descriptionLocation;
    }

    public void setDescriptionLocation(DescriptionLocation descriptionLocation) {
        this.descriptionLocation = descriptionLocation;
    }


    public static DescriptionMatch notFound() {
        return new DescriptionMatch(false);
    }
}
