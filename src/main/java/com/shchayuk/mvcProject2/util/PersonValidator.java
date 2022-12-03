package com.shchayuk.mvcProject2.util;

import com.shchayuk.mvcProject2.dao.PersonDAO;
import com.shchayuk.mvcProject2.models.Person;
import com.shchayuk.mvcProject2.servicies.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {

    private final PersonService personService;

    @Autowired
    public PersonValidator(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person) o;

//        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "year", "", "No");

        if (personService.showPersonByName(person.getName()).isPresent()){
            errors.rejectValue("name", "","This name already exists");
        }
        if (String.valueOf(person.getYear()).isEmpty()){
            errors.rejectValue("year", "org.springframework.web.util.NestedServletException","This field shouldn't be empty");
        }

    }
}
