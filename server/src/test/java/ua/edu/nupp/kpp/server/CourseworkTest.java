package ua.edu.nupp.kpp.server;

import org.junit.jupiter.api.Test;
import ua.edu.nupp.kpp.server.Models.RequestState;
import ua.edu.nupp.kpp.server.Models.Room;
import ua.edu.nupp.kpp.server.Service.RequestService;
import ua.edu.nupp.kpp.server.Service.RiskCalculator;

import static org.junit.jupiter.api.Assertions.*;

public class CourseworkTest {

    private final RequestService requestService = new RequestService();

    @Test
    void priorityIndexCalculationTest() {
        double result = RiskCalculator.calculate(70.0, 60.0, 50.0);
        assertEquals(61.5, result, 0.001);
    }

    @Test
    void riskCalculatorRejectsInvalidFactor() {
        assertThrows(IllegalArgumentException.class, () -> {
            RiskCalculator.calculate(120.0, 50.0, 50.0);
        });
    }

    @Test
    void newToAssignedTransitionAllowed() {
        assertTrue(requestService.isValidStateTransition(RequestState.НОВА, RequestState.ПРИЗНАЧЕНА));
    }

    @Test
    void assignedToInProgressTransitionAllowed() {
        assertTrue(requestService.isValidStateTransition(RequestState.ПРИЗНАЧЕНА, RequestState.В_РОБОТІ));
    }

    @Test
    void inProgressToCompletedTransitionAllowed() {
        assertTrue(requestService.isValidStateTransition(RequestState.В_РОБОТІ, RequestState.ВИКОНАНО));
    }

    @Test
    void newToCompletedTransitionRejected() {
        assertFalse(requestService.isValidStateTransition(RequestState.НОВА, RequestState.ВИКОНАНО));
    }

    @Test
    void completedStateIsFinal() {
        assertFalse(requestService.isValidStateTransition(RequestState.ВИКОНАНО, RequestState.В_РОБОТІ));
    }

    @Test
    void roomFloorValidationTest() {
        Room room = new Room();
        room.setRoomNumber("202");
        room.setCategory("Люкс");
        room.setFloor(-1);

        assertTrue(room.getFloor() < 0, "Поверх не може бути від'ємним");
    }
}