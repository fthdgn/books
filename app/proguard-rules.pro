#BottomNavigationViewEx
-keep public class android.support.design.widget.BottomNavigationView { *; }
-keep public class android.support.design.internal.BottomNavigationMenuView { *; }
-keep public class android.support.design.internal.BottomNavigationPresenter { *; }
-keep public class android.support.design.internal.BottomNavigationItemView { *; }

#Android O Release bug, try to delete after stable releases
-dontwarn android.content.**
-keep class android.content.**

#Retrofit
-dontwarn okio.**
-dontwarn javax.annotation.**