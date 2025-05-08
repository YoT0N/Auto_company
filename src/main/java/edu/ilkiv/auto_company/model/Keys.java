//package edu.ilkiv.auto_company.model;
//
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Table(name = "keys")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class Keys {
//
//    @Id
//    @Column(name = "id_keys")
//    private Long idKeys;  // ідентифікатор таблиці ключів
//
//    @Column(name = "login" , unique = true, length = 50)
//    private String login;  // логін користувача
//
//    @Column(name = "password", length = 255)
//    private String password;  // пароль користувача
//
//    @Column(name = "role", length = 50)
//    private String role;  // роль у системі
//
//    @Column(name = "tablename", length = 50)
//    private String tablename;  // назва таблиці для редагування
//
//    @Column(name = "fields", length = 255)
//    private String fields;  // доступні поля для редагування
//}
