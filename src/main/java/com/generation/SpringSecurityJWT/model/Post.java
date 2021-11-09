package com.generation.SpringSecurityJWT.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;


import javax.persistence.*;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "text")
    private String content;
    //se indica el tipo de campo que quiero crear, eb varchar se peude alamancenar hasta
    //250 caravteres
    //con tipo text se pueden almacenar mas caracteres

    @ManyToOne
    //@JsonIgnore //no trae ni escribe este campo
    @JsonProperty(access = Access.WRITE_ONLY) //asi se guardara el id de quien hizo el post
    //acceder a la propiedad solo para escribirla
    private User user; //enlace de tablas, esta avisndo que es una llave foranea

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
