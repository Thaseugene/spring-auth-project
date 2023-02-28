package com.spring.auth.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class User implements Serializable{

        private static final long serialVersionUID = 1L;
        private int id;
        private String name;
        private String surname;
        private String email;
        private String login;
        private String password;
        private Date registerDate;
        private Role role;
        private boolean isActive;

        public User(int id, String name, String surname, String email, String login, String password, Role role, Date registerDate, boolean isActive) {
            this.id = id;
            this.name = name;
            this.surname = surname;
            this.email = email;
            this.login = login;
            this.password = password;
            this.role = role;
            this.registerDate = registerDate;
            this.isActive = isActive;
        }



        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }


        public Role getRole() {
            return role;
        }


        public void setRole(Role role) {
            this.role = role;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Date getRegisterDate() {
            return registerDate;
        }

        public void setRegisterDate(Date registerDate) {
            this.registerDate = registerDate;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean isActive() {
            return isActive;
        }

        public void setActive(boolean active) {
            isActive = active;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            User user = (User) o;
            return id == user.id && isActive == user.isActive && Objects.equals(name, user.name) &&
                    Objects.equals(surname, user.surname) && Objects.equals(email, user.email) &&
                    Objects.equals(login, user.login) && Objects.equals(password, user.password) &&
                    Objects.equals(registerDate, user.registerDate) && role == user.role;
        }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, email, login, password, registerDate, role, isActive);
    }
}
