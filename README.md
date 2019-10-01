## Koreactor

Koreactor is an Android library for building reactive apps based on MVI architecture. It uses RxJava2 and is created specifically for Kotlin. Example of usage can be seen in the example directory.

Although it is not fully finished, it is ready to use:

Add it in your root build.gradle at the end of repositories:
```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
Add the dependency
```groovy
dependencies {
    implementation "com.github.SumeraMartin:koreactor:0.0.8"
}
```

### TODO:

- [ ] Add an example with complex use case
- [ ] Add better test coverage
- [ ] Add better documentation
- [ ] Refactor of naming conventions
- [ ] Write article about it
