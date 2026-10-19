package ua.edu.nupp.kpp.server.Service;

public class RiskCalculator {

    public static double calculate(double f1, double f2, double f3) {

        if (f1 < 0 || f1 > 100 || f2 < 0 || f2 > 100 || f3 < 0 || f3 > 100) {
            throw new IllegalArgumentException("Фактори F1, F2 та F3 мають бути в діапазоні від 0 до 100");
        }

        return 0.40 * f1 + 0.35 * f2 + 0.25 * f3;
    }
}