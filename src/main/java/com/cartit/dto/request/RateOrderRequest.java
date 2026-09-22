package com.cartit.dto.request;

public class RateOrderRequest {

    private Integer rating;
    private String reviewComment;

    public RateOrderRequest() {
    }

    public RateOrderRequest(Integer rating, String reviewComment) {
        this.rating = rating;
        this.reviewComment = reviewComment;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getReviewComment() {
        return reviewComment;
    }

    public void setReviewComment(String reviewComment) {
        this.reviewComment = reviewComment;
    }
}
