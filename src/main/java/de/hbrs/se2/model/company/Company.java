package de.hbrs.se2.model.company;

import de.hbrs.se2.model.common.BaseEntity;
import de.hbrs.se2.model.jobadvertisement.Advertisement;
import de.hbrs.se2.model.user.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(schema = "public")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Company extends BaseEntity {

    @Builder.Default
    @Column(nullable = false)
    private String name = ""; // die dürfen dür die db nicht leer sein
    @Builder.Default
    @Basic(fetch = FetchType.EAGER) //will immediately be loaded
    @Column(name = "logo", nullable = true)
    private byte[] logo = null;
    @Builder.Default
    @Column(name = "industry", nullable = false)
    private String industry = "";
    @Builder.Default()
    @Column(nullable = false)
    private String description = "";
    @Builder.Default()
    @Column(name = "phoneNumber", nullable = true)
    private String phoneNumber = "";
    //@PrimaryKeyJoinColumn(name = "user")
    //so that Hibernate knows it is a single value that will be recieved. otherwise there would be an error for the attrinute
    @Builder.Default
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, optional = false)
// if Company or User gets deleted, that company too and it is not nullable
    @JoinColumn(
            name = "fk_user", //creates the foreign key in Company with "user_id" as name. it is not bound to the actul name of the pimary key of User
            //referencedColumnName = "id", // the name of the referenced column for User in the DB
            foreignKey = @ForeignKey(name = "fk_from_company_to_user")// sets the name of the foreign key constraint in the db
    )
    private User user = null;
    @OneToMany(mappedBy = "company", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Advertisement> advertisements = new ArrayList<Advertisement>();
    @Builder.Default()
    @Column(name = "rating_punkt", nullable = false)
    private double rating_punkt = 0.0;

    public Company(String companyName, String industry, String description, User user) {
        this.name = companyName;
        this.industry = industry;
        this.description = description;
        this.user = user;

    }

    public Company(String name, byte[] logo, String industry, String description, User user) {
        this.name = name;
        this.logo = logo;
        this.industry = industry;
        this.description = description;
        this.user = user;
    }


}
