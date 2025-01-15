package BookShop.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "role_id")
    private int id;

    @Column(name = "role_name")
    private String roleName;

    // JsonIgnor is important here because when we want to serialize it, the serializer look for the user and after that
    // the user's role, when looking into a role, he finds users and it does never end ( infinite loop )
    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    List<User> users = new ArrayList<>();

    public Role (int roleId){
        this.id = roleId;
        if(roleId == 2){
            this.roleName = "SELLER";
        }else if(roleId == 3){
            this.roleName = "USER";
        }
    }

    public Role (int roleId, String roleName){
        this.id = roleId;
        if(roleId == 2){
            this.roleName = "SELLER";
        }else if(roleId == 3){
            this.roleName = "USER";
        }
    }

    public Role(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRolename() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", roleName='" + roleName + '\'' +
                '}';
    }
}
