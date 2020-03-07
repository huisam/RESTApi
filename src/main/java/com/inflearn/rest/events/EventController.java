package com.inflearn.rest.events;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.Errors;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

    private final EventRepository repository;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity postEvent(@RequestBody @Valid EventDto eventDto, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        final Event event = modelMapper.map(eventDto, Event.class);
        final Event newEvent = this.repository.save(event);
        URI createdURI = linkTo(EventController.class).slash(newEvent.getId()).toUri();
        return ResponseEntity.created(createdURI).body(newEvent);
    }
}
