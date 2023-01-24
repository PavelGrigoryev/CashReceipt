package by.grigoryev.cashreceipt.mapper;

import by.grigoryev.cashreceipt.dto.DiscountCardDto;
import by.grigoryev.cashreceipt.model.DiscountCard;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface DiscountCardMapper {

    DiscountCardDto toDiscountCardDto(DiscountCard discountCard);

    DiscountCard fromDiscountCardDto(DiscountCardDto discountCardDto);

    List<DiscountCardDto> toDiscountCardDtoList(List<DiscountCard> discountCards);

}
