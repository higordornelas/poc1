package com.higor.poc1.api.core.validation;

import javax.validation.Constraint;
import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.groups.Default;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { })
@Pattern(regexp = "\\d{5}-\\d{3}")
public @interface ZipCode {

    @OverridesAttribute(constraint = Pattern.class, name = "message")
    String message() default "Zip code is not valid. Please insert a zipCode in the format XXXXX-XXX.";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
