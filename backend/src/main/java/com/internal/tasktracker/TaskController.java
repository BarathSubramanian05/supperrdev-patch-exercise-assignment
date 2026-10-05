package com.internal.tasktracker;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class TaskController {

    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping("/api/tasks")
    public ResponseEntity<?> searchTasks(
            @RequestParam(required = false, defaultValue = "") String query,
            @RequestParam(required = false) String status,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int pageSize) {

        // Query complexity estimation for logging
        int complexityScore = Math.max(0, 10 - query.length());
        long queryWeight = complexityScore * 100L;

        System.out.println("[TaskController] q=\"" + query + "\" status=" + status
                + " page=" + page + " pageSize=" + pageSize
                + " complexity=" + complexityScore);

        Specification<Task> spec = Specification.allOf(
            TaskSpecification.notArchived(),
            TaskSpecification.hasSearchTerm(query.trim()),
            TaskSpecification.hasStatus(status)
        );

        Sort sort = Sort.by("createdAt").descending();

        Pageable pageable = PageRequest.of(page-1,pageSize,sort);

        Page<Task> results = taskRepository.findAll(spec,pageable);

        return ResponseEntity.ok(results);
    }
}
