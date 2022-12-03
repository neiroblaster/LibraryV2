package com.shchayuk.mvcProject2.controllers;

import com.shchayuk.mvcProject2.models.Book;
import com.shchayuk.mvcProject2.models.Person;
import com.shchayuk.mvcProject2.servicies.BookService;
import com.shchayuk.mvcProject2.servicies.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final PersonService personService;

    @Autowired
    public BookController(BookService bookService, PersonService personService) {
        this.bookService = bookService;
        this.personService = personService;
    }

    @GetMapping("/search")
    public String search(Model model, @ModelAttribute("pattern") String pattern){
        model.addAttribute("pattern", pattern);
        return "books/search";
    }

    @PostMapping("/search/result")
    public String searchResult(Model model,
                               @RequestParam(value = "pattern", required = false) String pattern,
                               RedirectAttributes redirectAttributes){
        model.addAttribute("books", bookService.findByNameIsStartingWith(pattern));
        redirectAttributes.addAttribute("pattern", pattern);

        return "books/search_result";
    }

    @GetMapping()
    public String index(Model model){
        model.addAttribute("books", bookService.findAll());
        return "books/index";
    }

    @GetMapping(params = {"page", "books_per_page"})
    public String index(Model model, @RequestParam(value = "page") int page,
                        @RequestParam(value = "books_per_page") int itemsPerPage) {

        model.addAttribute("books", bookService.findAll(page, itemsPerPage));
        return "books/index";
    }

    @GetMapping(params = {"sort_by_year"})
    public String index(Model model, @RequestParam(value = "sort_by_year") String year) {
        model.addAttribute("books", bookService.findAll(year));
        return "books/index";
    }

    @GetMapping(params = {"page", "books_per_page", "sort_by_year"})
    public String index(Model model, @RequestParam(value = "page") int page,
                        @RequestParam(value = "books_per_page") int itemsPerPage,
                        @RequestParam(value = "sort_by_year") String year) {

        model.addAttribute("books", bookService.findAll(page, itemsPerPage, year));
        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model,
                       @ModelAttribute("person") Person person) {
        model.addAttribute("book", bookService.show(id));
        model.addAttribute("people", personService.findAll());
        model.addAttribute("owner", personService.showOwner(id));
        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "/books/new";
    }

    @PostMapping()
    public String create(@Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/new";
        }

        bookService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", bookService.show(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/edit";
        }

        bookService.update(id, book);
        return "redirect:/books/" + id;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/lend")
    public String lendTheBook(@PathVariable("id") int id,
                              @ModelAttribute("person") Person person) {
        bookService.lendTheBook(id, person.getId());
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/release")
    public String releaseTheBook(@PathVariable("id") int id) {
        bookService.releaseTheBook(id);
        return "redirect:/books/" + id;
    }
}
