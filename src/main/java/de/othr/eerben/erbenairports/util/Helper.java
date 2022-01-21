package de.othr.eerben.erbenairports.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Helper {

    public static List<Integer> pageNumbers(int currentPage, int totalPages) {
        List<Integer> pageNumbers;
        if (currentPage <= 3) {
            pageNumbers = IntStream.rangeClosed(1, 6)
                    .boxed()
                    .collect(Collectors.toList());
            pageNumbers.add(totalPages);
        } else {

            pageNumbers = IntStream.rangeClosed(1, 1)
                    .boxed()
                    .collect(Collectors.toList());
            if (currentPage >= totalPages - 2) {
                for (int i = totalPages - 6; i <= totalPages; i++) {
                    pageNumbers.add(i);
                }
            } else {
                for (int i = currentPage - 2; i <= currentPage + 3; i++) {
                    pageNumbers.add(i);
                }
                pageNumbers.add(totalPages);
            }
        }
        return pageNumbers;
    }
}
