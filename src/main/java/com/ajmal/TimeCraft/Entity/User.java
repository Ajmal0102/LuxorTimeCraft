package com.ajmal.TimeCraft.Entity;

    import io.micrometer.observation.ObservationFilter;
    import jakarta.persistence.*;
    import jakarta.validation.constraints.Email;
    import jakarta.validation.constraints.NotEmpty;
    import jakarta.validation.constraints.Pattern;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;

    import java.util.ArrayList;
    import java.util.List;

    @Entity
    @Getter
    @Setter
    @NoArgsConstructor
    @Table(name = "users")
    public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotEmpty
        @Column(nullable = false)
        private String firstName;

        private String lastName;

        @Pattern(regexp = "^[0-9]{10}$", message = "Invalid phone number format. Must be 10 digits.")
        private String phoneNumber;

        @Column(nullable = false, unique = true)
        @NotEmpty(message = "Email must not be empty")
        @Email(message = "{errors.invalid_email}")
        private String email;

        @NotEmpty
        private String password;

        @NotEmpty
        private String confirmPassword;

        private boolean isActive;

        private boolean isVerified;

        @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
        @JoinTable(
                name = "user_role",
                joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
                inverseJoinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")}
        )
        private List<Role> roles;

        @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
        private List<Address> addresses = new ArrayList<>();








        public User(User user) {
            this.id = user.getId();
            this.firstName = user.getFirstName();
            this.lastName = user.getLastName();
            this.phoneNumber = user.getPhoneNumber();
            this.email = user.getEmail();
            this.password = user.getPassword();
            this.confirmPassword = user.getConfirmPassword();
            this.roles = user.getRoles();
        }


    }
