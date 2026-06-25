package com.payment_service.factory;

import com.payment_service.exception.InvalidPaymentTypeException;
import com.payment_service.strategy.CardPaymentStrategy;
import com.payment_service.strategy.PaymentStrategy;
import com.payment_service.strategy.UpiPaymentStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PaymentFactoryTest {

    @Mock
    private UpiPaymentStrategy upiPaymentStrategy;

    @Mock
    private CardPaymentStrategy cardPaymentStrategy;

    @InjectMocks
    private PaymentFactory paymentFactory;

    @Test
    void getPaymentStrategy_UPI_ReturnsUpiStrategy() {
        PaymentStrategy result = paymentFactory.getPaymentStrategy("UPI");

        assertNotNull(result);
        assertEquals(upiPaymentStrategy, result);
    }

    @Test
    void getPaymentStrategy_upi_lowercase_ReturnsUpiStrategy() {
        PaymentStrategy result = paymentFactory.getPaymentStrategy("upi");

        assertNotNull(result);
        assertEquals(upiPaymentStrategy, result);
    }

    @Test
    void getPaymentStrategy_CARD_ReturnsCardStrategy() {
        PaymentStrategy result = paymentFactory.getPaymentStrategy("CARD");

        assertNotNull(result);
        assertEquals(cardPaymentStrategy, result);
    }

    @Test
    void getPaymentStrategy_card_lowercase_ReturnsCardStrategy() {
        PaymentStrategy result = paymentFactory.getPaymentStrategy("card");

        assertNotNull(result);
        assertEquals(cardPaymentStrategy, result);
    }

    @Test
    void getPaymentStrategy_InvalidType_ThrowsException() {
        InvalidPaymentTypeException exception = assertThrows(
                InvalidPaymentTypeException.class,
                () -> paymentFactory.getPaymentStrategy("INVALID")
        );

        assertEquals("Invalid payment type. Use UPI or CARD", exception.getMessage());
    }

    @Test
    void getPaymentStrategy_Null_ThrowsException() {
        assertThrows(
                NullPointerException.class,
                () -> paymentFactory.getPaymentStrategy(null)
        );
    }

    @Test
    void getPaymentStrategy_EmptyString_ThrowsException() {
        InvalidPaymentTypeException exception = assertThrows(
                InvalidPaymentTypeException.class,
                () -> paymentFactory.getPaymentStrategy("")
        );

        assertEquals("Invalid payment type. Use UPI or CARD", exception.getMessage());
    }
}
