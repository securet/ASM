package com.securet.ssm.spring;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;



public class DateEditorRegistrar implements PropertyEditorRegistrar {

    @Autowired
    private SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy");

    private boolean allowEmpty = false;

    @Override
    public void registerCustomEditors(PropertyEditorRegistry registry) {
        registry.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat,allowEmpty));
    }
}


