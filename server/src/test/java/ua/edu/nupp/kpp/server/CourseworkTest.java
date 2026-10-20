package ua.edu.nupp.kpp.server;

import ua.edu.nupp.kpp.server.Models.Request;
import ua.edu.nupp.kpp.server.Models.RequestState;
import ua.edu.nupp.kpp.server.Service.RequestService;
import ua.edu.nupp.kpp.server.Service.RiskCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CourseworkTest {

    private RiskCalculator riskCalculator;
    private RequestService requestService;

    @BeforeEach
    void setUp() {
        riskCalculator = new RiskCalculator();
        requestService = new RequestService();
    }

    @Test
    void testRiskCalculationStandardValues() {
        double result = riskCalculator.calculate(80.0, 60.0, 100.0);
        assertEquals(78.0, result, 0.001);
    }

    @Test
    void testRiskCalculationMinimumValues() {
        double result = riskCalculator.calculate(0.0, 0.0, 0.0);
        assertEquals(0.0, result, 0.001);
    }

    @Test
    void testRiskCalculationMaximumValues() {
        double result = riskCalculator.calculate(100.0, 100.0, 100.0);
        assertEquals(100.0, result, 0.001);
    }

    @Test
    void testRiskCalculationWeightsSum() {
        double result = riskCalculator.calculate(50.0, 50.0, 50.0);
        assertEquals(50.0, result, 0.001);
    }

    @Test
    void testRiskCalculationInvalidFactorThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            riskCalculator.calculate(150.0, 50.0, 50.0);
        });
    }

    @Test
    void testInitialStateDefault() {
        Request request = new Request();
        request.setState("НОВА");
        assertEquals("НОВА", request.getState());
    }

    @Test
    void testValidTransitionNewToAssigned() {
        boolean valid = requestService.isValidStateTransition(RequestState.НОВА, RequestState.ПРИЗНАЧЕНА);
        assertTrue(valid);
    }

    @Test
    void testValidTransitionAssignedToInProgress() {
        boolean valid = requestService.isValidStateTransition(RequestState.ПРИЗНАЧЕНА, RequestState.В_РОБОТІ);
        assertTrue(valid);
    }

    @Test
    void testValidTransitionInProgressToCompleted() {
        boolean valid = requestService.isValidStateTransition(RequestState.В_РОБОТІ, RequestState.ВИКОНАНО);
        assertTrue(valid);
    }

    @Test
    void testInvalidBackwardTransition() {
        boolean valid = requestService.isValidStateTransition(RequestState.В_РОБОТІ, RequestState.НОВА);
        assertFalse(valid);
    }

    @Test
    void testInvalidJumpTransition() {
        boolean valid = requestService.isValidStateTransition(RequestState.НОВА, RequestState.ВИКОНАНО);
        assertFalse(valid);
    }
}