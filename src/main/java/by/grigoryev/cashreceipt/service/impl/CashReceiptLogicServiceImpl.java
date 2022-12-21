package by.grigoryev.cashreceipt.service.impl;

import by.grigoryev.cashreceipt.dto.DiscountCardDto;
import by.grigoryev.cashreceipt.dto.ProductDto;
import by.grigoryev.cashreceipt.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
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

        StringBuilder checkBuilder = cashReceiptInformationService.createCashReceiptHeader();
        StringBuilder promoDiscBuilder = new StringBuilder();

        totalSumWithDiscount = getTotalSumWithDiscount(productDtoList, totalSumWithDiscount,
                checkBuilder, promoDiscBuilder);

        checkBuilder.append(cashReceiptInformationService.createCashReceiptResults(totalSum,
                discountCardDto.getDiscountPercentage(), discount, promoDiscBuilder, totalSumWithDiscount));

        uploadFileService.uploadFile(checkBuilder.toString());

        log.info(checkBuilder.toString());
        return checkBuilder.toString();
    }

    protected BigDecimal getTotalSum(String idAndQuantity, List<ProductDto> productDtoList) {
        String[] splitSpace = idAndQuantity.split(" ");
        BigDecimal totalSum = new BigDecimal("0");

        for (String string : splitSpace) {
            String[] splitHyphen = string.split("-");
            String id = splitHyphen[0];
            String quantity = splitHyphen[1];

            ProductDto productDto = productService.update(Long.valueOf(id), Integer.valueOf(quantity));

            productDtoList.add(productDto);
            totalSum = totalSum.add(productDto.getTotal());
        }
        return totalSum;
    }

    protected BigDecimal getDiscount(BigDecimal totalSum, DiscountCardDto discountCardDto) {
        return totalSum.divide(BigDecimal.valueOf(100), 4, RoundingMode.UP)
                .multiply(discountCardDto.getDiscountPercentage());
    }

    protected BigDecimal getTotalSumWithDiscount(List<ProductDto> productDtoList,
                                                 BigDecimal totalSumWithDiscount,
                                                 StringBuilder checkBuilder,
                                                 StringBuilder promoDiscBuilder) {
        for (ProductDto productDto : productDtoList) {
            checkBuilder.append(cashReceiptInformationService.createCashReceiptBody(productDto));

            if (Boolean.TRUE.equals(productDto.getPromotion()) && productDto.getQuantity() > 5) {
                BigDecimal promotionDiscount = productDto.getTotal()
                        .divide(BigDecimal.valueOf(100), 4, RoundingMode.UP)
                        .multiply(BigDecimal.valueOf(10)).stripTrailingZeros();
                totalSumWithDiscount = totalSumWithDiscount.subtract(promotionDiscount);
                promoDiscBuilder
                        .append(cashReceiptInformationService
                                .createCashReceiptPromoDiscount(productDto.getName(), promotionDiscount));
            }

        }
        return totalSumWithDiscount;
    }

}
