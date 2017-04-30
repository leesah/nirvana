package name.leesah.nirvana.ui;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Switch;

import org.hamcrest.Matcher;

import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by sah on 2017-04-06.
 */

public class MoreViewActions {

    public static ViewAction setNumber(int n) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return allOf(isAssignableFrom(NumberPicker.class), isDisplayed());
            }

            @Override
            public String getDescription() {
                return "set number in a NumberPicker";
            }

            @Override
            public void perform(UiController uiController, View view) {
                final NumberPicker numberPicker = (NumberPicker) view;
                numberPicker.setValue(n);
            }
        };
    }

    public static ViewAction setChecked(boolean checked) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return allOf(isAssignableFrom(Switch.class), isDisplayed());
            }

            @Override
            public String getDescription() {
                return "set checked state in a Switch";
            }

            @Override
            public void perform(UiController uiController, View view) {
                ((Switch) view).setChecked(checked);
            }
        };
    }

}
