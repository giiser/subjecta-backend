package ee.subjecta.subjecta_backend.controller;

import ee.subjecta.subjecta_backend.dto.SubjectDto;
import ee.subjecta.subjecta_backend.service.SubjectService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subjects")
public class SubjectController {

    private final SubjectService service;

    public SubjectController(SubjectService service) {
        this.service = service;
    }

    @GetMapping
    public List<SubjectDto> subjects() {
        return service.getSubjects();
    }
}
