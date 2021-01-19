package com.punici.gulimall.common.valid;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

public class ListValueConstraintValidator implements ConstraintValidator<ListValue, Integer>
{
    private Set<Integer> set = new HashSet<>();
    
    @Override
    public void initialize(ListValue constraintAnnotation)
    {
        int[] vales = constraintAnnotation.vales();
        for(int val : vales)
        {
            if(StringUtils.isNotBlank(val + ""))
            {
                set.add(val);
            }
        }
    }
    
    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext)
    {
        return set.contains(integer);
    }
}
