package com.inflearn.rest.events;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EventTest {
    @Test
    void builder() {
        Event event = Event.builder()
                .name("Inflearn Title")
                .description("REST API")
                .build();
        assertThat(event).isNotNull();
    }

    @Test
    void javaBean() {
        // given
        Event event = new Event();
        String name = "Inflearn";
        String description = "REST";
        // when
        event.setName(name);
        event.setDescription(description);
        // then
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);
    }
}