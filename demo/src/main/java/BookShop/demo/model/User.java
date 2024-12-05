package BookShop.demo.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "MY_USERS")
public class User {

    // A noter que t'as eu un probleme avec le nom de column firstName et lastName
    // SQL est case insensitive, il ne fait pas de diff entre miniscule et majuscule
    // il change automatiquement la majiscule en _
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name ="user_id")
    private Integer id;
    @Column(name ="first_name")
    private String firstName;
    @Column(name ="last_name")
    private String lastName;
    private String email;
    private String password;
    @Lob
    private byte[] image; // maybe you will need to change it to Byte

    @OneToMany(mappedBy = "user")
    List<Stock> bookStocks = new ArrayList<>();

    @OneToOne(mappedBy = "purchaser",cascade = CascadeType.ALL)
    private Basket basket;

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    // Fetch EAGER so that the roles are fetched immediately when we are fetching the user,
    // knowing that a user will not have a lot of roles to the point where it would impact the
    // performance it is better to use it
    @JoinTable(name = "users_role",
    joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    )
    private List<Role> roles = new ArrayList<>();

    protected User(){}

    public User(String firstName, String lastName, String email, String password){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
