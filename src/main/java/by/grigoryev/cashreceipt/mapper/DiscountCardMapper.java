package by.grigoryev.cashreceipt.mapper;

import by.grigoryev.cashreceipt.dto.DiscountCardDto;
import by.grigoryev.cashreceipt.model.DiscountCard;
import org.mapstruct.Mapper;

@Mapper
public interface DiscountCardMapper {

    DiscountCardDto toDiscountCardDto(DiscountCard discountCard);

    DiscountCard fromDiscountCardDto(DiscountCardDto discountCardDto);

}
