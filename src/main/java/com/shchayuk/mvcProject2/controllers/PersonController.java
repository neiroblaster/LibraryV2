package com.shchayuk.mvcProject2.controllers;

import com.shchayuk.mvcProject2.models.Book;
import com.shchayuk.mvcProject2.models.Person;
import com.shchayuk.mvcProject2.servicies.BookService;
import com.shchayuk.mvcProject2.servicies.PersonService;
//import com.shchayuk.mvcProject2.util.PersonValidator;
import com.shchayuk.mvcProject2.util.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/people")
public class PersonController {

    private final PersonValidator personValidator;
    private final PersonService personService;
    private final BookService bookService;

    @Autowired
    public PersonController(PersonService personService, PersonValidator personValidator, BookService bookService) {
        this.personService = personService;
        this.personValidator = personValidator;
        this.bookService = bookService;
    }

    @GetMapping()
    public String index(Model model){
        model.addAttribute("people", personService.findAll());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model,
                       @ModelAttribute("person") Person person){
        model.addAttribute("person", personService.show(id));
        model.addAttribute("books", bookService.findByOwnerId(id));
        //        model.addAttribute("books", personService.showLentBooks(id));

        return "people/show";
    }


    @GetMapping("/new")
    public String newPerson(@ModelAttribute ("person") Person person){
        return "people/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult){
        personValidator.validate(person, bindingResult);

        if(bindingResult.hasErrors()){
            return "people/new";
        }

        personService.save(person);
        return "redirect:/people";
    }


    @GetMapping("{id}/edit")
    public String edit(@PathVariable("id") int id, Model model){
        model.addAttribute("person", personService.show(id));
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult,
                         @PathVariable("id") int id){
        personValidator.validate(person, bindingResult);

        if(bindingResult.hasErrors()){
            return "people/edit";
        }

        personService.update(person, id);
        return "redirect:/people/" + id;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        personService.delete(id);
        return "redirect:/people";
    }


}
