package com.inflearn.rest.events;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Getter
public class EventResource extends RepresentationModel<EventResource> {
    @JsonUnwrapped
    private Event event;

    public EventResource(Event event) {
        super(linkTo(EventController.class).slash(event.getId()).withSelfRel());
        this.event = event;
    }

    public EventResource(List<Link> initialLinks, Event event) {
        super(initialLinks);
        this.event = event;
    }
}
