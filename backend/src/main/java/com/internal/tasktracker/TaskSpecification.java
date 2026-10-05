package com.internal.tasktracker;

import org.springframework.data.jpa.domain.Specification;

public class TaskSpecification {
    public static Specification<Task> notArchived()
    {
        return ((root, query, criteriaBuilder) ->
            criteriaBuilder.isFalse(root.get("archived"))
            );
    }

    public static Specification<Task> hasSearchTerm(String term)
    {

        return (root, query, criteriaBuilder) ->
        {

            if(term == null || term.isBlank() ) return null;

            String searchTerm = "%"+term.toLowerCase()+"%";

            return criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), searchTerm),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), searchTerm)
            );
        };
    }

    public static Specification<Task> hasStatus(String status)
    {
        return (root, query, criteriaBuilder) -> {
            if(status == null || status.isBlank() ) return null;

            return criteriaBuilder.equal(root.get("status"),TaskStatus.valueOf(status.toUpperCase()).name());
        };
    }
}
