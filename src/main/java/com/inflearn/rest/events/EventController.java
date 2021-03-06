package com.inflearn.rest.events;

import com.inflearn.rest.commons.ErrorsResource;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

    private final EventRepository repository;
    private final ModelMapper modelMapper;
    private final EventValidator eventValidator;

    @PostMapping
    public ResponseEntity postEvent(@RequestBody @Valid EventDto eventDto, Errors errors) {
        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        eventValidator.validate(eventDto, errors);
        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        final Event event = modelMapper.map(eventDto, Event.class);
        final Event newEvent = this.repository.save(event);

        WebMvcLinkBuilder linkBuilder = linkTo(EventController.class).slash(newEvent.getId());
        URI createdURI = linkBuilder.toUri();

        List<Link> links = List.of(linkTo(EventController.class).withRel("query-events"),
                linkBuilder.withSelfRel(), linkBuilder.withRel("update-event")
        );
        EventResource eventResource = new EventResource(links, newEvent);
        return ResponseEntity.created(createdURI).body(eventResource);
    }

    private ResponseEntity<ErrorsResource> badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorsResource(errors));
    }


    @GetMapping
    public ResponseEntity queryEvents(Pageable pageable, PagedResourcesAssembler<Event> assembler) {
        Page<Event> pages = repository.findAll(pageable);
        var pagedModel = assembler.toModel(pages, EventResource::new);
        pagedModel.add(new Link("/docs/index.html#resources-events-list").withRel("profile"));
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity getEvent(@PathVariable Long id) {
        final Optional<Event> optionalEvent = this.repository.findById(id);

        if (optionalEvent.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            EventResource eventResource = new EventResource(optionalEvent.get());
            eventResource.add(new Link("/docs/index.html#resources-events-get").withRel("profile"));
            return ResponseEntity.ok(eventResource);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity putEvent(@PathVariable Long id,
                                   @RequestBody @Valid EventDto eventDto,
                                   Errors errors) {
        final Optional<Event> optionalEvent = this.repository.findById(id);

        if (optionalEvent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        this.eventValidator.validate(eventDto, errors);
        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        final Event existingEvent = optionalEvent.get();
        this.modelMapper.map(eventDto, existingEvent);
        final Event savedEvent = repository.save(existingEvent);

        EventResource eventResource = new EventResource(savedEvent);
        eventResource.add(new Link("/docs/index.html#resources-events-put").withRel("profile"));
        return ResponseEntity.ok(eventResource);
    }
}
