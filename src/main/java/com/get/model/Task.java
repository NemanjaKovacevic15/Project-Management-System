package com.get.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
public class Task implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty
    @Column(nullable = false)
    private String status = StatusType.NEW.getStatusType();

    @NotEmpty
    @Column(nullable = false)
    private String title;

    @Size(min = 20, max = 200, message = "please describe the task")
    private String description;

    @NotNull
    @Column(nullable = false)
    private Double progressPercentage;

    @NotNull
    @Column(nullable = false)
    @DateTimeFormat(pattern = "MM-dd-yyyy")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deadLine;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "dev_user_id")
    private User developer;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "project_id")
    private Project project;
}
