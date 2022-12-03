package com.shchayuk.mvcProject2.servicies;

import com.shchayuk.mvcProject2.models.Book;
import com.shchayuk.mvcProject2.models.Person;
import com.shchayuk.mvcProject2.repositories.PersonRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PersonService {

    private final EntityManager entityManager;
    private final PersonRepository personRepository;

    @Autowired
    public PersonService(EntityManager entityManager, PersonRepository personRepository) {
        this.entityManager = entityManager;
        this.personRepository = personRepository;
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Person show(int id) {
        return personRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(Person person) {
        personRepository.save(person);
    }

    @Transactional
    public void update(Person updatedPerson, int id) {
        updatedPerson.setId(id);
        personRepository.save(updatedPerson);
    }

    @Transactional
    public void delete(int id) {
        personRepository.deleteById(id);
    }

//    public List<Book> showLentBooks(int id) {
//        Session session = entityManager.unwrap(Session.class);
//        return session.createQuery("select b from Book b where b.owner.id=:person_id").
//                setParameter("person_id", id).getResultList();
//    }

    public Optional<Person> showPersonByName(String name) {
        return Optional.ofNullable(personRepository.findByName(name));
    }

    public Person showOwner(int id) {
       return personRepository.findByBooksId(id);
    }
}
