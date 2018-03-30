# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/sah/Android/Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#dynamicalclass
-dontnote sun.misc.Unsafe
-dontnote com.google.appengine.api.**
-dontnote com.google.apphosting.api.**
-dontnote com.android.internal.R$id

#dynamicalclasscast
-keep class * implements name.leesah.nirvana.ui.medication.StrategyEditFragment
-dontnote org.joda.time.DateTimeZone

#dynamicalclassmember
-dontnote com.google.common.util.concurrent.**
-dontnote com.google.common.cache.**
-dontnote com.google.gson.internal.**
-keep class android.text.format.Time { int minute; }

#descriptorclass
-keep,includedescriptorclasses class name.leesah.nirvana.model.medication.Medication {
  Medication(int,java.lang.String,java.lang.String,name.leesah.nirvana.model.medication.DosageForm,name.leesah.nirvana.model.medication.reminding.RemindingStrategy,name.leesah.nirvana.model.medication.repeating.RepeatingStrategy,name.leesah.nirvana.model.medication.starting.StartingStrategy,name.leesah.nirvana.model.medication.stopping.StoppingStrategy,name.leesah.nirvana.model.medication.Medication$1);
}
-keep,includedescriptorclasses class name.leesah.nirvana.model.medication.reminding.RemindingStrategy {
  java.util.Set getRemindersThroughDay(name.leesah.nirvana.model.medication.Medication,org.joda.time.LocalDate);
}
-keep,includedescriptorclasses class name.leesah.nirvana.model.medication.repeating.RepeatingStrategy {
  boolean matches(name.leesah.nirvana.model.treatment.Treatment,name.leesah.nirvana.model.medication.starting.StartingStrategy,org.joda.time.LocalDate);
}
-keep,includedescriptorclasses class name.leesah.nirvana.model.medication.starting.StartingStrategy {
  boolean hasStarted(name.leesah.nirvana.model.treatment.Treatment,org.joda.time.LocalDate);
  org.joda.time.LocalDate getRealStartDate(name.leesah.nirvana.model.treatment.Treatment,org.joda.time.LocalDate);
}
-keep,includedescriptorclasses class name.leesah.nirvana.model.medication.stopping.StoppingStrategy {
  boolean hasStopped(name.leesah.nirvana.model.treatment.Treatment,org.joda.time.LocalDate);
}
-keep,includedescriptorclasses class name.leesah.nirvana.model.reminder.TimedDosage {
  TimedDosage(org.joda.time.LocalTime,int);
}
-keep,includedescriptorclasses class name.leesah.nirvana.model.treatment.Treatment {
  boolean contains(org.joda.time.LocalDate);
  org.joda.time.LocalDate getStartDateOf(org.joda.time.LocalDate);
}
-keep,includedescriptorclasses class name.leesah.nirvana.ui.widget.PeriodPicker {
  void setPeriod(org.joda.time.Period);
}

#duplicateclass
-dontnote android.net.http.**
-dontnote org.apache.http.**

#unknownclass
-dontnote com.android.internal.widget.PreferenceImageView

#unresolvedclass
-dontwarn sun.misc.Unsafe
-dontwarn java.lang.ClassValue
-dontwarn java.lang.invoke.MethodHandles**
-dontwarn java.nio.file.**
-dontwarn javax.annotation.**
-dontwarn com.google.errorprone.annotations.**
-dontwarn com.google.j2objc.annotations.**
-dontwarn org.joda.convert.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

