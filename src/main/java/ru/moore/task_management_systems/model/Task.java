package ru.moore.task_management_systems.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;
import ru.moore.task_management_systems.enums.TaskPriorityEnum;
import ru.moore.task_management_systems.enums.TaskStatusEnum;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "TASK")
@Where(clause = "deleted = false")
public class Task extends BaseEntity {

    @Column(name = "status")
    private String status;

    public String getStatus() {
        return TaskStatusEnum.convertToEntityAttribute(this.status);
    }

    public void setStatus(String type) {
        this.status = TaskStatusEnum.convertToDatabaseColumn(type);
    }

    @Column(name = "priority")
    private String priority;

    public String getPriority() {
        return TaskPriorityEnum.convertToEntityAttribute(this.priority);
    }

    public void setPriority(String priority) {
        this.priority = TaskPriorityEnum.convertToDatabaseColumn(priority);
    }

    @Column(name = "header")
    private String header;

    @Column(name = "text")
    private String text;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    private Account author;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "executor_id")
    private Account executor;

}