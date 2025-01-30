package com.backend.desafio.user;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "user_type")
public class UserType {
    @Id
    @SequenceGenerator(name = "user_type_sequence", sequenceName = "user_type_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_type_sequence")
    private Long id;
    private String type;

    public UserType() {}

    public UserType(Long id, String type) {
        this.id = id;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserType userType = (UserType) o;
        return Objects.equals(id, userType.id) && Objects.equals(type, userType.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type);
    }

    @Override
    public String toString() {
        return "UserType{" +
                "id=" + id +
                ", type='" + type + '\'' +
                '}';
    }
}
