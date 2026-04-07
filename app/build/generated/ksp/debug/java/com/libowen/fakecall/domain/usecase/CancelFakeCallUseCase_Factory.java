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
public final class CancelFakeCallUseCase_Factory implements Factory<CancelFakeCallUseCase> {
  private final Provider<AlarmHelper> alarmHelperProvider;

  private CancelFakeCallUseCase_Factory(Provider<AlarmHelper> alarmHelperProvider) {
    this.alarmHelperProvider = alarmHelperProvider;
  }

  @Override
  public CancelFakeCallUseCase get() {
    return newInstance(alarmHelperProvider.get());
  }

  public static CancelFakeCallUseCase_Factory create(Provider<AlarmHelper> alarmHelperProvider) {
    return new CancelFakeCallUseCase_Factory(alarmHelperProvider);
  }

  public static CancelFakeCallUseCase newInstance(AlarmHelper alarmHelper) {
    return new CancelFakeCallUseCase(alarmHelper);
  }
}
