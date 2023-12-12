package ru.moore.task_management_systems.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.MultiValueMap;
import ru.moore.task_management_systems.enums.TaskStatusEnum;
import ru.moore.task_management_systems.model.Task;

public class TaskSpecifications {

    public static Specification<Task> whereAuthor(String name) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("author").get("userName")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Task> whereExecutor(String name) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("executor").get("userName")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Task> whereStatus(String name) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("status")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Task> whereText(String name) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("text")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Task> build(MultiValueMap<String, String> params) {
        Specification<Task> spec = Specification.where(null);
        if (params.containsKey("author") && !params.getFirst("author").isBlank()) {
            for (int i = 0; i < params.get("author").size(); i++) {
                spec = spec.and(whereAuthor(params.get("author").get(i)));
            }
        }
        if (params.containsKey("executor") && !params.getFirst("executor").isBlank()) {
            for (int i = 0; i < params.get("executor").size(); i++) {
                spec = spec.and(whereExecutor(params.get("executor").get(i)));
            }
        }
        if (params.containsKey("status") && !params.getFirst("status").isBlank()) {
            for (int i = 0; i < params.get("status").size(); i++) {
                spec = spec.and(whereStatus(TaskStatusEnum.convertToDatabaseColumn(params.get("status").get(i))));
            }
        }
        if (params.containsKey("text") && !params.getFirst("text").isBlank()) {
            for (int i = 0; i < params.get("text").size(); i++) {
                spec = spec.and(whereText(params.get("text").get(i)));
            }
        }
        return spec;
    }

}
