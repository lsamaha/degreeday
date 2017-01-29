package meadowbrook.weather.model;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Assists with string dates, date objects, and iteration.
 */
public class DateRange implements Iterable<LocalDate> {

    private final LocalDate startDate;
    private final LocalDate endDate;

    public DateRange(LocalDate startDate, LocalDate endDate) {
        if (endDate.atStartOfDay(ZoneId.systemDefault()).toEpochSecond()
                < startDate.atStartOfDay(ZoneId.systemDefault()).toEpochSecond()) {
            throw new IllegalArgumentException("The startDate must be before the endDate.");
        }
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public Iterator<LocalDate> iterator() {
        return stream().iterator();
    }

    public Stream<LocalDate> stream() {
        return Stream.iterate(startDate, d -> d.plusDays(1))
                .limit(ChronoUnit.DAYS.between(startDate, endDate) + 1);
    }

    public List<LocalDate> toList() {
        return stream().collect(Collectors.toList());
    }
}
