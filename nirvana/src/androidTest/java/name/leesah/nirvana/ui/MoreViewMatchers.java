package name.leesah.nirvana.ui;

import android.support.annotation.NonNull;
import android.view.View;

import org.hamcrest.Matcher;

import name.leesah.nirvana.R;

import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by sah on 2017-04-30.
 */

public class MoreViewMatchers {
    @NonNull
    public static Matcher<View> switchWidgetBesidesTitle(int prefTitleResId) {
        return allOf(
                withId(android.R.id.switch_widget),
                withParent(allOf(
                        withId(android.R.id.widget_frame),
                        withParent(withChild(allOf(
                                withId(R.id.text_frame),
                                withChild(allOf(
                                        withId(android.R.id.title),
                                        withText(prefTitleResId))))))))
        );
    }
}
