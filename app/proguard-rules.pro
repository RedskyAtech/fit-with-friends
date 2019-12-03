-dontwarn okio.**
-keep class *{
  public private *;
}
-keepattributes *Annotation*
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.app.Application
-keep public class * extends android.app.Activity
-keep public class * extends android.app.PreferenceActivity
-keep public class * extends android.view.View
-keep public class * extends android.widget.BaseAdapter
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * implements android.view.View.OnTouchListener
-keep public class * implements android.view.View.OnClickListener
-keep public class * extends com.actionbarsherlock.app.SherlockActivity
-keep public class * extends com.actionbarsherlock.app.SherlockFragmentActivity
-keep public class * extends com.actionbarsherlock.app.SherlockMapActivity
-keep public class * extends com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay<OverlayItem>
-keep public class * extends com.actionbarsherlock.app.SherlockFragment
-keepclassmembers class * extends com.actionbarsherlock.ActionBarSherlock {
    <init>(android.app.Activity, int);
}
-keepclasseswithmembernames class * {
    native <methods>;
}
-assumenosideeffects class android.util.Log {
    public static *** v(...);
    public static *** i(...);
    public static *** d(...);
    public static *** w(...);
    public static *** e(...);
}
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

-dontwarn dagger.internal.codegen.**
-keepclassmembers,allowobfuscation class * {
    @javax.inject.* *;
    @dagger.* *;
    <init>();
}

-keep class dagger.* { *; }
-keep class javax.inject.* { *; }
-keep class * extends dagger.internal.Binding
-keep class * extends dagger.internal.ModuleAdapter
-keep class * extends dagger.internal.StaticInjection

#
# Proguard config for a Google Maps Android API sample project.
#
# This file only contains the proguard options required by the Google Maps
# Android API v2. You should use these settings in addition to those provided by the
# Android SDK (<sdk>/tools/proguard/proguard-android-optimize.txt).
#
# For more details on the use of proguard in Android, please read:
# http://proguard.sourceforge.net/manual/examples.html#androidapplication
-optimizations !code/simplification/variable
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Fragment
-keep public class * extends android.support.v4.app.Fragment
# The Maps Android API uses custom parcelables.
# Use this rule (which is slightly broader than the standard recommended one)
# to avoid obfuscating them.
-keepclassmembers class * implements android.os.Parcelable {
    static *** CREATOR;
}
# The Maps Android API uses serialization.
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose
-dump class_files.txt
-printseeds seeds.txt
-printusage unused.txt
-printmapping mapping.txt
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keep class com.google.android.gms.common.GooglePlayServicesUtil {*;}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient {*;}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info {*;}

-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**
-keep class com.google.ads.** {*;}

-keepattributes Signature

-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }
-keep enum android.support.v7.** { *; }

-keep class android.support.v4.** { *; }
-keep interface android.support.v4.** { *; }
-keep enum android.support.v4.** { *; }

-keep class com.google.** { *; }
-keep interface com.google.** { *; }
-keep enum com.google.** { *; }