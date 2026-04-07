package com.libowen.fakecall.system;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class RingtoneHelper_Factory implements Factory<RingtoneHelper> {
  private final Provider<Context> contextProvider;

  private RingtoneHelper_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public RingtoneHelper get() {
    return newInstance(contextProvider.get());
  }

  public static RingtoneHelper_Factory create(Provider<Context> contextProvider) {
    return new RingtoneHelper_Factory(contextProvider);
  }

  public static RingtoneHelper newInstance(Context context) {
    return new RingtoneHelper(context);
  }
}
