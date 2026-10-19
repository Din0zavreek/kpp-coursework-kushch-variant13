package ua.edu.nupp.kpp.server.Service;

import ua.edu.nupp.kpp.server.Models.Request;
import java.util.List;

public class RiskCalculator {

    public static double calculate(double factor1, double factor2, double factor3) {
        if (factor1 < 0 || factor1 > 100 || factor2 < 0 || factor2 > 100 || factor3 < 0 || factor3 > 100) {
            throw new IllegalArgumentException("Invalid factor value");
        }
        return (factor1 + factor2 + factor3) / 3.0;
    }

    public double calculateOverallRisk(List<Request> requests) {
        if (requests == null || requests.isEmpty()) {
            return 0.0;
        }

        long unresolvedCount = requests.stream()
                .filter(r -> "НОВА".equalsIgnoreCase(r.getState()) || "В_РОБОТІ".equalsIgnoreCase(r.getState()))
                .count();

        return (double) unresolvedCount / requests.size() * 100.0;
    }
}