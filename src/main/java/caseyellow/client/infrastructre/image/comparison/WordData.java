package caseyellow.client.infrastructre.image.comparison;


import caseyellow.client.common.resolution.Point;

import java.util.List;

public class WordData {

    private String locale;
    private String description;
    private BoundingPoly boundingPoly;

    public WordData() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BoundingPoly getBoundingPoly() {
        return boundingPoly;
    }

    public void setBoundingPoly(BoundingPoly boundingPoly) {
        this.boundingPoly = boundingPoly;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}
