package com.inflearn.rest.events;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@AllArgsConstructor
@Getter
public class EventResource extends RepresentationModel<EventResource> {
    @JsonUnwrapped
    private Event event;
}
