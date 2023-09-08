package ru.clevertec.cashreceipt.validation;

import java.util.List;

public record ValidationErrorResponse(List<Violation> violations) {
}
