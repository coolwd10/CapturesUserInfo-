# CapturesUserInfo-

Application support English/Hindi language to capture user’s personal data and creates a beautiful shareable resume.

1.Added java doc Scope 2.Added class UML diagram(Requirement install plugin in SimpleUML in adroid studio.) 3. Project include below dependency:


 def daggerVersion = '2.16'
    implementation "com.google.dagger:dagger:${daggerVersion}"
    implementation "com.google.dagger:dagger-android:${daggerVersion}"
    implementation "com.google.dagger:dagger-android-support:${daggerVersion}"
    annotationProcessor "com.google.dagger:dagger-compiler:${daggerVersion}"

    // Room
    implementation "androidx.room:room-runtime:${archVersion}"
    annotationProcessor "androidx.room:room-compiler:${archVersion}"

    // Picasso
    implementation('com.squareup.picasso:picasso:2.71828') {
        exclude group: 'com.android.support', module: 'support-annotations'
        exclude group: 'com.android.support', module: 'exifinterface'
    }

    implementation 'com.google.firebase:firebase-auth:16.1.0'
