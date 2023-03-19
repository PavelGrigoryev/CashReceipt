package ru.clevertec.cashreceipt.service.impl;

import ru.clevertec.cashreceipt.dto.DiscountCardDto;
import ru.clevertec.cashreceipt.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.clevertec.cashreceipt.service.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CashReceiptLogicServiceImpl implements CashReceiptLogicService {

    private final ProductService productService;

    private final DiscountCardService discountCardService;

    private final CashReceiptInformationService cashReceiptInformationService;

    private final UploadFileService uploadFileService;

    @Override
    public String createCashReceipt(String idAndQuantity, String discountCardNumber) {
        List<ProductDto> productDtoList = new ArrayList<>();
        BigDecimal totalSum = getTotalSum(idAndQuantity, productDtoList);

        DiscountCardDto discountCardDto = discountCardService.findByDiscountCardNumber(discountCardNumber);
        BigDecimal discount = getDiscount(totalSum, discountCardDto);
        BigDecimal totalSumWithDiscount = totalSum.subtract(discount);

        StringBuilder checkBuilder = cashReceiptInformationService
                .createCashReceiptHeader(LocalDate.now(), LocalTime.now());
        StringBuilder promoDiscBuilder = new StringBuilder();

        totalSumWithDiscount = getTotalSumWithDiscount(productDtoList, totalSumWithDiscount,
                checkBuilder, promoDiscBuilder);

        checkBuilder.append(cashReceiptInformationService.createCashReceiptResults(totalSum,
                discountCardDto.discountPercentage(), discount, promoDiscBuilder, totalSumWithDiscount));

        uploadFileService.uploadFileTxt(checkBuilder.toString());
        uploadFileService.uploadFilePdf(checkBuilder.toString());

        log.info(checkBuilder.toString());
        return checkBuilder.toString();
    }

    protected BigDecimal getTotalSum(String idAndQuantity, List<ProductDto> productDtoList) {
        List<ProductDto> products = idAndQuantity.lines()
                .map(s -> s.split(" "))
                .flatMap(Arrays::stream)
                .map(s -> s.split("-"))
                .map(strings -> productService.update(Long.valueOf(strings[0]), Integer.valueOf(strings[1])))
                .toList();

        productDtoList.addAll(products);

        return products.stream()
                .map(ProductDto::total)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    protected BigDecimal getDiscount(BigDecimal totalSum, DiscountCardDto discountCardDto) {
        return totalSum.divide(BigDecimal.valueOf(100), 4, RoundingMode.UP)
                .multiply(discountCardDto.discountPercentage());
    }

    protected BigDecimal getTotalSumWithDiscount(List<ProductDto> productDtoList,
                                                 BigDecimal totalSumWithDiscount,
                                                 StringBuilder checkBuilder,
                                                 StringBuilder promoDiscBuilder) {
        productDtoList.forEach(productDto -> checkBuilder
                .append(cashReceiptInformationService.createCashReceiptBody(productDto)));

        BigDecimal promoDiscountSum = productDtoList.stream()
                .filter(productDto -> productDto.promotion() && productDto.quantity() > 5)
                .map(productDto -> {
                    BigDecimal promotionDiscount = getPromotionDiscount(productDto);
                    promoDiscBuilder.append(cashReceiptInformationService
                            .createCashReceiptPromoDiscount(productDto.name(), promotionDiscount));
                    return promotionDiscount;
                })
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        return totalSumWithDiscount
                .subtract(promoDiscountSum);
    }

    protected BigDecimal getPromotionDiscount(ProductDto productDto) {
        return productDto.total()
                .divide(BigDecimal.valueOf(10), 4, RoundingMode.UP)
                .stripTrailingZeros();
    }

}
