package com.ecommerce.micrommerce.web.exceptions;

import com.ecommerce.micrommerce.web.model.Product;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ProductValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Product.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Product product = (Product) target;
        if (product.getPrix() <= 0) {
            errors.rejectValue("prix", "prix.invalide", "Le produit ne peut pas être gratuit !");
        }
    }
}
