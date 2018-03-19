package caseyellow.client.domain.analyze.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DescriptionMatch {

    private boolean isMatchedDescription;
    private DescriptionLocation descriptionLocation;

    public boolean foundMatchedDescription() {
        return isMatchedDescription;
    }
}
