package com.core.models.searchModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserRating {

@SerializedName("aggregate_rating")
@Expose
private Float aggregateRating;
@SerializedName("rating_text")
@Expose
private String ratingText;
@SerializedName("rating_color")
@Expose
private String ratingColor;
@SerializedName("votes")
@Expose
private Integer votes;
@SerializedName("custom_rating_text")
@Expose
private String customRatingText;
@SerializedName("custom_rating_text_background")
@Expose
private String customRatingTextBackground;
@SerializedName("rating_tool_tip")
@Expose
private String ratingToolTip;

public Float getAggregateRating() {
return aggregateRating;
}

public void setAggregateRating(Float aggregateRating) {
this.aggregateRating = aggregateRating;
}

public String getRatingText() {
return ratingText;
}

public void setRatingText(String ratingText) {
this.ratingText = ratingText;
}

public String getRatingColor() {
return ratingColor;
}

public void setRatingColor(String ratingColor) {
this.ratingColor = ratingColor;
}

public Integer getVotes() {
return votes;
}

public void setVotes(Integer votes) {
this.votes = votes;
}

public String getCustomRatingText() {
return customRatingText;
}

public void setCustomRatingText(String customRatingText) {
this.customRatingText = customRatingText;
}

public String getCustomRatingTextBackground() {
return customRatingTextBackground;
}

public void setCustomRatingTextBackground(String customRatingTextBackground) {
this.customRatingTextBackground = customRatingTextBackground;
}

public String getRatingToolTip() {
return ratingToolTip;
}

public void setRatingToolTip(String ratingToolTip) {
this.ratingToolTip = ratingToolTip;
}

}
