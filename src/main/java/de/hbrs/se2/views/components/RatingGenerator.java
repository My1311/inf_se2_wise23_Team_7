package de.hbrs.se2.views.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingGenerator extends Div {

    private static final String RATING_STAR = "rating star";
    private static final String Height = "40px";
    private final Image[] empty_stars = new Image[5];
    private final Image[] full_stars = new Image[5];
    private final Image[] half_stars = new Image[5]; // for die Average in Company View

    private int currentRating; // 1-5

    public int getCurrentRatingIndex() {
        return currentRating;
    }

    public Component getRatingButton() {
        return this.createRatingButton();
    }

    public Component getRatingDisplay(double ratingPoint) {
        return this.createRatingDisplay(ratingPoint);
    }

    private Component createRatingDisplay(double rating) {
        HorizontalLayout layout = new HorizontalLayout();
        int fullStarCount = (int) rating; // Number of full stars
        boolean hasHalfStar = (rating - fullStarCount) >= 0.5; // Check if there should be a half star

        // Add full stars
        for (int i = 0; i < fullStarCount; i++) {
            full_stars[i] = new Image("images/star-full-icon.png", "full-star");
            full_stars[i].setHeight(Height);
            layout.add(full_stars[i]);
        }
        // Add half star if needed
        if (hasHalfStar) {
            half_stars[fullStarCount] = new Image("images/star-half-icon.png", "half-star");
            half_stars[fullStarCount].setHeight(Height);
            layout.add(half_stars[fullStarCount]);
            fullStarCount++; // Increase fullStarCount as we added a half star
        }
        // Add empty stars
        for (int k = fullStarCount; k < 5; k++) {
            empty_stars[k] = new Image("images/star-empty-icon.png", "empty-star");
            empty_stars[k].setHeight(Height);
            layout.add(empty_stars[k]);
        }
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        return layout;
    }


    private Component createRatingButton() {
        HorizontalLayout layout = new HorizontalLayout();
        for (int i = 0; i < full_stars.length; i++) {
            full_stars[i] = new Image("images/star-full-icon.png", "full-star");
            full_stars[i].setHeight(Height);
            final int point = i;
            full_stars[i].addClickListener(event -> {
                this.updateRatingStars(point);
            });
            layout.add(full_stars[i]);
        }
        for (int i = 0; i < empty_stars.length; i++) {
            empty_stars[i] = new Image("images/star-empty-icon.png", "empty-star");
            empty_stars[i].setHeight(Height);
            final int point = i;
            empty_stars[i].addClickListener(event -> {
                this.updateRatingStars(point);
            });
            layout.add(empty_stars[i]);
        }
        updateRatingStars(0);
        return layout;
    }

    private void updateRatingStars(int point) {
        this.currentRating = point;
        for (int i = 0; i < full_stars.length; i++) {
            if (i <= point) {
                full_stars[i].setVisible(true);
                this.empty_stars[i].setVisible(false);
            } else {
                full_stars[i].setVisible(false);
                this.empty_stars[i].setVisible(true);
            }
        }

    }


}
