package ru.clevertec.cashreceipt.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.clevertec.cashreceipt.dto.ProductDto;
import ru.clevertec.cashreceipt.service.CashReceiptInformationService;
import ru.clevertec.cashreceipt.service.CashReceiptLogicService;
import ru.clevertec.cashreceipt.service.DiscountCardService;
import ru.clevertec.cashreceipt.service.ProductService;
import ru.clevertec.cashreceipt.service.factory.UploadFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class CashReceiptLogicServiceImpl implements CashReceiptLogicService {

    private final ProductService productService;

    private final DiscountCardService discountCardService;

    private final CashReceiptInformationService cashReceiptInformationService;

    private final UploadFactory uploadFactory;

    @Value("${upload.file}")
    private String fileType;

    @Override
    public String createCashReceipt(String idAndQuantity, String discountCardNumber) {
        StringBuilder cashReceiptHeader = cashReceiptInformationService
                .createCashReceiptHeader(LocalDate.now(), LocalTime.now());
        StringBuilder promoDiscountBuilder = new StringBuilder();
        final BigDecimal[] promoDiscount = {new BigDecimal("0")};

        String cashReceipt = getProducts(idAndQuantity)
                .stream()
                .peek(productDto -> cashReceiptHeader
                        .append(cashReceiptInformationService.createCashReceiptBody(productDto)))
                .peek(productDto -> promotionFilter(promoDiscountBuilder, promoDiscount, productDto))
                .map(ProductDto::total)
                .reduce(BigDecimal::add)
                .map(totalSum -> getCashReceiptResults(
                        discountCardNumber,
                        cashReceiptHeader,
                        promoDiscountBuilder,
                        promoDiscount,
                        totalSum
                ))
                .map(StringBuilder::toString)
                .stream()
                .peek(s -> uploadFactory.create(fileType).uploadFile(s))
                .findFirst()
                .orElse("");

        log.info(cashReceipt);
        return cashReceipt;
    }

    protected List<ProductDto> getProducts(String idAndQuantity) {
        return idAndQuantity.lines()
                .map(s -> s.split(" "))
                .flatMap(Arrays::stream)
                .map(s -> s.split("-"))
                .map(strings -> productService.update(Long.valueOf(strings[0]), Integer.valueOf(strings[1])))
                .toList();
    }

    protected void promotionFilter(StringBuilder promoDiscountBuilder, BigDecimal[] promoDiscount, ProductDto productDto) {
        if (Boolean.TRUE.equals(productDto.promotion()) && productDto.quantity() > 5) {
            BigDecimal promo = getPromotionDiscount(productDto);
            promoDiscountBuilder.append(cashReceiptInformationService
                    .createCashReceiptPromoDiscount(productDto.name(), promo));
            promoDiscount[0] = promoDiscount[0].add(promo);
        }
    }

    protected StringBuilder getCashReceiptResults(String discountCardNumber,
                                                  StringBuilder cashReceiptHeader,
                                                  StringBuilder promoDiscountBuilder,
                                                  BigDecimal[] promoDiscount,
                                                  BigDecimal totalSum) {
        return Stream.of(discountCardService.findByDiscountCardNumber(discountCardNumber))
                .map(discountCardDto -> {
                    BigDecimal discount = getDiscount(totalSum, discountCardDto.discountPercentage());
                    return cashReceiptHeader.append(
                            cashReceiptInformationService.createCashReceiptResults(
                                    totalSum,
                                    discountCardDto.discountPercentage(),
                                    discount,
                                    promoDiscountBuilder,
                                    totalSum.subtract(discount).subtract(promoDiscount[0]))
                    );
                })
                .findFirst()
                .orElse(new StringBuilder());
    }

    protected BigDecimal getPromotionDiscount(ProductDto productDto) {
        return productDto.total()
                .multiply(BigDecimal.valueOf(0.1))
                .stripTrailingZeros();
    }

    protected BigDecimal getDiscount(BigDecimal totalSum, BigDecimal percentage) {
        return totalSum.multiply(BigDecimal.valueOf(0.01)
                .multiply(percentage));
    }

}
