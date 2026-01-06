package com.healthcare.patient.constant;

public final class FeedbackConstants {

    private FeedbackConstants() {}


    public static final String MSG_RATING_REQUIRED = "Rating is required";
    public static final String MSG_RATING_MIN = "Rating must be at least 1";
    public static final String MSG_RATING_MAX = "Rating must be at most 5";
    public static final String MSG_COMMENTS_SIZE = "Comments cannot exceed 1000 characters";

    public static final String SUCCESS_SUBMIT = "Feedback submitted successfully";
    public static final String SUCCESS_HISTORY = "Patient feedback history retrieved successfully";

    public static final String ERR_PATIENT_NOT_FOUND = "Cannot submit feedback. Patient not found with ID: ";
}
