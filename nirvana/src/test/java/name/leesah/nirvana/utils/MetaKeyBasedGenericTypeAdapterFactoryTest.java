package name.leesah.nirvana.utils;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import org.joda.time.LocalTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Type;

import name.leesah.nirvana.BuildConfig;
import name.leesah.nirvana.model.medication.reminding.CertainHours;
import name.leesah.nirvana.model.medication.reminding.EveryNHours;
import name.leesah.nirvana.model.medication.reminding.RemindingStrategy;
import name.leesah.nirvana.model.medication.repeating.WithInterval;
import name.leesah.nirvana.model.medication.repeating.RepeatingStrategy;
import name.leesah.nirvana.model.medication.starting.Immediately;
import name.leesah.nirvana.model.medication.starting.StartingStrategy;
import name.leesah.nirvana.model.medication.stopping.Never;
import name.leesah.nirvana.model.medication.stopping.StoppingStrategy;
import name.leesah.nirvana.model.reminder.TimedDosage;
import name.leesah.nirvana.model.treatment.recurring.NTimes;
import name.leesah.nirvana.model.treatment.recurring.RecurringStrategy;

import static java.util.Collections.singletonList;
import static name.leesah.nirvana.utils.AdaptedGsonFactory.getGson;
import static name.leesah.nirvana.utils.MetaKeyBasedGenericTypeAdapterFactory.CLASS_META_KEY;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.equalTo;
import static org.joda.time.LocalTime.now;
import static org.junit.Assert.assertThat;

/**
 * Created by sah on 2016-12-05.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = {24, 25})
public class MetaKeyBasedGenericTypeAdapterFactoryTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void recurringStrategy() throws Exception {
        JsonElement json = getGson().toJsonTree(
                new NTimes(6),
                RecurringStrategy.class);

        assertThat(
                json.getAsJsonObject().get(CLASS_META_KEY).getAsString(),
                equalTo(NTimes.class.getCanonicalName()));

        assertThat(
                getGson().fromJson(json, RecurringStrategy.class),
                is(instanceOf(NTimes.class)));
    }

    @Test
    public void remindingStrategy() throws Exception {
        JsonElement json = getGson().toJsonTree(
                new EveryNHours(new TimedDosage(new LocalTime(0).withHourOfDay(6), 2), 8),
                RemindingStrategy.class);

        assertThat(
                json.getAsJsonObject().get(CLASS_META_KEY).getAsString(),
                equalTo(EveryNHours.class.getCanonicalName()));

        assertThat(
                getGson().fromJson(json, RemindingStrategy.class),
                is(instanceOf(EveryNHours.class)));
    }

    @Test
    public void repeatingStrategy() throws Exception {
        JsonElement json = getGson().toJsonTree(
                new WithInterval(7),
                RepeatingStrategy.class);

        assertThat(
                json.getAsJsonObject().get(CLASS_META_KEY).getAsString(),
                equalTo(WithInterval.class.getCanonicalName()));

        assertThat(
                getGson().fromJson(json, RepeatingStrategy.class),
                is(instanceOf(WithInterval.class)));
    }

    @Test
    public void startingStrategy() throws Exception {
        JsonElement json = getGson().toJsonTree(
                new Immediately(),
                StartingStrategy.class);

        assertThat(
                json.getAsJsonObject().get(CLASS_META_KEY).getAsString(),
                equalTo(Immediately.class.getCanonicalName()));

        assertThat(
                getGson().fromJson(json, StartingStrategy.class),
                is(instanceOf(Immediately.class)));
    }

    @Test
    public void stoppingStrategy() throws Exception {
        JsonElement json = getGson().toJsonTree(
                new Never(),
                StartingStrategy.class);

        assertThat(
                json.getAsJsonObject().get(CLASS_META_KEY).getAsString(),
                equalTo(Never.class.getCanonicalName()));

        assertThat(
                getGson().fromJson(json, StoppingStrategy.class),
                is(instanceOf(Never.class)));
    }

    @Test
    public void explicitlyCertainHours() throws Exception {
        JsonElement json = getGson().toJsonTree(
                new CertainHours(singletonList(new TimedDosage(now(), 2))),
                CertainHours.class);

        assertThat(
                json.getAsJsonObject().has(CLASS_META_KEY),
                is(true));

        assertThat(
                getGson().fromJson(json, CertainHours.class),
                is(instanceOf(CertainHours.class)));
    }

    @Test
    public void withGenericType() throws Exception {
        JsonElement json = new GenericClass<RemindingStrategy>()
                .toJsonTreeWithGenericType(
                        new CertainHours(singletonList(new TimedDosage(now(), 2))));

        assertThat(
                json.getAsJsonObject().has(CLASS_META_KEY),
                is(true));

        assertThat(
                new GenericClass<RemindingStrategy>()
                        .fromJsonWithGenericType(json),
                not(instanceOf(CertainHours.class)));
    }

    @Test
    public void withGenericTypeExplicitlyCertainHours() throws Exception {
        JsonElement json = new GenericClass<CertainHours>()
                .toJsonTreeWithGenericType(
                        new CertainHours(singletonList(new TimedDosage(now(), 2))));

        assertThat(
                json.getAsJsonObject().has(CLASS_META_KEY),
                is(true));

        assertThat(
                new GenericClass<CertainHours>()
                        .fromJsonWithGenericType(json),
                not(instanceOf(CertainHours.class)));
    }

    private static class GenericClass<T> {
        public JsonElement toJsonTreeWithGenericType(T strategy) throws Exception {
            final Type type = new TypeToken<T>() {
            }.getType();
            return getGson().toJsonTree(strategy, type);
        }

        public T fromJsonWithGenericType(JsonElement json) throws Exception {
            final Type type = new TypeToken<T>() {
            }.getType();
            return getGson().fromJson(json, type);
        }
    }

    @Test
    public void withGenericTypeProvidingToken() throws Exception {
        TypeToken<RemindingStrategy> type = new TypeToken<RemindingStrategy>() {
        };

        JsonElement json = new GenericClassHoldingTypeToken<>(type)
                .toJsonTreeWithGenericType(
                        new CertainHours(singletonList(new TimedDosage(now(), 2))));

        assertThat(
                json.getAsJsonObject().has(CLASS_META_KEY),
                is(true));

        assertThat(
                new GenericClassHoldingTypeToken<>(type)
                        .fromJsonWithGenericType(json),
                is(instanceOf(CertainHours.class)));
    }

    @Test
    public void withGenericTypeExplicitlyCertainHoursProvidingToken() throws Exception {
        TypeToken<CertainHours> type = new TypeToken<CertainHours>() {
        };

        JsonElement json = new GenericClassHoldingTypeToken<>(type)
                .toJsonTreeWithGenericType(
                        new CertainHours(singletonList(new TimedDosage(now(), 2))));

        assertThat(
                json.getAsJsonObject().has(CLASS_META_KEY),
                is(true));

        assertThat(
                new GenericClassHoldingTypeToken<>(type)
                        .fromJsonWithGenericType(json),
                is(instanceOf(CertainHours.class)));
    }

    private static class GenericClassHoldingTypeToken<T> {
        private final TypeToken<T> type;

        private GenericClassHoldingTypeToken(TypeToken<T> type) {
            this.type = type;
        }

        public JsonElement toJsonTreeWithGenericType(T strategy) throws Exception {
            return getGson().toJsonTree(strategy, type.getType());
        }

        public T fromJsonWithGenericType(JsonElement json) throws Exception {
            return getGson().fromJson(json, type.getType());
        }
    }

}