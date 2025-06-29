package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import ru.practicum.shareit.booking.dto.BookingCreateDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingCreateDtoTest {

    @Autowired
    private JacksonTester<BookingCreateDto> jsonJacksonTester;

    @Test
    void testSerialize() throws Exception {
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 12, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 2, 12, 0);
        BookingCreateDto dto = new BookingCreateDto(start, end, 1L);

        String expectedJson = """
                  {
                  "start": "2024-01-01T12:00:00",
                  "end": "2024-01-02T12:00:00",
                  "itemId": 1
                  }
                  """;

        assertThat(jsonJacksonTester.write(dto))
                .isEqualToJson(expectedJson);

        assertThat(jsonJacksonTester.parse(expectedJson))
                .isEqualTo(dto);
    }
}