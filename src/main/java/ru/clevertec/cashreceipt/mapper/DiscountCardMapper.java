package ru.clevertec.cashreceipt.mapper;

import ru.clevertec.cashreceipt.dto.DiscountCardDto;
import ru.clevertec.cashreceipt.model.DiscountCard;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface DiscountCardMapper {

    DiscountCardDto toDiscountCardDto(DiscountCard discountCard);

    DiscountCard fromDiscountCardDto(DiscountCardDto discountCardDto);

    List<DiscountCardDto> toDiscountCardDtoList(List<DiscountCard> discountCards);

}
