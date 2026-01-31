package com.healthcare.patient.constant;

public final class PatientConstants {

    // Prevent instantiation
    private PatientConstants() {}

    // --- Regex Patterns ---
    public static final String PHONE_REGEX = "^[+]?[0-9]{10,15}$";
    public static final String ZIP_CODE_REGEX = "^[0-9]{5,10}$";

    // --- Validation Messages ---
    public static final String MSG_FIRST_NAME_REQUIRED = "First name is required";
    public static final String MSG_LAST_NAME_REQUIRED = "Last name is required";
    public static final String MSG_EMAIL_REQUIRED = "Email is required";
    public static final String MSG_EMAIL_INVALID = "Invalid email format";
    public static final String MSG_PHONE_REQUIRED = "Phone number is required";
    public static final String MSG_PHONE_INVALID = "Invalid phone number format (10-15 digits)";
    public static final String MSG_DOB_REQUIRED = "Date of birth is required";
    public static final String MSG_DOB_PAST = "Date of birth must be in the past";
    public static final String MSG_GENDER_REQUIRED = "Gender is required";
    public static final String MSG_ZIP_INVALID = "Invalid zip code format";
    public static final String VAL_METRIC_TYPE_REQUIRED = "Metric type is required (e.g., WEIGHT, BP)";
    public static final String VAL_METRIC_VALUE_REQUIRED = "Metric value is required";
    public static final String VAL_METRIC_UNIT_REQUIRED = "Metric unit is required";

    // --- Emergency Contact Messages ---
    public static final String MSG_CONTACT_NAME_REQUIRED = "Contact name is required";
    public static final String MSG_RELATION_REQUIRED = "Relationship is required";
    public static final String MSG_CONTACT_PHONE_REQUIRED = "Contact phone is required";

    // --- Success Messages ---
    public static final String SUCCESS_REGISTER = "Patient registered successfully";
    public static final String SUCCESS_RETRIEVE = "Patient profile retrieved successfully";

    public static final String MSG_METRIC_ADDED = "Health metric recorded successfully";
    public static final String MSG_METRICS_RETRIEVED = "Health metrics retrieved successfully";


}
