package com.higor.poc1.api.core.validation;

import org.hibernate.validator.constraints.CompositionType;
import org.hibernate.validator.constraints.ConstraintComposition;

import javax.validation.Constraint;
import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ConstraintComposition(CompositionType.OR)
@Pattern(regexp = "\\d{4}-\\d{4}")
@Pattern(regexp = "\\d{5}-\\d{4}")
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { })
@ReportAsSingleViolation
public @interface PhoneNumber {

    @OverridesAttribute(constraint = Pattern.class, name = "message")
    String message() default "{Pattern.customerDTO.phoneNumber}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
