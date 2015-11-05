import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.ArrayList;

public class P02_DragonAccounting {
    static BigDecimal capital;
    static List<Long> employeesCount;
    static List<BigDecimal> employeesDSalary;

    public static void main(String[] args) {
        Locale.setDefault(Locale.ROOT);
        Scanner scan = new Scanner(System.in);
        capital = new BigDecimal(scan.nextLine());
        employeesCount = new ArrayList<>();
        employeesDSalary = new ArrayList<>();
        String line = scan.nextLine();
        int count = 0;
        while (!line.equals("END")) {
            BigDecimal expense = new BigDecimal("0");
            BigDecimal income = new BigDecimal("0");
            count++;
            String[] day = line.split("[;:]");
            hireEmployees(day[0], day[2]);
            checkForRaise(count);
            expense = expense.add(new BigDecimal(giveSalaries(count)));
            fireEmployees(day[1]);

            if (day.length > 4) {
                for (int i = 3; i < day.length; i += 2) {
                    switch (day[i]) {
                        case ("Previous years deficit"): expense = expense.add(new BigDecimal(day[i + 1])); break;
                        case ("Machines"): expense = expense.add(new BigDecimal(day[i + 1])); break;
                        case ("Product development"): income = income.add(new BigDecimal(day[i + 1])); break;
                        case ("Taxes"): expense = expense.add(new BigDecimal(day[i + 1])); break;
                        case ("Unconditional funding"): income = income.add(new BigDecimal(day[i + 1])); break;
                    }
                }
            }
            capital = capital.subtract(expense);
            capital = capital.add(income);
            if (capital.signum() < 0) {
                break;
            }
            line = scan.nextLine();
        }
        capital = capital.setScale(4, RoundingMode.DOWN);
        DecimalFormat df = new DecimalFormat("0.0000");
        if (capital.signum() < 0) {
            System.out.println("BANKRUPTCY: " + df.format(capital.abs()));
        } else {
            Long employees = 0L;
            for (int i = 0; i < employeesCount.size(); i++) {
                employees = employees + employeesCount.get(i);
            }
            System.out.println(employees + " " + df.format(capital));
        }
    }

    static void hireEmployees(String a, String b) {
        employeesCount.add(0, Long.parseLong(a));
        BigDecimal salary = new BigDecimal(b);
        BigDecimal daySalary = salary.divide(BigDecimal.valueOf(30), 9, RoundingMode.UP);
        employeesDSalary.add(0, daySalary.setScale(7, RoundingMode.DOWN));
    }

    static void checkForRaise(int c) {
        if (c >= 365) {
            if (employeesDSalary.size() >= 365) {
                for (int j = 365; j < employeesDSalary.size(); j++) {
                    if (j % 365 == 0) {
                        BigDecimal raisedSalary = employeesDSalary.get(j).multiply(BigDecimal.valueOf(1.006));
                        employeesDSalary.set(j, raisedSalary.setScale(7, RoundingMode.DOWN));
                    }
                }
            }
        }
    }

    static String giveSalaries(int c) {
        BigDecimal salaries = new BigDecimal("0");
        if (c % 30 == 0) {
            for (int j = 0; j < employeesCount.size(); j++) {
                if (j < 29) {
                    BigDecimal sal = (new BigDecimal(employeesCount.get(j)).multiply(employeesDSalary.get(j)));
                    salaries = salaries.add(sal.multiply(new BigDecimal(j + 1)));
                } else {
                    BigDecimal sal = (new BigDecimal(employeesCount.get(j)).multiply(employeesDSalary.get(j)));
                    salaries = salaries.add(sal.multiply(new BigDecimal("30")));
                }
            }
        }
        return salaries.toString();
    }

    static void fireEmployees(String a) {
        Long fired = Long.parseLong(a);
        while (fired > 0) {
            Long left = employeesCount.get(employeesCount.size() - 1);
            employeesCount.set((employeesCount.size() - 1),
                    (employeesCount.get(employeesCount.size() - 1) - fired));
            fired = fired - left;
            if ((employeesCount.get(employeesCount.size() - 1)) <= 0) {
                employeesCount.remove(employeesCount.size() - 1);
                employeesDSalary.remove(employeesCount.size() - 1);
            }
        }
    }
}
