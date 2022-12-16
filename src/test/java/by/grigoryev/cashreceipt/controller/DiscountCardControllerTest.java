package by.grigoryev.cashreceipt.controller;

import by.grigoryev.cashreceipt.model.DiscountCard;
import by.grigoryev.cashreceipt.service.DiscountCardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DiscountCardController.class)
class DiscountCardControllerTest {

    public static final long ID = 1L;
    public static final String DISCOUNT_CARD_NUMBER = "1234";
    public static final BigDecimal DISCOUNT_PERCENTAGE = BigDecimal.valueOf(3);
    public static final String JSON_CONTENT = """
            {
                "id": %s,
                "discountCardNumber": "%s",
                "discountPercentage": %s
              }
            """.formatted(ID, DISCOUNT_CARD_NUMBER, DISCOUNT_PERCENTAGE);

    @Autowired
    MockMvc mockMvc;
    @MockBean
    private DiscountCardService discountCardService;

    @Test
    @DisplayName("testing findAll endpoint with empty list of elements")
    void findAllWithEmptyList() throws Exception {
        doReturn(new ArrayList<>()).when(discountCardService).findAll();

        mockMvc.perform(get("/discountCards"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DisplayName("testing findAll endpoint with filled list of elements")
    void findAllWithFilledValues() throws Exception {
        doReturn(List.of(getMockedDiscountCard())).when(discountCardService).findAll();

        mockMvc.perform(get("/discountCards"))
                .andExpect(status().isOk())
                .andExpect(content().json("[" + JSON_CONTENT + "]"));
    }

    @Test
    @DisplayName("testing findById endpoint with exist element")
    void findByIdWithExistElement() throws Exception {
        doReturn(getMockedDiscountCard()).when(discountCardService)
                .findById(ID);

        mockMvc.perform(get("/discountCards/" + ID))
                .andExpect(status().isOk())
                .andExpect(content().json(JSON_CONTENT));
    }

    @Test
    @DisplayName("testing save endpoint")
    void save() throws Exception {
        doReturn(getMockedDiscountCard()).when(discountCardService)
                .save(any(DiscountCard.class));

        mockMvc.perform(post("/discountCards")
                        .content(JSON_CONTENT)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(JSON_CONTENT));
    }

    @Test
    @DisplayName("testing save endpoint with bad request")
    void saveWithBadRequest() throws Exception {
        doReturn(getMockedDiscountCard()).when(discountCardService)
                .save(any(DiscountCard.class));

        mockMvc.perform(post("/discountCards")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private DiscountCard getMockedDiscountCard() {
        return DiscountCard.builder()
                .id(ID)
                .discountCardNumber(DISCOUNT_CARD_NUMBER)
                .discountPercentage(DISCOUNT_PERCENTAGE)
                .build();
    }

}