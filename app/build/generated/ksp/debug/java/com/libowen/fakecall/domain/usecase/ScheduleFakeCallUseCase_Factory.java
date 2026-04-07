package com.libowen.fakecall.domain.usecase;

import com.libowen.fakecall.system.AlarmHelper;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class ScheduleFakeCallUseCase_Factory implements Factory<ScheduleFakeCallUseCase> {
  private final Provider<AlarmHelper> alarmHelperProvider;

  private ScheduleFakeCallUseCase_Factory(Provider<AlarmHelper> alarmHelperProvider) {
    this.alarmHelperProvider = alarmHelperProvider;
  }

  @Override
  public ScheduleFakeCallUseCase get() {
    return newInstance(alarmHelperProvider.get());
  }

  public static ScheduleFakeCallUseCase_Factory create(Provider<AlarmHelper> alarmHelperProvider) {
    return new ScheduleFakeCallUseCase_Factory(alarmHelperProvider);
  }

  public static ScheduleFakeCallUseCase newInstance(AlarmHelper alarmHelper) {
    return new ScheduleFakeCallUseCase(alarmHelper);
  }
}
