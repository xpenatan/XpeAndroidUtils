**Quick Setup:**

```Gradle
//######## Root build.gradle

repositories {

  ...
  
  maven { url "https://oss.sonatype.org/content/repositories/snapshots/" }
  maven { url "https://oss.sonatype.org/content/repositories/releases/" }
  ....
  
}


...

dependencies {
  implementation "com.github.xpenatan.XpeAndroidUtils:XpeRetrofitEx:1.0-SNAPSHOT"
}


```
