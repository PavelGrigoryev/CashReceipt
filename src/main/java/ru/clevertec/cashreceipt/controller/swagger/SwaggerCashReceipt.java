package ru.clevertec.cashreceipt.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "CashReceipt", description = "The CashReceipt API")
public interface SwaggerCashReceipt {

    @Operation(
            summary = "Retrieve your cash receipt", tags = "CashReceipt",
            description = """
                    Retrieve a Cash Receipt, where idAndQuantity=3-6 2-6 1-7 discountCardNumber=9875 should form
                    and output a check containing the name of the product with id=3 and quantity of 6 items,
                    the same with id=2 and quantity of 6 items, id=1 and quantity of 7 items, etc.
                    discountCardNumber=9875 means that a discount card with the number 9875 was presented.
                    """,
            parameters = {
                    @Parameter(name = "idAndQuantity", description = "Enter id and quantity here",
                            example = "3-6 2-6 1-7"),
                    @Parameter(name = "discountCardNumber", description = "Enter discount card number here",
                            example = "9875")
            }
    )
    ResponseEntity<String> createCashReceipt(String idAndQuantity, String discountCardNumber);

}
