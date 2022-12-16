package by.grigoryev.cashreceipt.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class CashReceiptExceptionHandler {

    private final IncorrectData data = new IncorrectData();

    @ExceptionHandler(NoSuchProductException.class)
    public ResponseEntity<IncorrectData> noSuchProductException(NoSuchProductException exception) {
        data.setInfo(exception.getMessage());
        log.error(data.getInfo());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(data);
    }

    @ExceptionHandler(NoSuchDiscountCardException.class)
    public ResponseEntity<IncorrectData> noSuchDiscountCardException(NoSuchDiscountCardException exception) {
        data.setInfo(exception.getMessage());
        log.error(data.getInfo());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(data);
    }

}
