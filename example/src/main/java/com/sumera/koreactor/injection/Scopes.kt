package cz.muni.fi.pv256.movio2.uco_461464.injection

import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class PerActivity()

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class PerFragment()

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class PerChildFragment()
