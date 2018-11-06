package com.get.model;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
public class Project implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty
    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private Long projectCode;

    @OneToOne
    @JoinColumn(name = "pm_user_id")
    private User projectManager;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private List<Task> tasks;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Project)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        Project project = (Project) o;

        if (!id.equals(project.id)) {
            return false;
        }
        return name.equals(project.name);
    }

    @Override
    public int hashCode() {
        if (id == null) {
            id = 0L;
        }
        int result = super.hashCode();
        result = 31 * result + id.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
