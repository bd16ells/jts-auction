package com.example.auctionapp.domain.auction.bid.validation.constraint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BidIncrementConstraint.class)
public @interface BidIncrement {

    String message() default "{BidIncrement.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    double value() default 0.25D;

}