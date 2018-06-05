package uk.sponte.automation.seleniumpom.testobjects.ba;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import uk.sponte.automation.seleniumpom.PageFactory;
import uk.sponte.automation.seleniumpom.annotations.Section;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * Selects the date from the date picker of mobile page
 * Created by n448846 on 22/07/2015.
 */
public class MobileDateSelector {

    @Inject
    private WebDriver driver;

    @Inject
    private Provider<PageFactory> pageFactory;

    static Map<String, Integer> MONTH_NAMES = Calendar.getInstance()
            .getDisplayNames(Calendar.MONTH, Calendar.LONG, Locale.UK);

    private static final int DEFAULT_PAGE_LOAD_TIMEOUT = 70000;

    @Section
    @FindBy(css = ".formSection.journeyDate")
    public List<MobileCalenderBox> calender;

    /**
     * Sets the departure date
     *
     * @param MonthNumber
     * @param Date
     * @throws TimeoutException
     */
    public void setDeparture(Date date) throws TimeoutException {
        setDate(calender.get(0), date);
    }

    public void setReturn(Date date) throws TimeoutException {
        setDate(calender.get(1), date);
    }

    /**
     * Selects the date in the calender box
     *
     * @param calenderNextElement
     * @param confirmButton
     * @param monthName
     * @param calenderBox
     * @param monthNumber
     * @param date
     */
    private void setDate(MobileCalenderBox calendar, Date date) {
        calendar.calendarNext.waitUntilStopsMoving();
        while (!MONTH_NAMES.get(calendar.monthName.getText())
                .equals(date.getMonth())) {
            calendar.calendarNext.waitUntilVisible();
            calendar.calendarNext.click();
            pageFactory.get().invalidate(calendar);
        }

        calendar.dateCell(date.getDate()).waitUntilStopsMoving().click();
        calendar.confirmDateButton.click();
        calendar.confirmDateButton.waitUntilHidden();
    }
}
