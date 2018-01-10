package com.TobyMellor.TrainRouteFinder.validation;

import java.util.List;

public interface Validator<T> {
    boolean validate(T e);

    List<String> getValidationMessages();
}
