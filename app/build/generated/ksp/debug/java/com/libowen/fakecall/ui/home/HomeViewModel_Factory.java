package com.libowen.fakecall.ui.home;

import com.libowen.fakecall.domain.repository.SettingsRepository;
import com.libowen.fakecall.domain.usecase.CancelFakeCallUseCase;
import com.libowen.fakecall.domain.usecase.GenerateRandomCallerUseCase;
import com.libowen.fakecall.domain.usecase.GetCallerInfoUseCase;
import com.libowen.fakecall.domain.usecase.SaveCallerPresetUseCase;
import com.libowen.fakecall.domain.usecase.ScheduleFakeCallUseCase;
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<GenerateRandomCallerUseCase> generateRandomCallerProvider;

  private final Provider<ScheduleFakeCallUseCase> scheduleFakeCallProvider;

  private final Provider<CancelFakeCallUseCase> cancelFakeCallProvider;

  private final Provider<GetCallerInfoUseCase> getCallerInfoProvider;

  private final Provider<SaveCallerPresetUseCase> saveCallerPresetProvider;

  private final Provider<SettingsRepository> settingsRepositoryProvider;

  private HomeViewModel_Factory(Provider<GenerateRandomCallerUseCase> generateRandomCallerProvider,
      Provider<ScheduleFakeCallUseCase> scheduleFakeCallProvider,
      Provider<CancelFakeCallUseCase> cancelFakeCallProvider,
      Provider<GetCallerInfoUseCase> getCallerInfoProvider,
      Provider<SaveCallerPresetUseCase> saveCallerPresetProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    this.generateRandomCallerProvider = generateRandomCallerProvider;
    this.scheduleFakeCallProvider = scheduleFakeCallProvider;
    this.cancelFakeCallProvider = cancelFakeCallProvider;
    this.getCallerInfoProvider = getCallerInfoProvider;
    this.saveCallerPresetProvider = saveCallerPresetProvider;
    this.settingsRepositoryProvider = settingsRepositoryProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(generateRandomCallerProvider.get(), scheduleFakeCallProvider.get(), cancelFakeCallProvider.get(), getCallerInfoProvider.get(), saveCallerPresetProvider.get(), settingsRepositoryProvider.get());
  }

  public static HomeViewModel_Factory create(
      Provider<GenerateRandomCallerUseCase> generateRandomCallerProvider,
      Provider<ScheduleFakeCallUseCase> scheduleFakeCallProvider,
      Provider<CancelFakeCallUseCase> cancelFakeCallProvider,
      Provider<GetCallerInfoUseCase> getCallerInfoProvider,
      Provider<SaveCallerPresetUseCase> saveCallerPresetProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    return new HomeViewModel_Factory(generateRandomCallerProvider, scheduleFakeCallProvider, cancelFakeCallProvider, getCallerInfoProvider, saveCallerPresetProvider, settingsRepositoryProvider);
  }

  public static HomeViewModel newInstance(GenerateRandomCallerUseCase generateRandomCaller,
      ScheduleFakeCallUseCase scheduleFakeCall, CancelFakeCallUseCase cancelFakeCall,
      GetCallerInfoUseCase getCallerInfo, SaveCallerPresetUseCase saveCallerPreset,
      SettingsRepository settingsRepository) {
    return new HomeViewModel(generateRandomCaller, scheduleFakeCall, cancelFakeCall, getCallerInfo, saveCallerPreset, settingsRepository);
  }
}
