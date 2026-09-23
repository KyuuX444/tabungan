-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod

-keepclassmembers class * {
    @androidx.room.* *;
}

-keep class androidx.room.** { *; }
-dontwarn androidx.room.**

-keep class com.kyu.tabungan.data.entity.** { *; }
-keep class com.kyu.tabungan.data.model.** { *; }
-keep class com.kyu.tabungan.data.backup.** { *; }

-keepattributes *JavascriptInterface*
-keepclassmembers enum * { *; }

-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
-keep class com.google.gson.** { *; }
-dontwarn com.google.gson.**
