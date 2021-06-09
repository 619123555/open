package com.open.common.utils.validation;

/** @author admin */
public class ValidationItem {
  private String value;
  private boolean required;
  private ValidationRule rule;
  private String fieldName;
  private String max;
  private String min;

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public boolean isRequired() {
    return required;
  }

  public void setRequired(boolean required) {
    this.required = required;
  }

  public ValidationRule getRule() {
    return rule;
  }

  public void setRule(ValidationRule rule) {
    this.rule = rule;
  }

  public String getFieldName() {
    return fieldName;
  }

  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }

  public String getMax() {
    return max;
  }

  public void setMax(String max) {
    this.max = max;
  }

  public String getMin() {
    return min;
  }

  public void setMin(String min) {
    this.min = min;
  }

  public ValidationItem(
      String fieldName,
      String value,
      boolean required,
      String min,
      String max,
      ValidationRule rule) {
    this.min = min;
    this.fieldName = fieldName;
    this.value = value;
    this.required = required;
    this.rule = rule;
    this.max = max;
  }

  public ValidationItem(
      String fieldName, String value, boolean required, String max, ValidationRule rule) {
    this.fieldName = fieldName;
    this.value = value;
    this.required = required;
    this.rule = rule;
    this.min = null;
    this.max = max;
  }

  public ValidationItem(String fieldName, String value, boolean required, ValidationRule rule) {
    this.fieldName = fieldName;
    this.value = value;
    this.required = required;
    this.rule = rule;
    this.min = null;
    this.max = null;
  }

  public ValidationItem(String fieldName, String value, boolean required) {
    this.fieldName = fieldName;
    this.value = value;
    this.required = required;
    this.rule = ValidationRule.STRING;
    this.min = null;
    this.max = null;
  }

  public ValidationItem(String fieldName, String value, boolean required, String max) {
    this.fieldName = fieldName;
    this.value = value;
    this.required = required;
    this.rule = ValidationRule.STRING;
    this.min = null;
    this.max = max;
  }
}
