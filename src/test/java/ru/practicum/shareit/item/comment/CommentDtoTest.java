package ru.practicum.shareit.item.comment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class CommentDtoTest {

    @Autowired
    private JacksonTester<CommentDto> jsonJacksonTester;

    @Test
    void testSerializeDeserialize() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        CommentDto dto = new CommentDto(1L, "Text", "Author", now);

        String expectedJson = String.format("""
            {
              "id": 1,
              "text": "Text",
              "authorName": "Author",
              "created": "%s"
            }
        """, now.toString());

        assertThat(jsonJacksonTester.write(dto))
                .isEqualToJson(expectedJson);

        assertThat(jsonJacksonTester.parse(expectedJson))
                .isEqualTo(dto);
    }
}
