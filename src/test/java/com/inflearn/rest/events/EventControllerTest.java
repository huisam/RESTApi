package com.inflearn.rest.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("정상적으로 이벤트를 생성할 수 있는 테스트")
    void createEvent() throws Exception {
        /* given */
        EventDto event = EventDto.builder()
                .name("Spring")
                .description("REST API")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 9, 4, 3, 20, 30))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 9, 19, 4, 22, 30))
                .beginEventDateTime(LocalDateTime.of(2018, 10, 4, 3, 20, 22))
                .endEventDateTime(LocalDateTime.of(2018, 10, 10, 4, 20, 22))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남")
                .build();

        /* then */
        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event))
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("basePrice").value(100));
    }


    @Test
    @DisplayName("입력값이 입력받을 수 없는 값을 사용할 수 있는 경우에 에러가 발생하는 테스트")
    void createEvent_BadRequest() throws Exception {
        /* given */
        Event event = Event.builder()
                .name("Spring")
                .description("REST API")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 9, 4, 3, 20, 30))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 9, 19, 4, 22, 30))
                .beginEventDateTime(LocalDateTime.of(2018, 10, 4, 3, 20, 22))
                .endEventDateTime(LocalDateTime.of(2018, 10, 10, 4, 20, 22))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남")
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        /* then */
        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event))
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("입력값이 비어있는 경우에 에러가 발생하는 테스트")
    void createEvent_BadRequest_Empty_Input() throws Exception {
        /* given */
        EventDto eventDto = EventDto.builder().build();
        /* then */
        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(eventDto))
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("입력값이 잘못된 경우에 에러가 발생하는 테스트")
    void createEvent_BadRequest_Wrong_Input() throws Exception {
        /* given */
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 9, 15, 3, 20, 30))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 9, 4, 4, 22, 30))
                .beginEventDateTime(LocalDateTime.of(2018, 10, 10, 3, 20, 22))
                .endEventDateTime(LocalDateTime.of(2018, 10, 4, 4, 20, 22))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남")
                .build();

        /* then */
        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(eventDto))
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").exists())
                .andExpect(jsonPath("$[0].objectName").exists())
                .andExpect(jsonPath("$[0].defaultMessage").exists())
                .andExpect(jsonPath("$[0].code").exists())
                .andExpect(jsonPath("$[0].rejectedValue").exists());
    }
}