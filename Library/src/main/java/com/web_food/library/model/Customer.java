package com.web_food.library.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;


@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customers", uniqueConstraints = @UniqueConstraint(columnNames = {"username","image","phone_number"}))
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;

    @Column(name = "phone_number")
    private String phoneNumber;
    private String address;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "city_id",referencedColumnName = "city_id")
    private City city;
    private String country;
    @Lob
    @Column(name = "image", columnDefinition = "MEDIUMBLOB")
    private String image;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private ShoppingCart cart;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Order> orders;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name = "customers_role", joinColumns = @JoinColumn(name = "customer_id",referencedColumnName = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id",referencedColumnName = "role_id"))
    private Collection<Role> roleCollection;

}
