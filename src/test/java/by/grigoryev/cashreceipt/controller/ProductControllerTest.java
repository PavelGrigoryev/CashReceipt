package by.grigoryev.cashreceipt.controller;

import by.grigoryev.cashreceipt.model.Product;
import by.grigoryev.cashreceipt.service.ProductService;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    public static final long ID = 1L;
    public static final int QUANTITY = 3;
    public static final String NAME = "Самовар золотой";
    public static final BigDecimal PRICE = BigDecimal.valueOf(256.24);
    public static final boolean PROMOTION = true;
    public static final String JSON_CONTENT = """
            {
                "id": %s,
                "quantity": %s,
                "name": "%s",
                "price": %s,
                "total": %s,
                "promotion": %s
              }
            """.formatted(ID, QUANTITY, NAME, PRICE, PRICE.multiply(BigDecimal.valueOf(QUANTITY)), PROMOTION);

    @Autowired
    MockMvc mockMvc;
    @MockBean
    private ProductService productService;

    @Test
    @DisplayName("testing findAll endpoint with empty list of elements")
    void findAllWithEmptyList() throws Exception {
        doReturn(new ArrayList<>()).when(productService).findAll();

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DisplayName("testing findAll endpoint with filled list of elements")
    void findAllWithFilledValues() throws Exception {
        doReturn(List.of(getMockedProduct())).when(productService).findAll();

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().json("[" + JSON_CONTENT + "]"));
    }

    @Test
    @DisplayName("testing findById endpoint with exist element")
    void findByIdWithExistElement() throws Exception {
        doReturn(getMockedProduct()).when(productService)
                .findById(ID);

        mockMvc.perform(get("/products/" + ID))
                .andExpect(status().isOk())
                .andExpect(content().json(JSON_CONTENT));
    }

    @Test
    @DisplayName("testing save endpoint")
    void save() throws Exception {
        doReturn(getMockedProduct()).when(productService)
                .save(any(Product.class));

        mockMvc.perform(post("/products")
                        .content(JSON_CONTENT)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(JSON_CONTENT));
    }

    @Test
    @DisplayName("testing update endpoint")
    void update() throws Exception {
        doReturn(getMockedProduct()).when(productService)
                .update(anyLong(), anyInt());

        mockMvc.perform(put("/products/?id=" + ID + "&quantity=" + QUANTITY)
                        .content(JSON_CONTENT)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(JSON_CONTENT));
    }

    @Test
    @DisplayName("testing save endpoint with bad request")
    void saveWithBadRequest() throws Exception {
        doReturn(getMockedProduct()).when(productService)
                .save(any(Product.class));

        mockMvc.perform(post("/products")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private Product getMockedProduct() {
        return Product.builder()
                .id(ID)
                .quantity(QUANTITY)
                .name(NAME)
                .price(PRICE)
                .total(PRICE.multiply(BigDecimal.valueOf(QUANTITY)))
                .promotion(PROMOTION)
                .build();
    }

}