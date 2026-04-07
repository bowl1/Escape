package com.libowen.fakecall.domain.usecase;

import com.libowen.fakecall.domain.repository.CallerRepository;
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
public final class SaveCallerPresetUseCase_Factory implements Factory<SaveCallerPresetUseCase> {
  private final Provider<CallerRepository> repositoryProvider;

  private SaveCallerPresetUseCase_Factory(Provider<CallerRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public SaveCallerPresetUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static SaveCallerPresetUseCase_Factory create(
      Provider<CallerRepository> repositoryProvider) {
    return new SaveCallerPresetUseCase_Factory(repositoryProvider);
  }

  public static SaveCallerPresetUseCase newInstance(CallerRepository repository) {
    return new SaveCallerPresetUseCase(repository);
  }
}
