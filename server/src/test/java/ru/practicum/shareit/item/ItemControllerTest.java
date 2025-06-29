package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.service.ItemService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.List;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    void createItem() throws Exception {
        ItemDto dto = new ItemDto(null, "Name", "Desc", true, null);
        ItemDto response = new ItemDto(1L, "Name", "Desc", true, null);

        when(itemService.createItem(anyLong(), any())).thenReturn(response);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(itemService, times(1)).createItem(1L, dto);
    }

    @Test
    void updateItem() throws Exception {
        ItemDto dto = new ItemDto(null, "Updated", "New Desc", false, null);
        ItemDto response = new ItemDto(1L, "Updated", "New Desc", false, null);

        when(itemService.updateItem(anyLong(), anyLong(), any())).thenReturn(response);

        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("New Desc"));

        verify(itemService, times(1)).updateItem(1L, 1L, dto);
    }

    @Test
    void getItemById() throws Exception {
        ItemWithBookingDto response = new ItemWithBookingDto(
                1L, "Name", "Desc", true, null, null, List.of()
        );

        when(itemService.getItemById(anyLong())).thenReturn(response);

        mockMvc.perform(get("/items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Name"));
    }

    @Test
    void getItemsByOwner() throws Exception {
        ItemWithBookingDto response = new ItemWithBookingDto(
                1L, "Name", "Desc", true, null, null, List.of()
        );

        when(itemService.getItemsByOwner(anyLong())).thenReturn(List.of(response));

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Name"));
    }

    @Test
    void searchItems() throws Exception {
        ItemDto response = new ItemDto(1L, "Name", "Desc", true, null);

        when(itemService.searchItems(anyString())).thenReturn(List.of(response));

        mockMvc.perform(get("/items/search?text=name"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Name"));
    }
}
