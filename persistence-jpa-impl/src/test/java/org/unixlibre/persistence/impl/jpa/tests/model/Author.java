package org.unixlibre.persistence.impl.jpa.tests.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by antoniovl on 13/05/17.
 */
@Entity
@Table(name = "authors")
public class Author implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "birth_date")
    private Date birthDate;
    @Column(name = "email")
    private String email;

    public Author() {
    }

    public Author(Long id) {
        this.id = id;
    }

    public Author(Long id, String name, Date birthDate, String email) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Author author = (Author) o;

        return id.equals(author.id);
    }

    @Override
    public int hashCode() {
        return (id == null) ? hashCode() : id.hashCode();
    }
}
